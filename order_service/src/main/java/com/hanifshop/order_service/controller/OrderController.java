package com.hanifshop.order_service.controller;

import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public interface OrderController {

    @GetMapping(Constant.ControllerRoute.listOrder)
    ResponseEntity<?> listOrder(
            OrderDto orderDto
    );

    @PostMapping(Constant.ControllerRoute.createOrder)
    ResponseEntity<?> createOrder(
            OrderDto orderDto
    );

    @PostMapping(Constant.ControllerRoute.createOrderDetail)
    ResponseEntity<?> addOrderDetail(
            OrderDetailDto orderDetailDto
    );


}
