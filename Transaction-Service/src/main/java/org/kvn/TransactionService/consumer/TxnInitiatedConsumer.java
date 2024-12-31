package org.kvn.TransactionService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.kvn.CommonUtils.CommonConstants;
import org.kvn.TransactionService.dto.SenderReceiverInfo;
import org.kvn.TransactionService.model.Txn;
import org.kvn.TransactionService.model.TxnStatus;
import org.kvn.TransactionService.repository.TxnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TxnInitiatedConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TxnInitiatedConsumer.class);

    @Autowired
    private TxnRepository txnRepo;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = {CommonConstants.TRANSACTION_CREATION_TOPIC}, groupId = "${txn.group.id}",
            containerFactory = "factory1")
    public void txnCreation(SenderReceiverInfo message, Acknowledgment acknowledgment) throws JsonProcessingException {
        // save Txn in DB
        logger.info("Received a message in {}", CommonConstants.TRANSACTION_CREATION_TOPIC);

        logger.info("Saving Transaction details in DB");
        Txn txn = new Txn();
        txn.setTxnId(UUID.randomUUID().toString());
        txn.setAmount(message.getAmount());
        txn.setSenderContact(message.getSenderContact());
        txn.setReceiverContact(message.getReceiverContact());
        txn.setTxnStatus(TxnStatus.INITIATED);
        txn.setMessage(message.getMessage());
        txnRepo.save(txn);
        logger.info("Transaction details are saved in DB successfully with {}", txn.getTxnId());

        // Publish message to the Kafka_Queue
        // ( for deducting amount from Senders wallet and to add amount to Receivers wallet )
        logger.info("publishing message to {}," +
                " for deducting amount from Senders wallet and to add amount to Receivers wallet ",
                CommonConstants.TRANSACTION_UPDATION_TOPIC);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txnId", txn.getTxnId());
        jsonObject.put("senderContact", txn.getSenderContact());
        jsonObject.put("receiverContact", txn.getReceiverContact());
        jsonObject.put("amount", txn.getAmount());
        jsonObject.put("txnStatus", txn.getTxnStatus());
        jsonObject.put("message", txn.getMessage());
        kafkaTemplate.send(CommonConstants.TRANSACTION_UPDATION_TOPIC, objectMapper.writeValueAsString(jsonObject));
        logger.info("message published to {}," +
                " for deducting amount from Senders wallet and to add amount to Receivers wallet ",
                CommonConstants.TRANSACTION_UPDATION_TOPIC);
        // send acknowledgment back to the Kafka_Queue
        acknowledgment.acknowledge();

    }


}
