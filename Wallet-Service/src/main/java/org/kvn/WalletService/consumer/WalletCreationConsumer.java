package org.kvn.WalletService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.kvn.CommonUtils.CommonConstants;
import org.kvn.WalletService.model.Wallet;
import org.kvn.WalletService.repository.WalletRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class WalletCreationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(WalletCreationConsumer.class);

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Value("${user.creation.time.balance}")
    private Double balance;

    @KafkaListener(topics = {CommonConstants.USER_CREATION_TOPIC},
            groupId = "${wallet.kafka.groupId}",
            containerFactory = "factory1")
    public void walletCreation(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {

            JSONObject jsonObject = objectMapper.readValue(message, JSONObject.class);
            Integer userId = (Integer) jsonObject.get(CommonConstants.USER_ID);
            String contact = (String) jsonObject.get(CommonConstants.USER_CONTACT);
            logger.info("creating wallet for a new user with userID {}", userId);

            Wallet wallet = new Wallet(userId, contact, balance);
            walletRepo.save(wallet);
            logger.info("wallet created for the new user with userID {} and wallet it is {}", userId, wallet.getId());

            // publish message to kafka queue
            logger.info("Sending message to the Kafka Queue: {} ", CommonConstants.WALLET_CREATION_TOPIC);
            JSONObject object = new JSONObject();
            object.put(CommonConstants.USER_ID, userId);
            object.put(CommonConstants.USER_EMAIL, jsonObject.get(CommonConstants.USER_EMAIL));
            object.put(CommonConstants.USER_NAME, jsonObject.get(CommonConstants.USER_NAME));
            kafkaTemplate.send(CommonConstants.WALLET_CREATION_TOPIC, objectMapper.writeValueAsString(object));
            logger.info("Sent message to the Kafka Queue: {} ", CommonConstants.WALLET_CREATION_TOPIC);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        acknowledgment.acknowledge();
    }
}
