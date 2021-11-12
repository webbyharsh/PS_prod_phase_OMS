package com.oms.exchange.service;

import com.oms.exchange.dto.OrderRequest;
import com.oms.exchange.dto.OrderResponse;

import java.util.concurrent.CompletableFuture;

public interface IExchangeService {
    public  CompletableFuture<OrderResponse> exchangeService(OrderRequest order) throws InterruptedException;

    CompletableFuture<Boolean> acknowledgeOrderService(Long orderId, String accepted, String s) throws InterruptedException;

    CompletableFuture<Boolean> sendAcceptedOrderToFillService(OrderRequest request) throws InterruptedException;
}
