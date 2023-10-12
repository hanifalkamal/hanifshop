package com.hanifshop.order_service.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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
        String validationRequest = "ProductValidation:" + productId + ":" + requestedQty;
        kafkaTemplate.send("product-validation-topic", validationRequest);
    }
}
