package com.hanifshop.order_service.util;


import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.model.Order;
import com.hanifshop.order_service.model.OrderDetail;
import com.hanifshop.order_service.repository.OrderDetailDao;
import com.hanifshop.order_service.repository.SessionDao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class EngineUtils {
    public static  Map<String, Object> createFailedReponse(Integer errorCode, String errorMessage, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("status", errorCode);
        result.put("error", errorMessage);
        result.put("path", path);
        return result;
    }

    public static  Map<String, Object> createSuccessReponse(Integer errorCode, String message, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("status", errorCode);
        result.put("message", message);
        result.put("path", path);
        return result;
    }

    public static  Map<String, Object> createSuccessReponse(Integer errorCode, Map<String, Object> data, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("status", errorCode);
        result.put("data", data);
        result.put("path", path);
        return result;
    }

    public static  Map<String, Object> createSuccessReponse(Integer errorCode, List<Map<String, Object>> data, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("status", errorCode);
        result.put("data", data);
        result.put("path", path);
        return result;
    }

    public static Boolean isValidToken(SessionDao sessionDao, String userId, String token){
        String redisKey = "auth_token:" + userId;
        String storedToken = sessionDao.findSessionByRedisKey(redisKey);

        return (token.equals(storedToken));

    }

    public static List<Map<String, Object>> toListMap(List<Order> orders, OrderDetailDao orderDetailDao){
        return orders.stream()
                .map(order -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId", order.getOrderId());
                    map.put("customerId", order.getCustomerId());
                    map.put("orderNumber", order.getOrderNumber());
                    map.put("statusOrder", order.getStatus());
                    map.put("orderDate", order.getOrderDate());
                    map.put("totalAmountOrder", order.getTotalAmount());
                    map.put("orderDetail", orderDetailDao.findByOrder_OrderId(order.getOrderId()) == null
                            ? "[]" : orderDetailforList(orderDetailDao.findByOrder_OrderId(order.getOrderId()))  );
                    return map;
                })
                .collect(Collectors.toList());
    }

    public static List<OrderDetailDto> orderDetailforList(List<OrderDetail> orders) {
        return orders.stream()
                .map(orderDetail -> {
                    OrderDetailDto newOrderDetail = new OrderDetailDto();
                    newOrderDetail.setOrderDetailId(orderDetail.getOrderDetailId());
                    newOrderDetail.setProductId(orderDetail.getProductId());
                    newOrderDetail.setQuantity(orderDetail.getQuantity());
                    newOrderDetail.setUnitPrice(orderDetail.getUnitPrice());
                    newOrderDetail.setTotalPrice(orderDetail.getTotalPrice());
                    newOrderDetail.setOrderId(orderDetail.getOrder().getOrderId());
                    return newOrderDetail;
                })
                .collect(Collectors.toList());
    }







}
