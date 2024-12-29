package org.kvn.UserService.dto;

import org.kvn.UserService.enums.UserIdentifier;

import java.util.Date;

public class UserResponseDTO {
    private String name;
    private String contact;
    private String email;
    private String address;
    private UserIdentifier userIdentifier;
    private String userIdentifierValue;
    private Date createdOn;
    private Date updatedOn;

    public UserResponseDTO(String name, String contact, String email,
                           String address, UserIdentifier userIdentifier,
                           String userIdentifierValue, Date createdOn,
                           Date updatedOn) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.userIdentifier = userIdentifier;
        this.userIdentifierValue = userIdentifierValue;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(UserIdentifier userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getUserIdentifierValue() {
        return userIdentifierValue;
    }

    public void setUserIdentifierValue(String userIdentifierValue) {
        this.userIdentifierValue = userIdentifierValue;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
