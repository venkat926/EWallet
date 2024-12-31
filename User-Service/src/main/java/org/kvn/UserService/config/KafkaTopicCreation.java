package org.kvn.UserService.config;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.kvn.CommonUtils.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaTopicCreation {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTopicCreation.class);

    private AdminClient adminClient;

    public KafkaTopicCreation(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @PostConstruct
    public void createTopic() {
        createTopic(CommonConstants.TRANSACTION_CREATION_TOPIC, 1, 1);
    }

    /**
     * To create a new kafka_topic with configuration (with n number of partitions and n number of replication factor)
     */
    public void createTopic(String topicName, int partitions, int rFactor) {
        NewTopic topic = new NewTopic(topicName, partitions, (short) rFactor);
        try {
            adminClient.createTopics(Collections.singleton(topic)).all().get();
            logger.info("Transaction_creation_topic topic has been created");
        } catch (TopicExistsException | InterruptedException | ExecutionException e) {
            logger.info("Transaction_creation_topic Topic already exists. Not creating it again");
        }
    }
}
