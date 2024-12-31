package org.kvn.TransactionService.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public class SenderReceiverInfo {
    private String senderContact;
    private String receiverContact;
    private Double amount;
    private String message;

    // Constructors
    public SenderReceiverInfo() {
    }
    public SenderReceiverInfo(String senderContact, String receiverContact, Double amount) {
        this.senderContact = senderContact;
        this.receiverContact = receiverContact;
        this.amount = amount;
    }
    public SenderReceiverInfo(String senderContact, String receiverContact, Double amount, String message) {
        this.senderContact = senderContact;
        this.receiverContact = receiverContact;
        this.amount = amount;
        this.message = message;
    }

    // Getters and Setters
    public String getSenderContact() {
        return senderContact;
    }

    public void setSenderContact(String senderContact) {
        this.senderContact = senderContact;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
