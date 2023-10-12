package com.hanifshop.order_service.service.impl;

import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.model.Order;
import com.hanifshop.order_service.repository.OrderDao;
import com.hanifshop.order_service.repository.OrderDetailDao;
import com.hanifshop.order_service.service.OrderService;
import com.hanifshop.order_service.util.Constant;
import com.hanifshop.order_service.util.EngineUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderDetailDao orderDetailDao;

    @Override
    public Map<String, Object> createOrder(OrderDto orderDto) {
        try {

            Order order = orderDto.toOrder();
            orderDao.save(order);

            return EngineUtils.createSuccessReponse(200,
                    order.createResponse(), Constant.ControllerRoute.createOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrder);
        }
    }

    @Override
    public Map<String, Object> listOrder(OrderDto orderDto) {
        try {

            List<Order> orders =
                    (orderDto.getOrderId() != null && orderDto.getCustomerId() != null) ?
                            orderDao.findByCustomerIdAndOrderId(orderDto.getCustomerId(), orderDto.getOrderId()) :
                            (orderDto.getOrderId() != null) ?
                                    orderDao.findByOrderId(orderDto.getOrderId()) :
                                    (orderDto.getCustomerId() != null) ?
                                            orderDao.findByCustomerId(orderDto.getCustomerId()) :
                                            orderDao.findAll();

            List<Map<String, Object>> orderlist = orders.stream()
                    .map(order -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("orderId", order.getOrderId());
                        map.put("customerId", order.getCustomerId());
                        map.put("orderNumber", order.getOrderNumber());
                        map.put("statusOrder", order.getStatus());
                        map.put("orderDate", order.getOrderDate());
                        map.put("totalAmountOrder", order.getTotalAmount());
                        return map;
                    })
                    .collect(Collectors.toList());

            return EngineUtils.createSuccessReponse(200,
                    orderlist, Constant.ControllerRoute.createOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrder);
        }
    }

    @Override
    public Map<String, Object> createOrderDetail(OrderDetailDto dto) {
        return null;
    }

    @Override
    public Map<String, Object> listOrderDetail(OrderDetailDto dto) {
        return null;
    }

    @Override
    public Map<String, Object> listProducts() {
        return null;
    }
}
