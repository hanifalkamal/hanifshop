package com.hanifshop.authentication_service.util;

public class Constant {

    public static final String baseUrl = "http://localhost:8080";
    public static final String getProduct  = "/product/all";

    public static class ControllerRoute{
        public static final String login  = "/auth/login";
        public static final String validateToken  = "/auth/validatetoken";
    }

    public static class STATUS{
        public static final String SUCCESS = "SUCCESS";
        public static final String DISPOSE = "DISPOSE";
        public static final String PENDING = "PENDING";
    }
}
