package com.hanifshop.productservice.product_service.stream;

import com.hanifshop.productservice.product_service.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hanif al kamal 12/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Component
public class KafkaProducer {
    private final Logger logger =  LogManager.getLogger(KafkaProducer.class);

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
