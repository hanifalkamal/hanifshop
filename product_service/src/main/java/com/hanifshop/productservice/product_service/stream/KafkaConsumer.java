package com.hanifshop.productservice.product_service.stream;

import com.hanifshop.productservice.product_service.dto.ProductDto;
import com.hanifshop.productservice.product_service.model.Product;
import com.hanifshop.productservice.product_service.service.ProductService;
import com.hanifshop.productservice.product_service.util.PojoJsonMapper;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @KafkaListener(
            topicPartitions = @TopicPartition(topic = "rdi",
                    partitionOffsets = {
                            @PartitionOffset(partition = "0", initialOffset = "2147483647")}))
    public void listenToPartition(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {


        logger.info("### Message Receive From Topics : rdi");

        logger.info("   Message :   " + message);
        Map<String, String> data = PojoJsonMapper.fromJson(message, Map.class);
        logger.info(data.get("productId"));
        logger.info(data.get("orderQty"));
        Product product = productService.getProduct(Long.parseLong(data.get("productId")));
        ProductDto productDto = ProductDto.fromProduct(product);
        Integer orderQty = Integer.parseInt(data.get("orderQty"));
        productDto.setStockQuantity(productDto.getStockQuantity() - orderQty);

        logger.info("QTY = " + productDto.getStockQuantity());

        productService.UpdateProduct(productDto);


    }
}
