package com.oms.fill.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.entities.Order;
import com.oms.fill.exceptions.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FillService implements IFillService {

	@Autowired
	UpdateOrderService updateOrderService;
	@Autowired
	NotificationService notificationService;

	public boolean fillOrder(FillRequest rq) throws OrderNotFoundException {
		Order order = updateOrderService.updateOrder(rq);
		boolean a = false;
		boolean b = false;
		Fill fill = checkFill(rq.getQuantity(), order.getQuantity());
		if (order != null) {
			a = notificationService.sendNotificationToBroker(order, fill);
			b = notificationService.sendNotificationToClient(order, fill);
			log.info(String.format("Order - %s filled successfully", rq.getOrderId()));
		}

		return (a && b);
	}

	public Fill checkFill(int fillQuantity, int orderQuantity) {
		if (fillQuantity < orderQuantity) {
			return Fill.PARTIALFILLED;

		} else {
			return Fill.FULLYFILLED;
		}

	}

}
