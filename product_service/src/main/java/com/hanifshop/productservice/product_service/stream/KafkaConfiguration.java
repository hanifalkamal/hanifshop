package com.hanifshop.productservice.product_service.stream;

import com.hanifshop.productservice.product_service.model.Product;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Hanif al kamal 13/10/2023
 * @contact hanif.alkamal@gmail.com
 */
//@Configuration
public class KafkaConfiguration {

//    @Bean
    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties("validation-result-topic");
        containerProps.setMessageListener(new KafkaConsumer());

        return new ConcurrentMessageListenerContainer<>(
                consumerFactory("product-validation-consumer"),
                containerProps
        );
    }

//    @Bean
    public ConcurrentMessageListenerContainer<String, String> messageListenerContainer2() {
        ContainerProperties containerProps = new ContainerProperties("product-qty-update-topic");
        containerProps.setMessageListener(new KafkaConsumer()); // Ganti YourMessageListener2 dengan implementasi yang sesuai.

        return new ConcurrentMessageListenerContainer<>(
                consumerFactory("product-update-consumer"),
                containerProps
        );
    }

//    @Bean
    public ConsumerFactory<String, String> consumerFactory(String groupId) {
        JsonDeserializer<String> deserializer = new JsonDeserializer<>(String.class);
        // Batasi paket yang dipercayai sebanyak mungkin.
        deserializer.addTrustedPackages("com.hanifshop.order_service"); // Gantilah dengan paket yang sesuai dengan aplikasi Anda.

        ErrorHandlingDeserializer<String> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(groupId),
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

//    @Bean
    public Map<String, Object> consumerConfig(String groupId) {
        Map<String, Object> props = new HashMap<>();
        String bootstrapServers = "103.82.242.61:9092";
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return props;
    }
}
