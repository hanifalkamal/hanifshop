package com.hanifshop.order_service.controller.impl;

import com.hanifshop.order_service.controller.OrderController;
import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Controller
public class OrderControllerImpl implements OrderController {

    @Autowired
    private OrderService orderService;

    @Override
    public ResponseEntity<?> listOrder(OrderDto orderDto) {
        Map<String, Object> mapping = orderService.listOrder(orderDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrder(OrderDto orderDto) {
        Map<String, Object> mapping = orderService.createOrder(orderDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addOrderDetail(OrderDetailDto orderDetailDto) {
        Map<String, Object> mapping = orderService.addOrderDetail(orderDetailDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }
}
