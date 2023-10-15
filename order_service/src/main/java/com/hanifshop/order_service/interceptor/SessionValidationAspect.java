package com.hanifshop.order_service.interceptor;

import com.hanifshop.order_service.dto.BaseDto;
import com.hanifshop.order_service.repository.SessionDao;
import com.hanifshop.order_service.util.EngineUtils;
import com.hanifshop.order_service.util.PojoJsonMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * @author Hanif al kamal 16/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Aspect
@Component
public class SessionValidationAspect {

    @Autowired
    private SessionDao sessionDao;

    @Around("@annotation(ValidateSession) && args(baseDto, token, ..)")
    public ResponseEntity<?> validateSession(ProceedingJoinPoint joinPoint, BaseDto baseDto, String token) throws Throwable {
        System.out.println("Attempt to validate Session");

        try {
            if (!EngineUtils.isValidToken(sessionDao, baseDto.getCustomerId().toString(), token)) {
                throw new Exception("Invalid Token, Please Re-Login");
            }
            return (ResponseEntity<?>) joinPoint.proceed();
        } catch (Exception e) {
            e.printStackTrace();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            String requestPath = attributes.getRequest().getRequestURI();

            return new ResponseEntity<>(PojoJsonMapper.toJson(EngineUtils.createFailedReponse(500, e.getMessage(), requestPath))
                    , headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
