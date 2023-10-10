package com.hanifshop.productservice.product_service.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EngineUtils {
    public static  Map<String, Object> createFailedReponse(Integer errorCode, String errorMessage, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", LocalDateTime.now());
        result.put("status", errorCode);
        result.put("error", errorMessage);
        result.put("path", path);
        return result;
    }

    public static  Map<String, Object> createSuccessReponse(Integer errorCode, String message, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", LocalDateTime.now());
        result.put("status", errorCode);
        result.put("message", message);
        result.put("path", path);
        return result;
    }

    public static  Map<String, Object> createSuccessReponse(Integer errorCode, Map<String, Object> data, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", LocalDateTime.now());
        result.put("status", errorCode);
        result.put("data", data);
        result.put("path", path);
        return result;
    }

    public static  Map<String, Object> createSuccessReponse(Integer errorCode, List<Map<String, Object>> data, String path){
        final Map<String, Object> result = new HashMap<>();
        result.put("timestamp", LocalDateTime.now());
        result.put("status", errorCode);
        result.put("data", data);
        result.put("path", path);
        return result;
    }
}
