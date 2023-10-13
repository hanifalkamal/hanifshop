package com.hanifshop.order_service.stream;

import com.hanifshop.order_service.util.PojoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendValidationRequest(String productId, int requestedQty) {
        Map<String, String> map = new HashMap<>();
        map.put("prducutId", productId);

        String validationRequest = PojoJsonMapper.toJson(map);
        kafkaTemplate.send("product-validation-topic", validationRequest);
    }
}
