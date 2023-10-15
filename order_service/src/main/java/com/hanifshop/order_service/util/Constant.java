package com.hanifshop.order_service.util;

public class Constant {

    public static final String baseUrl = "http://localhost:8080";
    public static final String getProduct  = "/product/all";

    public static class ControllerRoute{
        public static final String createOrder  = "/order/create";
        public static final String createOrderDetail  = "/order/detail";
        public static final String listOrder  = "/order/list";
        public static final String listOrderDetail  = "/order/detail/list";
        public static final String updateOrder  = "/order/update";
        public static final String orderTotalPrice  = "/order/total";
        public static final String payOrder  = "/order/pay";
    }

    public static class STATUS{
        public static final String SUCCESS = "SUCCESS";
        public static final String DISPOSE = "DISPOSE";
        public static final String PENDING = "PENDING";
    }
}
