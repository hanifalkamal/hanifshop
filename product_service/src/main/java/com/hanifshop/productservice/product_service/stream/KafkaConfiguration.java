package com.hanifshop.productservice.product_service.stream;

import com.hanifshop.productservice.product_service.model.Product;
import com.hanifshop.productservice.product_service.service.ProductService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@EnableKafka
@Configuration
public class KafkaConfiguration {

    private final Logger logger =  LogManager.getLogger(KafkaConsumer.class);

    @Autowired
    ProductService productService;

    @Autowired
    KafkaProducer kafkaProducer;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        logger.info("ATTEMPT TO CONFIG KAFKA");
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "103.82.242.61:9092");
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "rdi-group");
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
//        props.put(
//                ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
//                "100");
//
//        props.put(
//                ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
//                "100");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setErrorHandler(new ContainerAwareErrorHandler() {
            @Override
            public void handle(Exception e, List<ConsumerRecord<?, ?>> list, Consumer<?, ?> consumer, MessageListenerContainer messageListenerContainer) {
                ConsumerRecord record = list.get(0);
                logger.info("exceptional data and topic {}-{}", record.value(), record.topic());
                messageListenerContainer.stop();
                logger.info("consumer has been stopped for listener id : {}", messageListenerContainer.getListenerId());
            }
        });
        return factory;
    }
}
