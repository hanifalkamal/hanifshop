package com.hanifshop.authentication_service.service;

import com.hanifshop.authentication_service.dto.AuthDto;
import com.hanifshop.authentication_service.dto.TokenAuthDto;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Service
public interface AuthService {
    Map<String, Object> login(AuthDto authDto);
    Map<String, Object> validateToken(TokenAuthDto tokenAuthDto);
}
