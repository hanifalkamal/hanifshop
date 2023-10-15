package com.hanifshop.order_service.service.impl;

import com.hanifshop.order_service.dto.OrderDetailDto;
import com.hanifshop.order_service.dto.OrderDto;
import com.hanifshop.order_service.model.Order;
import com.hanifshop.order_service.model.OrderDetail;
import com.hanifshop.order_service.repository.OrderDao;
import com.hanifshop.order_service.repository.OrderDetailDao;
import com.hanifshop.order_service.repository.SessionDao;
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
import java.util.stream.Collectors;

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

            if (order.getOrderId() == null)
                throw new Exception("Order Id not found");

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



            List<Map<String, Object>> orderlist = orders.stream()
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

            return EngineUtils.createSuccessReponse(200,
                    orderlist, Constant.ControllerRoute.listOrder);

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

            checkOrderAvailablity(dto.getOrderId());

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
    public Map<Long, CompletableFuture<String>> totalOrder() {
        return null;
    }

    @Override
    public Map<Long, CompletableFuture<String>> totalAmount() {
        return null;
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

    public List<OrderDetailDto> orderDetailforList(List<OrderDetail> orders) {
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
