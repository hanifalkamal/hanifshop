package com.hanifshop.order_service.service;

import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface OrderService {

    Map<String, Object> createOrder(OrderDto orderDto);
    Map<String, Object> listOrder(OrderDto orderDto);
    Map<String, Object> createOrderDetail(OrderDetailDto dto);
    Map<String, Object> listOrderDetail(OrderDetailDto dto);
    Map<String, Object> listProducts();




}
