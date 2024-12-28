package dto;

import enums.UserIdentifier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import model.Users;
import org.springframework.boot.autoconfigure.security.SecurityProperties;


public class UserRequestDTO {

    private String name;

    private String contact;

    @NotBlank(message = "email should not be blank")
    private String email;

    private String address;

    private String dob;

    @NotNull(message = "userIdentifier is required")
    private UserIdentifier userIdentifier;

    @NotBlank(message = "userIdentifier is required")
    private String userIdentifierValue;

    @NotBlank(message = "password should not be blank")
    private String password;

    public Users toUser() {

//        return Users.builder()
//                .name(name)
//                .phoneNo(contact)
//                .email(email)
//                .address(address)
//                .userIdentifier(userIdentifier)
//                .userIdentifierValue(userIdentifierValue)
//                .enabled(true)
//                .accountNonExpired(true)
//                .accountNonLocked(true)
//                .credentialsNonExpired(true)
//                .build();
        Users user = new Users();
        user.setName(name);
        user.setPhoneNo(contact);
        user.setEmail(email);
        user.setAddress(address);
        user.setUserIdentifier(userIdentifier);
        user.setUserIdentifierValue(userIdentifierValue);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        return user;
    }

    // Getters and Setters

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

    public @NotBlank(message = "email should not be blank") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "email should not be blank") String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public @NotBlank(message = "userIdentifier is required") String getUserIdentifierValue() {
        return userIdentifierValue;
    }

    public void setUserIdentifierValue(@NotBlank(message = "userIdentifier is required") String userIdentifierValue) {
        this.userIdentifierValue = userIdentifierValue;
    }

    public @NotBlank(message = "password should not be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "password should not be blank") String password) {
        this.password = password;
    }

    public @NotNull(message = "userIdentifier is required") UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(@NotNull(message = "userIdentifier is required") UserIdentifier userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
}
