package com.hanifshop.authentication_service.controller;

import com.hanifshop.authentication_service.dto.AuthDto;
import com.hanifshop.authentication_service.dto.TokenAuthDto;
import com.hanifshop.authentication_service.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public interface AuthController {

    @PostMapping(Constant.ControllerRoute.login)
    ResponseEntity<?> Login(
            AuthDto authDto
    );


    @PostMapping(Constant.ControllerRoute.validateToken)
    ResponseEntity<?> ValidateToken(
            TokenAuthDto tokenAuthDto
    );

}
