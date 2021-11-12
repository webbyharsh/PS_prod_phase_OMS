package com.oms.order.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import com.oms.order.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.oms.order.entities.Order;
import com.oms.order.exceptions.BadRequestException;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.service.IOrderService;
import com.oms.order.service.ISearchOrderService;
import com.oms.order.service.OrderValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class OrderController {

	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private ISearchOrderService searchOrderService;

	@Autowired
	private OrderValidator orderValidator;

	RestTemplate restTemplate = new RestTemplate();

	@Value("${exchange_server_url}")
	private String exchangeServerURL;

	@Value("${receive_order_api}")
	private String receiveOrderAPI;

	// Receives order details as json
	@PostMapping("/order")
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest newOrderRequest,
			@RequestHeader("userId") Long userId, @RequestHeader("Authorization") String authorization) throws BadRequestException {

		log.info("Order Data Submitted By User");

		orderValidator.validateOrderRequest(newOrderRequest);
		log.info("Order Data Validated by Order Validator");

		Order newOrder = new Order(newOrderRequest, userId);

		// calls method of order service to create an order
		Order savedOrder = orderService.saveOrder(newOrder);
		log.info("Returned from Create New Order Method");

		// send notification of order creation
		orderService.sendNotification(savedOrder);

		OrderResponse orderResponse = new OrderResponse(savedOrder);
		log.info("Created and Returning Order Response Object");

		// returns the saved order and status created
		return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);

	}
	
	// Sends order to exchange
	@PutMapping("/order/send/{orderId}")
	public ResponseEntity<Order> sendOrderById(@PathVariable("orderId") String orderId,
			@RequestHeader("userId") String userId, @RequestHeader("Authorization") String authorization)
			throws NumberFormatException, OrderNotFoundException, BadRequestException {
		log.info("Received ID of order to be sent");

		Order order = orderService.getOrderById(Long.parseLong(userId), Long.parseLong(orderId));
		log.info("Order obtained from database using orderId and userId");

		log.info("Checking if status of order is CREATED");
			if (order.getStatus().toString().equals("CREATED")) {
				log.info("Checked and found that status of order is CREATED");

				String exchangeResponse = restTemplate.postForObject(exchangeServerURL + receiveOrderAPI, order, String.class);
				log.info("Order sent to exchange. Response from Exchange: " + exchangeResponse);

				if (exchangeResponse!= null && exchangeResponse.equals("RECEIVED")) {
					order.setStatus(Order.Status.valueOf("SENT"));
					log.info("Changed status of order from CREATED to SENT");

					order.setModifiedBy(Long.parseLong(userId));
					order.setModifiedAt(LocalDateTime.now());
					log.info("Changed \"Modified\" fields");

					Order sentOrder = orderService.saveOrder(order);
					log.info("Saved modified order");

					return new ResponseEntity<>(sentOrder, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(order, HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				log.info("Target order is " + order.getStatus().toString() + " and thus can't be SENT");
				throw new BadRequestException("This order can not be sent");
			}
	}

	// Get one order by taking one order id
	@GetMapping("/order/{orderid}")
	public ResponseEntity<Order> getOrderById(@PathVariable("orderid") String orderID,
											  @RequestHeader("Authorization") String jwt, @RequestHeader("userId")  Long userId) {
		try {
			Long orderId = Long.valueOf(orderID);
			Order order = orderService.getOrderById(userId, orderId);
			log.info(String.format("user id - %s user id - %s", userId, orderId));
			return new ResponseEntity<>(order, HttpStatus.OK);

		} catch (Exception e) {
			log.error("404 Not Found Error");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	// end point to get all orders for particular broker
	@GetMapping("/order")
	public ResponseEntity<OrderListResponse> getOrderListByBrokerId(@RequestHeader("userId") String id,
			@RequestHeader(value = "pageIndex", defaultValue = "0") Integer pageIndex,
			@RequestHeader(value = "pageSize", defaultValue = "5") Integer pageSize, @RequestHeader(value="Authorization") String authorization) throws BadRequestException {
		if (!id.matches("[0-9]+") || Long.parseLong(id) < 0) {
			log.warn("The header userId is not formatted properly");
			throw new BadRequestException("The supplied parameter is not correct");
		}
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<Order> pageOrder = orderService.getOrdersByBrokerId(Long.parseLong(id), pageable);
		List<Order> listOrder = pageOrder.getContent();
		log.info("Order List Fetch Successful");
		Integer totalPages = pageOrder.getTotalPages();
		Long totalOrders = pageOrder.getTotalElements();
		Boolean isFirstPage = pageOrder.isFirst();
		Boolean isLastPage = pageOrder.isLast();
		Integer pageNumber = pageOrder.getNumber();
		if (!pageOrder.getContent().isEmpty()) {
			log.info("Orders found for user");
			return ResponseEntity.status(HttpStatus.OK).body(new OrderListResponse("SUCCESS", listOrder, totalPages,
					totalOrders, isFirstPage, isLastPage, pageNumber));
		} else {
			log.info("No orders found for user");
			return ResponseEntity.status(HttpStatus.OK).body(new OrderListResponse("NO_ORDERS", listOrder, totalPages,
					totalOrders, isFirstPage, isLastPage, pageNumber));
		}

	}

	@PutMapping("/order/{orderId}")
	public ResponseEntity<Order> updateOrderById(@Valid @RequestBody OrderRequest updateOrderRequest,
			@PathVariable("orderId") String orderStrId, @RequestHeader("userId") Long userId, @RequestHeader("Authorization") String authorization)
			throws BadRequestException, OrderNotFoundException {
		log.info("Update Order request received and forwarded to Validator");
		orderValidator.validateOrderRequest(updateOrderRequest);
		log.info("New request is validated and now being forwarded to OrderService");

		updateOrderRequest.setOrderId(Long.parseLong(orderStrId));
		Order orderToBeUpdated = orderService.getOrderById(userId, updateOrderRequest.getOrderId());
		Order orderAfterUpdate = orderService.getOrderAfterRequestedUpdates(orderToBeUpdated, updateOrderRequest);
		Order responseOrder = orderService.updateOrder(orderAfterUpdate, userId);

		log.info("Updated order received back from service and being sent as API response");
		return new ResponseEntity<>(responseOrder, HttpStatus.OK);
	}

	@PutMapping("/order/ack")
	public ResponseEntity<Order> acknowledgeOrderFromExchange(@RequestHeader("OrderId") Long orderId, @RequestHeader("Status") String status, @RequestHeader("Reason") String reason) throws BadRequestException {
		log.info("Acknowledgement from exchange service received.");
		String rejected = "REJECTED";
		try {
			Order orderToBeAcknowledged = orderService.getOrderById(orderId);
			if(orderToBeAcknowledged.getStatus().toString().equals(rejected) || orderToBeAcknowledged.getStatus().toString().equals("CANCELLED")) {
				throw new BadRequestException("The status of already rejected order can not be changed");
			} else {
				if (status.equals("ACCEPTED")) {

					orderToBeAcknowledged.setStatus(Order.Status.valueOf("ACCEPTED"));
					log.info("Order has been accepted by exchange service");
				}
				if (status.equals(rejected)) {
					orderToBeAcknowledged.setStatus(Order.Status.valueOf(rejected));
					log.info("Order has been rejected by exchange service");
					log.info(reason);
				}
			}
			Order orderAcknowledged = orderService.saveOrder(orderToBeAcknowledged);
			orderService.sendNotification(orderAcknowledged);
			return new ResponseEntity<>(orderAcknowledged, HttpStatus.OK);
		} catch (BadRequestException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<Order> cancelOrderById(@PathVariable("orderId") String orderStrId,
			@RequestHeader("userId") Long userId, @RequestHeader("Authorization") String authorization) throws BadRequestException {
		try {
			log.info("Getting the order to be cancelled");
			Long orderId = Long.parseLong(orderStrId);
			Order getOrder = orderService.getOrderById(userId, orderId);
			if (getOrder.getStatus().toString().equals("CREATED")) {
				log.info("setting the status to cancel order");
				getOrder.setStatus(Order.Status.valueOf("CANCELLED"));
				log.info("Updating modified by and modified at order details");
				getOrder.setModifiedBy(userId);
				getOrder.setModifiedAt(LocalDateTime.now());
				log.info("Saving the cancelled order status");
				Order orderToBeCancelled = orderService.saveOrder(getOrder);
				orderService.sendNotification(orderToBeCancelled);
				return new ResponseEntity<>(orderToBeCancelled, HttpStatus.OK);
			} else {
				throw new BadRequestException("The status of order can not be cancelled");
			}
		} catch (BadRequestException e) {
			throw e;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Search API 
	// Get list of orders filtered by stock name
	@PutMapping("/search-order")
	public ResponseEntity<OrderListResponse> searchOrders(
			@Valid @RequestBody SearchOrdersRequest searchByRequest,
			@RequestHeader("Authorization") String jwt,
			@RequestHeader("userId") Long userId,
			@RequestHeader(value = "pageIndex", defaultValue = "0") Integer pageIndex,
			@RequestHeader(value = "pageSize", defaultValue = "5") Integer pageSize
		) throws BadRequestException, OrderNotFoundException {
		try {
		if(!jwt.isEmpty()) {
			Pageable pageable = PageRequest.of(pageIndex, pageSize);
			Page<Order> pageOrder = searchOrderService.getSearchOrders(searchByRequest, userId, pageable);
			List<Order> listOrder = pageOrder.getContent();
			log.info("Order List Fetch Successful");
			Integer totalPages = pageOrder.getTotalPages();
			Long totalOrders = pageOrder.getTotalElements();
			Boolean isFirstPage = pageOrder.isFirst();
			Boolean isLastPage = pageOrder.isLast();
			Integer pageNumber = pageOrder.getNumber();
			if (!pageOrder.getContent().isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK).body(new OrderListResponse("SUCCESS", listOrder, totalPages,
						totalOrders, isFirstPage, isLastPage, pageNumber));
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			log.error("401 Unauthorized Error");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	catch (Exception e) {
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
	
}