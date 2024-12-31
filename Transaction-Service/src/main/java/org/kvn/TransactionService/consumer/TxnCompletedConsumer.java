package org.kvn.TransactionService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.kvn.CommonUtils.CommonConstants;
import org.kvn.TransactionService.dto.SenderReceiverInfo;
import org.kvn.TransactionService.model.TxnStatus;
import org.kvn.TransactionService.repository.TxnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class TxnCompletedConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TxnCompletedConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TxnRepository txnRepo;

    @KafkaListener(topics = {CommonConstants.TRANSACTION_COMPLETED_TOPIC}, groupId = "${txn.group.id}",
            containerFactory = "factory2")
    public void txnCreation(String message, Acknowledgment acknowledgment) throws JsonProcessingException {

        logger.info("received a message in {}", CommonConstants.TRANSACTION_COMPLETED_TOPIC);

        // read the message and update txn status in db
        JSONObject jsonObject = objectMapper.readValue(message, JSONObject.class);
        String txnId = (String) jsonObject.get("txnId");
        String status = (String) jsonObject.get("status");
        String msg = (String) jsonObject.get("message");
        Double remainingBalance = (Double) jsonObject.get("remainingBalance");

        logger.info("Updating Transaction status in DB. status is {}", status);
        if (status.equals("success")) {
            txnRepo.updateTxnStatus(TxnStatus.SUCCESS, txnId);
        } else {
            txnRepo.updateTxnStatus(TxnStatus.FAILURE, txnId);
        }
        logger.info("Updated Transaction status in DB. status is {}", status);

        // Acknowledgment
        acknowledgment.acknowledge();
    }
}
