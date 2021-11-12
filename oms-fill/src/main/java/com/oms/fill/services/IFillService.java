package com.oms.fill.services;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.exceptions.OrderNotFoundException;

public interface IFillService {

	// Update order function
	public boolean fillOrder(FillRequest rq) throws OrderNotFoundException;

}
