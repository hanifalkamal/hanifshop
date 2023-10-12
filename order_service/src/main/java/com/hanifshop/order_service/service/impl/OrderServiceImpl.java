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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderDetailDao orderDetailDao;

    @Autowired
    private KafkaProducer kafkaProducer;

    private final Map<Long, CompletableFuture<Boolean>> validationFutures = new ConcurrentHashMap<>();


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
        try {

            if (dto.getOrderId() == 0L || dto.getOrderId() == null)
                throw  new Exception("Order ID is mandatory");

            if (dto.getProductId() == 0L || dto.getProductId() == null)
                throw new Exception("Product ID is mandatory");

            if (dto.getQuantity() == 0)
                throw new Exception("Qty is mandatory");

            Order order = checkOrderAvailablity(dto.getOrderId());


            CompletableFuture<Boolean> validationFuture = new CompletableFuture<>();
            validationFutures.put(dto.getProductId(), validationFuture);

            //TAMBAHKAN UNTUK REQUEST HARGA KE MODUL PRODUCT
            kafkaProducer.sendValidationRequest(dto.getProductId().toString(), dto.getQuantity());

            validationFuture.thenAccept(isQtyValid -> {
                if (!isQtyValid){
                    validationFuture.complete(false);
                } else {
                    validationFuture.complete(true);
                }
            });

            Boolean isQtyValid = validationFuture.get();

            if (!isQtyValid)
                throw new Exception("insufficient product");

            return executeCreateOrderDetail(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrder);
        }
    }

    @Override
    public Map<String, Object> listOrderDetail(OrderDetailDto dto) {
        return null;
    }

    @Override
    public Map<String, Object> listProducts() {
        return null;
    }

    @Override
    public Map<String, Object> executeCreateOrderDetail(OrderDetailDto dto) {
        OrderDetail orderDetail = dto.toOrderDetail();
        orderDetailDao.save(orderDetail);

        return EngineUtils.createSuccessReponse(200,
                orderDetail.createResponse(), Constant.ControllerRoute.createOrder);
    }

    @Override
    public Map<Long, CompletableFuture<Boolean>> getValidationFutures() {
        return validationFutures;
    }

    Order checkOrderAvailablity(Long orderId) throws Exception {
        List<Order> orders = orderDao.findByOrderId(orderId);

        if (orders.isEmpty())
            throw new Exception("Order not found");

        return orders.get(0);
    }
}
