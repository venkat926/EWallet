package org.kvn.UserService.service;

import org.kvn.CommonUtils.CommonConstants;
import org.kvn.UserService.dto.SenderReceiverInfo;
import org.kvn.UserService.dto.UserTxnDTO;
import org.kvn.UserService.dto.ValidateWalletDTO;
import org.kvn.UserService.model.Users;
import org.kvn.UserService.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidateUserBeforeTxnService {

    public static final Logger logger = LoggerFactory.getLogger(ValidateUserBeforeTxnService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, SenderReceiverInfo> kafkaTemplate;

    @Autowired
    private UserRepo userRepo;

    public String validateUsersAndStartTxn(UserTxnDTO dto, Users user) {
        // sender is having a wallet and enough money in it
        // receiver is having a wallet or not
        // rest template
        ValidateWalletDTO senderDTO = restTemplate.exchange("http://localhost:8070/wallet/validateWallet?contact=" + user.getPhoneNo()+"&balance="+dto.getAmount(),
                HttpMethod.GET, null, ValidateWalletDTO.class).getBody();

        ValidateWalletDTO receiverDTO = restTemplate.exchange("http://localhost:8070/wallet/validateWallet?contact=" + dto.getReceiverContact()+"&balance=0",
                HttpMethod.GET, null, ValidateWalletDTO.class).getBody();

        if (senderDTO==null || receiverDTO==null) {
            logger.info("EWallet Server is down. Please try again!");
            return "EWallet Server is down. Please try again!";
        }
        if (!senderDTO.isValidWallet()) {
            logger.info("There is no wallet associated with the sender.");
            return "There is no ewallet associated with the sender.";
        }
        if(!senderDTO.isDoesWalletHasEnoughAmount()) {
            logger.info("Sender does not have enough amount in his wallet to made the transaction.");
            return "Sender does not have enough amount in his wallet to made the transaction.";
        }
        if (!receiverDTO.isValidWallet()) {
            logger.info("There is no wallet associated with the receiver.");
            return "There is no ewallet associated with the receiver.";
        }

        // send message to queue to start the transaction
        SenderReceiverInfo senderReceiverInfo =
                new SenderReceiverInfo(user.getPhoneNo(), dto.getReceiverContact(), dto.getAmount(), dto.getMessage(),
                        user.getEmail(), userRepo.getEmailByContact(dto.getReceiverContact()));
        logger.info("Publishing message to {} to start the Transaction", CommonConstants.TRANSACTION_CREATION_TOPIC);
        kafkaTemplate.send(CommonConstants.TRANSACTION_CREATION_TOPIC, senderReceiverInfo);
        logger.info("Published message to {} to start the Transaction", CommonConstants.TRANSACTION_CREATION_TOPIC);

        return "Transaction has been started. You will get notified once your Transaction is done";
    }
}
