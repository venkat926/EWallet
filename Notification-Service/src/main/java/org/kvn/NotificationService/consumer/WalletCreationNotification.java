package org.kvn.NotificationService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.kvn.CommonUtils.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class WalletCreationNotification {

    private static final Logger logger = LoggerFactory.getLogger(WalletCreationNotification.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = {CommonConstants.WALLET_CREATION_TOPIC}, groupId = "notification-service")
    public void sendWalletCreatedNotification(String message) throws JsonProcessingException {
        logger.info("A new wallet is created. Sending mail Notification");
        JSONObject jsonObject = objectMapper.readValue(message, JSONObject.class);
        Integer userId = (Integer) jsonObject.get(CommonConstants.USER_ID);
        String email = (String) jsonObject.get(CommonConstants.USER_EMAIL);
        String user_name = (String) jsonObject.get(CommonConstants.USER_NAME);

        // send mail
        simpleMailMessage.setFrom("eWallet@kvn.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("EWallet created successfully for new user");
        simpleMailMessage.setText("Hello " + user_name + " !  EWallet is successfully created for you. Please utilize our services");
        mailSender.send(simpleMailMessage);
        logger.info("Sent mail Notification on wallet creation");

    }
}
