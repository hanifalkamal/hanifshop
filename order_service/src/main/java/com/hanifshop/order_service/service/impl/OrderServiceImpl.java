package com.hanifshop.order_service.service.impl;

import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.model.Order;
import com.hanifshop.order_service.model.OrderDetail;
import com.hanifshop.order_service.repository.OrderDao;
import com.hanifshop.order_service.repository.OrderDetailDao;
import com.hanifshop.order_service.service.OrderService;
import com.hanifshop.order_service.stream.KafkaProducer;
import com.hanifshop.order_service.util.Constant;
import com.hanifshop.order_service.util.EngineUtils;
import com.hanifshop.order_service.util.PojoJsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger =  LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private KafkaProducer kafkaProducer;

    private final Map<Long, CompletableFuture<String>> validationFutures = new ConcurrentHashMap<>();


    @Override
    public Map<String, Object> createOrder(OrderDto orderDto) {
        try {

            Order order = orderDto.toOrder();

            if (order.getOrderNumber() == null)
                throw new Exception("Order Number not found");

            if (order.getCustomerId() == null)
                throw new Exception("Customer ID not found");

            if (order.getStatus() == null)
                throw new Exception("Status not found");

            order.setOrderDate(new Date());
            order.setTotalAmount(new BigDecimal(0));
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
                    orderlist, Constant.ControllerRoute.listOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.listOrder);
        }
    }

    @Override
    public Map<String, Object> addOrderDetail(OrderDetailDto dto) {

        logger.info("Attempt to add Order Detail");

        try {

            if (dto.getOrderId() == 0L || dto.getOrderId() == null)
                throw  new Exception("Order ID is mandatory");

            if (dto.getProductId() == 0L || dto.getProductId() == null)
                throw new Exception("Product ID is mandatory");

            if (dto.getQuantity() == 0)
                throw new Exception("Qty is mandatory");

            checkOrderAvailablity(dto.getOrderId());

            CompletableFuture<String> validationFuture = new CompletableFuture<>();
            validationFutures.put(dto.getProductId(), validationFuture);

            kafkaProducer.sendValidationRequest(dto.getProductId().toString(), dto.getQuantity());

            validationFuture.thenAccept(response -> {
                validationFuture.complete(response);
            });

            String response = validationFuture.get();

            Map<String, String> data = PojoJsonMapper.fromJson(response, Map.class);

            if (data.containsKey("error"))
                throw new Exception(data.get("error"));

            Integer orderQty = dto.getQuantity();
            Integer availQty = Integer.parseInt(data.get("stockQuantity"));

            if (orderQty > availQty)
                throw new Exception("insufficient stock");

            dto.setUnitPrice(new BigDecimal(data.get("price")));
            dto.setTotalPrice(dto.getUnitPrice().multiply
                    (new BigDecimal(orderQty)));

            return executeCreateOrderDetail(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrderDetail);
        }
    }

//    @Override
//    public Map<String, Object> listOrderDetail(OrderDetailDto dto) {
//        try {
//
//            List<OrderDetail> orderDetails =
//                    (dto.getOrderDetailId() != null && dto.getOrderId() != null) ?
//                            orderDetailDao.findby(orderDto.getCustomerId(), orderDto.getOrderId()) :
//                            (orderDto.getOrderId() != null) ?
//                                    orderDao.findByOrderId(orderDto.getOrderId()) :
//                                    (orderDto.getCustomerId() != null) ?
//                                            orderDao.findByCustomerId(orderDto.getCustomerId()) :
//                                            orderDao.findAll();
//
//            List<Map<String, Object>> orderlist = orders.stream()
//                    .map(order -> {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("orderId", order.getOrderId());
//                        map.put("customerId", order.getCustomerId());
//                        map.put("orderNumber", order.getOrderNumber());
//                        map.put("statusOrder", order.getStatus());
//                        map.put("orderDate", order.getOrderDate());
//                        map.put("totalAmountOrder", order.getTotalAmount());
//                        return map;
//                    })
//                    .collect(Collectors.toList());
//
//            return EngineUtils.createSuccessReponse(200,
//                    orderlist, Constant.ControllerRoute.listOrder);
//
//        }catch (Exception e) {
//
//            e.printStackTrace();
//            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrder);
//
//        }
//    }

    @Override
    public Map<String, Object> executeCreateOrderDetail(OrderDetailDto dto) {
        OrderDetail orderDetail = dto.toOrderDetail();
        orderDetailDao.save(orderDetail);
        return EngineUtils.createSuccessReponse(200,
                orderDetail.createResponse(), Constant.ControllerRoute.createOrderDetail);
    }

    @Override
    public Map<Long, CompletableFuture<String>> getValidationFutures() {
        return validationFutures;
    }

    Order checkOrderAvailablity(Long orderId) throws Exception {
        List<Order> orders = orderDao.findByOrderId(orderId);

        if (orders.isEmpty())
            throw new Exception("Order not found");

        return orders.get(0);
    }
}
