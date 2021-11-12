package com.oms.exchange.service;

import java.math.BigDecimal;

public interface IFulfillService {
	
	public Integer fillQuantity(Integer receivedQuantity);
	
	public BigDecimal executedPrice(BigDecimal receivedTargetPrice);
	
	public boolean randomRejection();
}
