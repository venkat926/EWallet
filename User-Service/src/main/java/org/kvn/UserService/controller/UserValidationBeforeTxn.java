package org.kvn.UserService.controller;

import org.kvn.UserService.dto.UserTxnDTO;
import org.kvn.UserService.model.Users;
import org.kvn.UserService.service.ValidateUserBeforeTxnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserValidationBeforeTxn {

    public static final Logger logger = LoggerFactory.getLogger(UserValidationBeforeTxn.class);

    @Autowired
    private ValidateUserBeforeTxnService validateUserBeforeTxnService;

    @PostMapping("/startTxn")
    public String validateUserBeforeTxn(@RequestBody UserTxnDTO dto, @AuthenticationPrincipal Users user) {
        return validateUserBeforeTxnService.validateUsersAndStartTxn(dto, user);
    }
}
