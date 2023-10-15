package com.hanifshop.order_service.dto;

import com.hanifshop.order_service.model.Order;
import com.hanifshop.order_service.model.OrderDetail;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderDetailDto extends BaseDto {
    private Long orderDetailId;
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public static OrderDetailDto fromOrderDetail(OrderDetail orderDetail) {
        OrderDetailDto dto = new OrderDetailDto();
        dto.setOrderDetailId(orderDetail.getOrderDetailId());
        dto.setOrderId(orderDetail.getOrder().getOrderId());
        dto.setProductId(orderDetail.getProductId());
        dto.setQuantity(orderDetail.getQuantity());
        dto.setUnitPrice(orderDetail.getUnitPrice());
        dto.setTotalPrice(orderDetail.getTotalPrice());
        return dto;
    }

    public OrderDetail toOrderDetail() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(this.orderDetailId);
        Order order = new Order();
        order.setOrderId(this.orderId);
        orderDetail.setOrder(order);
        orderDetail.setProductId(this.productId);
        orderDetail.setQuantity(this.quantity);
        orderDetail.setUnitPrice(this.unitPrice);
        orderDetail.setTotalPrice(this.totalPrice);
        return orderDetail;
    }
}
