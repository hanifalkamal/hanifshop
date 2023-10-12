package com.hanifshop.productservice.product_service.grpc;

import com.hanifshop.productservice.product_service.model.Product;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hanif al kamal 12/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Product> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Product> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaMessage(Product message) {
        kafkaTemplate.send("com-product-getproduct", message);
    }

    public void sendKafkaMessages(List<Product> messages) {
        for (Product message : messages) {
            kafkaTemplate.send("com-product-getproduct", message);
        }
    }
}
