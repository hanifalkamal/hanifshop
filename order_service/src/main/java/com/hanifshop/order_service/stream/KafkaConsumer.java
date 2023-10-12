package com.hanifshop.order_service.stream;

import com.hanifshop.order_service.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public class KafkaConsumer {

    @Autowired
    OrderService orderService;

    @KafkaListener(id = "order-validation-consumer", topics = "validation-result-topic")
    public void listen(ConsumerRecord<String, String> record) {

        String validationResponse = record.value();
        String[] values = validationResponse.split(":");
        Long productId = Long.parseLong(values[1]);
        int requestedQty = Integer.parseInt(values[2]);
        Boolean isQtyValid = Boolean.parseBoolean(values[3]);

        CompletableFuture<Boolean> validationFuture = orderService.getValidationFutures().get(productId);
        if (validationFuture != null) {
            validationFuture.complete(isQtyValid);
        }

    }

    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties("validation-result-topic");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "your-bootstrap-servers");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-validation-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
    }
}
