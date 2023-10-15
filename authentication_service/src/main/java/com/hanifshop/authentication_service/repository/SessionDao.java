package com.hanifshop.authentication_service.repository;

import com.hanifshop.authentication_service.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Repository
public class SessionDao {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    public Session save(Session session){
        template.opsForValue().set(session.getRedisKey(), session.getToken(), Duration.ofMinutes(1));
        return session;
    }


    public String findSessionByRedisKey(String redisKey){
        return String.valueOf(template.opsForValue().get(redisKey));
    }


    public List<Session> findAll(Session session){
        return template.opsForHash().values(session.getRedisKey());
    }

}
