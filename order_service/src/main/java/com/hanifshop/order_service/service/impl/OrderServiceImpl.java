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
import com.hanifshop.order_service.util.HttpUtil;
import com.hanifshop.order_service.util.PojoJsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private KafkaProducer kafkaProducer;

    private final Map<Long, CompletableFuture<String>> validationFutures = new ConcurrentHashMap<>();


    @Override
    public Map<String, Object> createOrder(OrderDto orderDto, String bearerToken) {
        try {

            logger.info("Attempt to create order");

            Order order = orderDto.toOrder();

            if (order.getOrderNumber() == null)
                throw new Exception("Order number not found");

            if (order.getCustomerId() == null)
                throw new Exception("Customer ID not found");

            order.setOrderDate(new Date());
            order.setTotalAmount(new BigDecimal(0));
            order.setStatus(Constant.STATUS.PENDING);
            orderDao.save(order);

            return EngineUtils.createSuccessReponse(200,
                    order.createResponse(), Constant.ControllerRoute.createOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrder);
        }
    }

    @Override
    public Map<String, Object> updateOrder(OrderDto orderDto) {
        try {

            Order order = checkOrderAvailablity(orderDto.getOrderId());

            if (!order.getStatus().equals(Constant.STATUS.PENDING))
                throw new Exception("only PENDING order can be disposed");

            order.setStatus(orderDto.getStatus());
            orderDao.save(order);

            return EngineUtils.createSuccessReponse(200,
                    order.createResponse(), Constant.ControllerRoute.createOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrderDetail);
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

            return EngineUtils.createSuccessReponse(200,
                    EngineUtils.toListMap(orders, orderDetailDao), Constant.ControllerRoute.listOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.listOrder);
        }
    }

    @Override
    public Map<String, Object> addOrderDetail(OrderDetailDto dto, String token) {

        logger.info("Attempt to add Order Detail");

        try {

            if (dto.getOrderId() == 0L || dto.getOrderId() == null)
                throw new Exception("Order ID is mandatory");

            if (dto.getProductId() == 0L || dto.getProductId() == null)
                throw new Exception("Product ID is mandatory");

            if (dto.getQuantity() == 0)
                throw new Exception("Qty is mandatory");

            Order order = checkOrderAvailablity(dto.getOrderId());

            if (!order.getStatus().equals(Constant.STATUS.PENDING))
                throw new Exception("Unable to add order : " + order.getStatus());

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            requestHeaders.add(HttpHeaders.AUTHORIZATION, token);

            String url = Constant.baseUrl + Constant.getProduct;
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
            uriBuilder.queryParam("productId", dto.getProductId());
            ResponseEntity<String> request = HttpUtil.callServiceGETParam(url, uriBuilder, requestHeaders);

            Map<String, Object> body = PojoJsonMapper.fromJson(request.getBody(), Map.class);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) body.get("data");
            Map<String, Object> product = dataList.get(0);

            int orderQty = dto.getQuantity();
            int availQty = Integer.parseInt(String.valueOf(product.get("stockQuantity")));

            if (orderQty > availQty)
                throw new Exception("insufficient stock");

            dto.setUnitPrice(new BigDecimal(String.valueOf(product.get("price"))));
            dto.setTotalPrice(dto.getUnitPrice().multiply
                    (new BigDecimal(orderQty)));

            return executeCreateOrderDetail(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrderDetail);
        }
    }

    @Override
    public Map<String, Object> removeOrderDetail(OrderDetailDto dto) {

        logger.info("Attempt to remove Order Detail");

        try {

            if (dto.getOrderDetailId() == 0L || dto.getOrderDetailId() == null)
                throw new Exception("Order Detail ID not found");

            OrderDetail orderDetail = dto.toOrderDetail();
//            get qty
            //Remove orderDetail
            //Send kafka message to update qty



        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.createOrderDetail);
        }
        return null;
    }

    @Override
    public Map<String, Object> executeCreateOrderDetail(OrderDetailDto dto) {
        OrderDetail orderDetail = dto.toOrderDetail();
        Order order = orderDao.findByOrderId(dto.getOrderId()).get(0);
        order.setTotalAmount(order.getTotalAmount().add(dto.getTotalPrice()));

        orderDao.save(order);
        orderDetailDao.save(orderDetail);


        kafkaProducer.updateStockRequest(dto.getProductId(), dto.getQuantity());
        return EngineUtils.createSuccessReponse(200,
                orderDetail.createResponse(), Constant.ControllerRoute.createOrderDetail);
    }

    @Override
    public Map<Long, CompletableFuture<String>> getValidationFutures() {
        return validationFutures;
    }

    @Override
    public Map<String, Object> totalOrder(OrderDto dto) {
        logger.info("Attempt to count totalOrder");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("totalOrder", orderDao.countTotalOrdersByCustomer(dto.getCustomerId()));

            return EngineUtils.createSuccessReponse(200, map, Constant.ControllerRoute.totalOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.totalOrder);
        }
    }

    @Override
    public Map<String, Object> totalAmount(OrderDto dto) {
        logger.info("Attempt to count totalAmount");

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("totalAmount", orderDao.sumTotalAmountByCustomer(dto.getCustomerId()));

            return EngineUtils.createSuccessReponse(200, map, Constant.ControllerRoute.totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.totalAmount);
        }
    }

    @Override
    public Map<String, Object> listOrderByStatus(OrderDto dto) {
        logger.info("Attempt to remove Order Detail");

        try {
            if (!dto.getStatus().equals(Constant.STATUS.PENDING) &&
                    !dto.getStatus().equals(Constant.STATUS.DISPOSE) &&
                    !dto.getStatus().equals(Constant.STATUS.SUCCESS) )
                throw new Exception("Status unknow");

            List<Order> orders = orderDao.findOrdersByStatusAndLatestOrderDate(dto.getStatus());

            return EngineUtils.createSuccessReponse(200, EngineUtils.toListMap(orders, orderDetailDao), Constant.ControllerRoute.listOrderByStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.listOrderByStatus);
        }
    }

    @Override
    public Map<Long, CompletableFuture<String>> productsByStatus() {
        return null;
    }

    Order checkOrderAvailablity(Long orderId) throws Exception {
        List<Order> orders = orderDao.findByOrderId(orderId);

        if (orders.isEmpty())
            throw new Exception("Order not found");

        return orders.get(0);
    }


}
