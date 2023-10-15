package com.hanifshop.authentication_service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Getter
@Setter
public class TokenAuthDto {
    private String userId;
    private String token;
}
