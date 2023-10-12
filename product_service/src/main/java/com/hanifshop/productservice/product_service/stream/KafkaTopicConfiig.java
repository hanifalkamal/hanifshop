package com.hanifshop.productservice.product_service.stream;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Hanif al kamal 12/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Configuration
public class KafkaTopicConfiig {

    @Bean
    public NewTopic productTopic(){
        return TopicBuilder.name("com-product-all").build();
    }

}
