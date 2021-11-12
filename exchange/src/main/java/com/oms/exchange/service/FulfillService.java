package com.oms.exchange.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
@Service
public class FulfillService implements IFulfillService {

	private Random rand = new Random();

	public Integer fillQuantity(Integer receivedQuantity) {
		Integer filledQuantity;
		if (rand.nextInt(100) < 40) {
			filledQuantity = rand.nextInt(receivedQuantity) + 1;
			return filledQuantity;
		} else {
			return receivedQuantity;
		}
	}
	
	public BigDecimal executedPrice(BigDecimal receivedTargetPrice) {
		BigDecimal limitPrice = receivedTargetPrice.multiply(BigDecimal.valueOf(rand.nextDouble()/2));
		return randomBig(limitPrice);
	}
	
	public boolean randomRejection() {
		if(rand.nextInt(100) < 10) {
			return true;
		}
		return false;
	}
	
	public static BigDecimal randomBig(BigDecimal max) {
		double randValue = Math.random();
		BigDecimal randFromDouble = BigDecimal.valueOf(randValue);
		BigDecimal actualRandomDec = randFromDouble.multiply(max);
		actualRandomDec = actualRandomDec
				.setScale(2, RoundingMode.DOWN);
		return actualRandomDec;
    }
}
