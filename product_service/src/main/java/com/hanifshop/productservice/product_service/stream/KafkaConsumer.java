package com.hanifshop.productservice.product_service.stream;

import com.hanifshop.productservice.product_service.dto.ProductDto;
import com.hanifshop.productservice.product_service.model.Product;
import com.hanifshop.productservice.product_service.service.ProductService;
import com.hanifshop.productservice.product_service.util.PojoJsonMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.GenericMessageListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Configuration
public class KafkaConsumer implements MessageListener<String, String> {

    private final Logger logger =  LogManager.getLogger(KafkaConsumer.class);

    @Autowired
    ProductService productService;

    @Autowired
    KafkaProducer kafkaProducer;

    @KafkaListener(id = "product-validation-consumer-group", topics = "product-validation-topic")
    public void listenGetProduct(ConsumerRecord<String, String> record) {

        logger.info("### Message Receive From Topics : product-validation-topic");
        logger.info("   Message :   " + record.value());

        Map<String, String> data = PojoJsonMapper.fromJson(record.value(), Map.class);

        Product product = productService.getProduct(Long.parseLong(data.get("productId")));

        if (product == null) {
            Map<String, String> failedResult = new HashMap<>();
            failedResult.put("error", "Product not found");
            failedResult.put("productId", data.get("productId"));
            kafkaProducer.sendKafkaMessage("validation-result-topic", PojoJsonMapper.toJson(failedResult));
        } else {
            kafkaProducer.sendKafkaMessage("validation-result-topic", PojoJsonMapper.toJson(product));
        }
    }

    @KafkaListener(id = "product-update-consumer-group", topics = "product-qty-update-topic")
    public void listenUpdateProduct(ConsumerRecord<String, String> record) {
        Map<String, String> data = PojoJsonMapper.fromJson(record.value(), Map.class);

        logger.info("### Message Receive From Topics : product-qty-update-topic");
        logger.info("   Message :   " + record.value());

        Product product = productService.getProduct(Long.parseLong(data.get("productId")));

        ProductDto productDto = ProductDto.fromProduct(product);
        Integer orderQty = Integer.parseInt(data.get("orderQty"));
        productDto.setStockQuantity(productDto.getStockQuantity() - orderQty);

        productService.UpdateProduct(productDto);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties("product-qty-update-topic");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "103.82.242.61:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-update-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());

        // Konfigurasi trusted packages menggunakan spring.kafka.consumer.properties
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        containerProps.setMessageListener(this);

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer2() {
        ContainerProperties containerProps = new ContainerProperties("product-validation-topic");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "103.82.242.61:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-validation-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());

        // Konfigurasi trusted packages menggunakan spring.kafka.consumer.properties
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        containerProps.setMessageListener(this);

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();
        System.out.println("Received message - Key: " + key + ", Value: " + value);

    }
}
