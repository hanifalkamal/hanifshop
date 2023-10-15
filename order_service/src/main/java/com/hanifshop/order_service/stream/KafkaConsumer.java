package com.hanifshop.order_service.stream;

import com.hanifshop.order_service.model.Order;
import com.hanifshop.order_service.model.OrderDetail;
import com.hanifshop.order_service.service.OrderService;
import com.hanifshop.order_service.util.PojoJsonMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
//@Configuration
public class KafkaConsumer implements MessageListener<String, String> {

    private final Logger logger =  LogManager.getLogger(KafkaConsumer.class);

    @Autowired
    private OrderService orderService;

//    @KafkaListener(id = "order-validation-consumer", topics = "validation-result-topic")
    public void listen(ConsumerRecord<String, String> record) {

        logger.info("### Message Receive From Topics : product-validation-topic");
        logger.info("   Message :   " + record.value());

        String validationResponse = record.value();
        Map<String, String> data = PojoJsonMapper.fromJson(validationResponse, Map.class);
        String errorMessage = data.containsValue("error") ? data.get("error") : "";
        Long productId = Long.parseLong(data.get("productId"));

        CompletableFuture<String> validationFuture = orderService.getValidationFutures().get(productId);
        if (validationFuture != null) {
            validationFuture.complete(validationResponse);
        }

    }

//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer() {
//        ContainerProperties containerProps = new ContainerProperties("validation-result-topic");
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "103.82.242.61:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-validation-consumer-group");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
//
//        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
//        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
//
//
//    }

//    @Bean
    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties("validation-result-topic");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "103.82.242.61:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-validation-consumer-group");
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
