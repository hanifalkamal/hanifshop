package com.hanifshop.order_service.repository;

import com.hanifshop.order_service.model.Order;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    List<Order> findByOrderId(Long orderId);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdAndOrderId(Long customerId, Long orderId);

    @Query(value = "SELECT COUNT(*) FROM product_order po WHERE po.customer_id = :customerId", nativeQuery = true)
    Long countTotalOrdersByCustomer(@Param("customerId") Long customerId);

    @Query(value = "SELECT SUM(po.total_amount) as total " +
            "FROM order_detail od " +
            "INNER JOIN product_order po ON od.order_id = po.order_id " +
            "WHERE po.customer_id =" +
            " :customerId", nativeQuery = true)
    BigDecimal sumTotalAmountByCustomer(@Param("customerId") Long customerId);

    @Query(value = "SELECT po.customer_id, po.order_number, po.order_id,po.order_date,po.status, po.total_amount, od.unit_price, od.quantity, od.total_price " +
            "FROM product_order po  " +
            "INNER JOIN order_detail od  " +
            "WHERE po.status = :status ORDER BY po.order_date DESC", nativeQuery = true)
    List<Order> findOrdersByStatusAndLatestOrderDate(@Param("status") String status);

}
