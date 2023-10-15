package com.hanifshop.authentication_service.controller.Impl;

import com.hanifshop.authentication_service.controller.AuthController;
import com.hanifshop.authentication_service.dto.AuthDto;
import com.hanifshop.authentication_service.dto.TokenAuthDto;
import com.hanifshop.authentication_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Controller
public class AuthControllerImpl implements AuthController {

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<?> Login(AuthDto authDto) {
        Map<String, Object> mapping = authService.login(authDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> ValidateToken(TokenAuthDto tokenAuthDto) {
        Map<String, Object> mapping = authService.validateToken(tokenAuthDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }
}
