package com.hanifshop.authentication_service.interceptor;

import com.hanifshop.authentication_service.util.PojoJsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Hanif al kamal 7/30/23
 * @contact hanif.alkamal@gmail.com
 */

@Component
public class BaseInterceptor implements HandlerInterceptor {

    private final Logger logger =  LogManager.getLogger(BaseInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> mapping = new HashMap<>();
        Enumeration<?> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            mapping.put(paramName, request.getParameter(paramName));
        }
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);

        logger.info("Request["+requestId+"] :\n" + request.getRequestURL() + "; " + PojoJsonMapper.toJson(mapping));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
