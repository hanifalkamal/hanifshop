package com.hanifshop.order_service.controller.impl;

import com.hanifshop.order_service.controller.OrderController;
import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.interceptor.ValidateSession;
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
    @ValidateSession
    public ResponseEntity<?> listOrder(OrderDto orderDto, String token) {
        Map<String, Object> mapping = orderService.listOrder(orderDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @ValidateSession
    @Override
    public ResponseEntity<?> listOrderByStatus(OrderDto orderDto, String token) {
        Map<String, Object> mapping = orderService.listOrderByStatus(orderDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @ValidateSession
    @Override
    public ResponseEntity<?> totalOrder(OrderDto orderDto, String token) {
        Map<String, Object> mapping = orderService.totalOrder(orderDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @ValidateSession
    @Override
    public ResponseEntity<?> totalAmount(OrderDto orderDto, String token) {
        Map<String, Object> mapping = orderService.totalAmount(orderDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    @ValidateSession
    public ResponseEntity<?> createOrder(OrderDto orderDto, String token) {
        Map<String, Object> mapping = orderService.createOrder(orderDto, token);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    @ValidateSession
    public ResponseEntity<?> addOrderDetail(OrderDetailDto orderDetailDto, String token) {
        Map<String, Object> mapping = orderService.addOrderDetail(orderDetailDto, token);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }
}
