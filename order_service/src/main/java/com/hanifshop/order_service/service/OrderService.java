package com.hanifshop.order_service.service;

import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.model.Order;
import com.hanifshop.order_service.model.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


public interface OrderService {

    Map<String, Object> createOrder(OrderDto orderDto);
    Map<String, Object> updateOrder(OrderDto orderDto);
    Map<String, Object> listOrder(OrderDto orderDto);
    Map<String, Object> addOrderDetail(OrderDetailDto dto);
    Map<String, Object> removeOrderDetail(OrderDetailDto dto);

    Map<String, Object> executeCreateOrderDetail(OrderDetailDto dto);
    Map<Long, CompletableFuture<String>> getValidationFutures();




}
