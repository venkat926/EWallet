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
public class TxnCompletedNotification {

    private static final Logger logger = LoggerFactory.getLogger(TxnCompletedNotification.class);

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = {CommonConstants.TRANSACTION_COMPLETED_TOPIC}, groupId = "notification-service")
    public void transactionCompletedNotification(String message) throws JsonProcessingException {
        JSONObject jsonObject = objectMapper.readValue(message, JSONObject.class);
        logger.info("{}", jsonObject);
        String txnId = (String) jsonObject.get("txnId");
        String status = (String) jsonObject.get("status");
        String msg = (String) jsonObject.get("message");
        Double remainingBalance = (Double) jsonObject.get("remainingBalance");
        String senderEmail = (String) jsonObject.get("senderEmail");
        String receiverEmail = (String) jsonObject.get("receiverEmail");
        String senderContact = (String) jsonObject.get("senderContact");
        Double amount = (Double) jsonObject.get("amount");

        if (status.equals("success")) {
            // send mail to sender
            simpleMailMessage.setFrom("ewallet@kvn.com");
            simpleMailMessage.setTo(senderEmail);
            simpleMailMessage.setSubject("Transaction Successful: Amount Debited");
            simpleMailMessage.setText("HI... The transaction with the transaction id " + txnId + " is successful. " +
                    "Amount received to the receiver. Your current balance : " + remainingBalance + " Rupees");
            mailSender.send(simpleMailMessage);

            // send mail to receiver
            simpleMailMessage.setFrom("ewallet@kvn.com");
            simpleMailMessage.setTo(receiverEmail);
            simpleMailMessage.setSubject("Transaction Successful: Amount Credited");
            simpleMailMessage.setText("HI... You received " + amount + "Rupees from contact " + senderContact  +
                    " Transaction id is " + txnId);
            mailSender.send(simpleMailMessage);
        } else if (status.equals("pending")) {
            // send mail to sender
            simpleMailMessage.setFrom("ewallet@kvn.com");
            simpleMailMessage.setTo(senderEmail);
            simpleMailMessage.setSubject("Transaction is Pending:");
            simpleMailMessage.setText("HI... The transaction with the transaction id " + txnId + " is pending. " +
                    "Amount is not debited from your wallet");
            mailSender.send(simpleMailMessage);

        } else {
            // send mail to sender
            simpleMailMessage.setFrom("ewallet@kvn.com");
            simpleMailMessage.setTo(senderEmail);
            simpleMailMessage.setSubject("Transaction is Failed:");
            simpleMailMessage.setText("HI... The transaction with the transaction id " + txnId + " is Failed. " +
                    "Amount will be refunded to your wallet, if debited");
            mailSender.send(simpleMailMessage);
        }
    }
}
