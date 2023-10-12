package com.hanifshop.productservice.product_service.stream;

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
    private final KafkaTemplate<String, Product> kafkaTemplateProduct;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Product> kafkaTemplateProduct, KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplateProduct = kafkaTemplateProduct;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaMessage(String topics, String message) {
        kafkaTemplate.send(topics, message);
    }

    public void sendKafkaMessage(Product message) {
        kafkaTemplateProduct.send("com-product-all", message);
    }

    public void sendKafkaMessages(List<Product> messages) {
        for (Product message : messages) {
            kafkaTemplateProduct.send("com-product-all", message);
        }
    }
}
