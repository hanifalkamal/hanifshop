package com.hanifshop.order_service.dto;

import com.hanifshop.order_service.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OrderDto {
    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private Date orderDate;
    private String status;
    private BigDecimal totalAmount;

    public static OrderDto fromOrder(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerId(order.getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        return dto;
    }

    public Order toOrder() {
        Order order = new Order();
        order.setOrderId(this.orderId);
        order.setOrderNumber(this.orderNumber);
        order.setCustomerId(this.customerId);
        order.setOrderDate(this.orderDate);
        order.setStatus(this.status);
        order.setTotalAmount(this.totalAmount);
        return order;
    }
}
