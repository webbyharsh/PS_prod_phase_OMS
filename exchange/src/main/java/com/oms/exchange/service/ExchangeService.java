package com.oms.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.oms.exchange.dto.FillResponse;
import com.oms.exchange.dto.OrderRequest;
import com.oms.exchange.dto.OrderResponse;
import com.oms.exchange.entities.Stocks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class ExchangeService implements IExchangeService {


	RestTemplate restTemplate=new RestTemplate();
	Stocks object = new Stocks();
	@Autowired
	private IFulfillService fulfillService;
	@Value("${order.ack.api}")
	public String ackAPI;

	@Value("${fill.api}")
	public String fillAPI;
	
	@Async
	public CompletableFuture<OrderResponse> exchangeService(OrderRequest request) throws InterruptedException {
		OrderResponse response = new OrderResponse();
		String limit = "LIMIT";

		response.setOrderId(request.getOrderId());
		String nameOfStock = request.getStock();
		String message = "";
		// checks if fields in request is null
		if (request.getType().compareToIgnoreCase("MARKET") != 0
				&& request.getType().compareToIgnoreCase(limit) != 0) {
			message = "Type must be MARKET or LIMIT";
		} else if (request.getQuantity() > 1000 || request.getQuantity() <= 0) {

			message = "Quantity should be between 0 & 1001";
		} else if (!object.getStockSet().contains(nameOfStock)) {

			message = "Selected Stock is invalid";
		}
		// other validations

		else if (request.getType().equalsIgnoreCase("MARKET") && request.getTargetPrice() != null) {

			message = "Target price should be null for Market order";
		} else if (request.getType().equalsIgnoreCase(limit) && request.getTargetPrice() == null) {

			message = "Target price is null for Limit order";
		}

		else if (request.getType().equalsIgnoreCase(limit) && request.getTargetPrice().signum() < 0) {

			message = "Negative Price for Limit order";
		}

		else {
			response.setStatus("ACCEPTED");
			response.setReason("All condition validated");
			log.info(String.format("orderid - %s status - %s reason - %s", response.getOrderId(), response.getStatus(),
					response.getReason()));
			acknowledgeOrderService(request.getOrderId(),response.getStatus(), response.getReason()).exceptionally(throwable -> {
				return null;
			});
			sendAcceptedOrderToFillService(request) .exceptionally(throwable -> {
				log.info("Error in completable future sendAcceptedOrderToFillService");
				return null;
			});
            log.info("Order is validated and sent to further processing");
			return CompletableFuture.completedFuture(response);
		}
		response.setStatus("REJECTED");
		response.setReason(message);
		acknowledgeOrderService(request.getOrderId(),response.getStatus(), response.getReason()) .exceptionally(throwable -> {
			return null;
		});
		log.info(String.format("orderid - %s status - %s reason - %s", response.getOrderId(), response.getStatus(),
				response.getReason()));

		return CompletableFuture.completedFuture(response);

	}

	@Override
	@Async
	public CompletableFuture<Boolean> acknowledgeOrderService(Long orderId , String status, String reason ) throws InterruptedException {
		log.info("Started executing ack method");
		String url = ackAPI;
		URI targetUrl = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.add("OrderId",String.valueOf(orderId));
		headers.add("Status",status);
		headers.add("Reason",reason);
		HttpEntity entity=new HttpEntity(headers);
		boolean result =true;
		try {
			log.info("Entered try block");
			restTemplate.put(targetUrl,entity);

		}
		catch(Exception e)
		{
			log.info("Entered catch block");
			result=false;
		}

		return CompletableFuture.completedFuture(result);
	}

	@Override
	@Async
	public CompletableFuture<Boolean> sendAcceptedOrderToFillService(OrderRequest request) throws InterruptedException {

		Boolean rejected = false;
		log.info("Executing sendAcceptedOrderToFillService");
		if(request.getType().equalsIgnoreCase("LIMIT")) {
			log.info("getting random rejection value");
			rejected = fulfillService.randomRejection();
			log.info("Rejected "+rejected);
		}
		if(rejected)
		{
			log.info("Send Ack for Rejection");
			this.acknowledgeOrderService(request.getOrderId(),"REJECTED","Order is rejected from filling").exceptionally(throwable -> {
				return null;
			});
			log.info("Rejected Order as Stocks are not available");
			return  CompletableFuture.completedFuture(rejected);
		}
		log.info("Creating fill request");
//		This response needs to be sent to Fill service
		FillResponse response = new FillResponse();
		response.setOrderId(request.getOrderId());
		response.setExchangeId(1L); // later this value will be taken from build parameters
		response.setStock(request.getStock());
		response.setQuantity(fulfillService.fillQuantity(request.getQuantity()));
		if(request.getTargetPrice()!=null) {
            response.setExecutedPrice(fulfillService.executedPrice(request.getTargetPrice()));
        } else {
			int randomNum = ThreadLocalRandom.current().nextInt(500, 3000);
            BigDecimal randFromDouble = BigDecimal.valueOf(randomNum);
            response.setExecutedPrice(randFromDouble);
        }
		response.setExecutedTime( LocalDateTime.now());
		log.info(response.toString());


		String url = fillAPI;
		URI targetUrl = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		HttpEntity<FillResponse> entity=new HttpEntity(response);
		try {
			log.info("Entered Time try");
			String val=restTemplate.postForObject(targetUrl,entity,String.class);
			log.info("FillResponse"+val);
		}
		catch(Exception e)
		{
			log.info("Time catch");

		}



		log.info("Response to fill will be:\n" + response.toString()); // remove it after testing

		return CompletableFuture.completedFuture(!rejected);
	}

}
