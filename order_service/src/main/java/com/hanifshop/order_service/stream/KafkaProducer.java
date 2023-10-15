package com.hanifshop.order_service.stream;

import com.hanifshop.order_service.util.PojoJsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Configuration
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Logger logger =  LogManager.getLogger(KafkaProducer.class);

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendValidationRequest(String productId, int requestedQty) {
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);

        String validationRequest = PojoJsonMapper.toJson(map);

        logger.info("SEND KAFKA MESSAGE TO TOPICS rdi : " + validationRequest);

        kafkaTemplate.send("rdi", validationRequest);
    }

    public void updateStockRequest(Long productId, Integer requestedQty) {
        Map<String, String> map = new HashMap<>();
        map.put("productId", String.valueOf(productId));
        map.put("orderQty", String.valueOf(requestedQty));

        String request = PojoJsonMapper.toJson(map);
        kafkaTemplate.send("rdi", request);
    }
}
