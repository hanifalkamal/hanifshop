package com.hanifshop.authentication_service.service.impl;

import com.hanifshop.authentication_service.dto.AuthDto;
import com.hanifshop.authentication_service.dto.TokenAuthDto;
import com.hanifshop.authentication_service.model.AuthenticationToken;
import com.hanifshop.authentication_service.model.Session;
import com.hanifshop.authentication_service.model.User;
import com.hanifshop.authentication_service.repository.AuthenticationTokenDao;
import com.hanifshop.authentication_service.repository.SessionDao;
import com.hanifshop.authentication_service.repository.UserDao;
import com.hanifshop.authentication_service.service.AuthService;
import com.hanifshop.authentication_service.util.Constant;
import com.hanifshop.authentication_service.util.EngineUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationTokenDao tokenDao;

    @Autowired
    private SessionDao sessionDao;

    @Override
    public Map<String, Object> login(AuthDto authDto) {

        logger.info("Attempt To Login");

        try {

            User user = userDao.findByUsername(authDto.getUsername());
            if (user == null)
                throw new Exception("Username not found");

            if (!authDto.getPassword().equals(user.getPassword()))
                throw new Exception("Invalid password");

            String token = EngineUtils.generateAuthToken(user.getUserId());

            String redisKey = "auth_token:" + user.getUserId();

            Session session = new Session();
            session.setToken(token);
            session.setRedisKey(redisKey);

            sessionDao.save(session);

            AuthenticationToken authenticationToken = new AuthenticationToken();
            authenticationToken.setUser(user);
            authenticationToken.setToken(token);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, 15);
            Date newDate = calendar.getTime();
            authenticationToken.setExpirationTime(newDate);

            tokenDao.save(authenticationToken);

            return EngineUtils.createSuccessReponse(200, token, Constant.ControllerRoute.login);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.login);
        }
    }

    @Override
    public Map<String, Object> validateToken(TokenAuthDto tokenAuthDto) {

        logger.info("Attempt To Validate Token");

        try {
            String redisKey = "auth_token:" + tokenAuthDto.getUserId();
            String storedToken = sessionDao.findSessionByRedisKey(redisKey);

            logger.info("Token : " + tokenAuthDto.getToken());
            logger.info("Stored Token : " + storedToken);

            if (!tokenAuthDto.getToken().equals(storedToken))
                throw new Exception("Invalid Token");

            return EngineUtils.createSuccessReponse(200, "Valid Token", Constant.ControllerRoute.validateToken);
        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.validateToken);
        }
    }
}
