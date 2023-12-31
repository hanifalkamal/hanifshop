package com.hanifshop.order_service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    public Map<String, Object> createResponse(){
        return Stream.of(
                Map.entry("orderDetailId", this.orderDetailId),
                Map.entry("orderId", this.order.getOrderId())
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

//    public List<Map<String, Object>> createResponseDetail() {
//        Map<String, Object> detailMap = Stream.of(
//                Map.entry("orderDetailId", this.orderDetailId),
//                Map.entry("productId", this.productId),
//                Map.entry("quantity", this.quantity),
//                Map.entry("unitPrice", this.unitPrice),
//                Map.entry("totalPrice", this.totalPrice)
//        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//        return List.of(detailMap);
//    }

    public List<OrderDetail> createResponseDetail() {
        return Stream.of(this)
                .map(orderDetail -> {
                    OrderDetail newOrderDetail = new OrderDetail();
                    newOrderDetail.setOrderDetailId(orderDetail.orderDetailId);
                    newOrderDetail.setProductId(orderDetail.productId);
                    newOrderDetail.setQuantity(orderDetail.quantity);
                    newOrderDetail.setUnitPrice(orderDetail.unitPrice);
                    newOrderDetail.setTotalPrice(orderDetail.totalPrice);
                    return newOrderDetail;
                })
                .collect(Collectors.toList());
    }


}
