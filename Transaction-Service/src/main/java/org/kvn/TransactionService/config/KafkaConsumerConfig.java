package org.kvn.TransactionService.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.kvn.TransactionService.dto.SenderReceiverInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${bootstrap.server}")
    private String bootstrapAddress;

    @Value("${txn.group.id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, SenderReceiverInfo> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SenderReceiverInfo.class.getName());
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean(name = "factory1")
    public ConcurrentKafkaListenerContainerFactory<String, SenderReceiverInfo> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SenderReceiverInfo> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

}
