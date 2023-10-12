package com.hanifshop.order_service.repository;

import com.hanifshop.order_service.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetail, Integer> {

    OrderDetail findByOrderDetailId(Long orderDetailId);

    List<OrderDetail> findByOrder_OrderId(Long orderId);

}
