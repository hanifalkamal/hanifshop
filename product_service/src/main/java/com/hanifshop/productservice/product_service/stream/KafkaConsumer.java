package com.hanifshop.productservice.product_service.stream;

import com.hanifshop.productservice.product_service.model.Product;
import com.hanifshop.productservice.product_service.service.ProductService;
import com.hanifshop.productservice.product_service.util.PojoJsonMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Component
public class KafkaConsumer {

    private final Logger logger =  LogManager.getLogger(KafkaConsumer.class);

    @Autowired
    ProductService productService;

    @Autowired
    KafkaProducer kafkaProducer;

    @KafkaListener(id = "product-validation-consumer", topics = "product-validation-topic")
    public void listen(ConsumerRecord<String, String> record) {

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


    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties("product-validation-topic");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-validation-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }

}
