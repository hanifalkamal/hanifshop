package com.hanifshop.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Session")
public class Session {

    @Id
    private String redisKey;
    private String token;

}
