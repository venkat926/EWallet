package org.kvn.UserService.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class SenderReceiverInfo {

    private String senderContact;
    private String receiverContact;
    private Double amount;
    private String message;
    private String senderEmail;
    private String receiverEmail;

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

    public SenderReceiverInfo(String senderContact, String receiverContact, Double amount, String message,
                              String senderEmail, String receiverEmail) {
        this.senderContact = senderContact;
        this.receiverContact = receiverContact;
        this.amount = amount;
        this.message = message;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
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

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}
