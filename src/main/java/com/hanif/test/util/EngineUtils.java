package com.hanif.test.util;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
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
}
