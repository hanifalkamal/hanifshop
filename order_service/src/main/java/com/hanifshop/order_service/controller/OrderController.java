package com.hanifshop.order_service.controller;

import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.interceptor.Token;
import com.hanifshop.order_service.interceptor.ValidateSession;
import com.hanifshop.order_service.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public interface OrderController {

    @GetMapping(Constant.ControllerRoute.listOrder)
    ResponseEntity<?> listOrder(
            OrderDto orderDto,
            @Token String token
    );

    @GetMapping(Constant.ControllerRoute.listOrderByStatus)
    ResponseEntity<?> listOrderByStatus(
            OrderDto orderDto,
            @Token String token
    );

    @GetMapping(Constant.ControllerRoute.totalOrder)
    ResponseEntity<?> totalOrder(
            OrderDto orderDto,
            @Token String token
    );

    @GetMapping(Constant.ControllerRoute.totalAmount)
    ResponseEntity<?> totalAmount(
            OrderDto orderDto,
            @Token String token
    );


    @PostMapping(Constant.ControllerRoute.createOrder)
    ResponseEntity<?> createOrder(
            OrderDto orderDto,
            @Token String token
    );

    @PostMapping(Constant.ControllerRoute.createOrderDetail)
    ResponseEntity<?> addOrderDetail(
            OrderDetailDto orderDetailDto,
            @Token String token
    );


}
