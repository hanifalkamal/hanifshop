package com.hanifshop.order_service.repository;

import com.hanifshop.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    List<Order> findByOrderId(Long orderId);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdAndOrderId(Long customerId, Long orderId);
}
