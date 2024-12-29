package org.kvn.NotificationService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.kvn.NotificationService.utils.Constants;

@Service
public class UserCreatedNotification {
    private static final Logger logger = LoggerFactory.getLogger(UserCreatedNotification.class);

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = {Constants.USER_CREATION_TOPIC}, groupId = "notification-service")
    public void sendNotification(String message) throws JsonProcessingException {
        logger.info("A new user is added. Wallet is created. Sending mail Notification");
        JSONObject jsonObject = objectMapper.readValue(message, JSONObject.class);
        String name = (String) jsonObject.get(Constants.USER_NAME);
        String email = (String) jsonObject.get(Constants.USER_EMAIL);

        // send mail
        simpleMailMessage.setFrom("ewallet@kvn.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("EWallet User Created");
        simpleMailMessage.setText("Welcome " + name + " to EWallet. User has been created. wallet wil be created in a while");
        mailSender.send(simpleMailMessage);
        logger.info("Sent mail Notification");
    }
}
