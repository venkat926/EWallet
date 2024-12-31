package org.kvn.WalletService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.kvn.CommonUtils.CommonConstants;
import org.kvn.WalletService.repository.WalletRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TxnUpdationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TxnUpdationConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = {CommonConstants.TRANSACTION_UPDATION_TOPIC}, groupId = "${wallet.kafka.groupId}", containerFactory = "factory1")
    @Transactional
    public void txnUpdation(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        logger.info("Received a message in {}, to update the wallet balance of Sender and receiver", CommonConstants.TRANSACTION_UPDATION_TOPIC);
        JSONObject jsonObject = objectMapper.readValue(message, JSONObject.class);

        String senderContact = (String) jsonObject.get("senderContact");
        String receiverContact = (String) jsonObject.get("receiverContact");
        Double amount = (Double) jsonObject.get("amount");
        String txnId = (String) jsonObject.get("txnId");

        logger.info("updating the wallet balance of sender and receiver");
        logger.info("senderContact-{}, receiverContact-{}", senderContact, receiverContact);
        walletRepo.updateWallet(senderContact, -amount);
        walletRepo.updateWallet(receiverContact, amount);

        // publish message to the kafka_queue, to update the txn status and to send the mail notification
        logger.info("publishing message to {}, to update the txn status", CommonConstants.TRANSACTION_COMPLETED_TOPIC);
        JSONObject object = new JSONObject();
        object.put("txnId", txnId);
        object.put("status", "success");
        object.put("message", "wallet updated");
        object.put("remainingBalance", walletRepo.getBalance(senderContact));
        kafkaTemplate.send(CommonConstants.TRANSACTION_COMPLETED_TOPIC, objectMapper.writeValueAsString(object));
        logger.info("published message to {}, to update the txn status", CommonConstants.TRANSACTION_COMPLETED_TOPIC);

        // Acknowledgment
        acknowledgment.acknowledge();

    }

}
