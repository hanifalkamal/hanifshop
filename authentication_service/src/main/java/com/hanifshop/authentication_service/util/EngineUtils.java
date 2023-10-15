package com.hanifshop.authentication_service.util;


import com.hanifshop.authentication_service.model.Session;
import com.hanifshop.authentication_service.repository.SessionDao;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

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

    public static String generateAuthToken(Long userId) {
        return UUID.randomUUID().toString();
    }




}
