package org.kvn.UserService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.kvn.UserService.dto.UserRequestDTO;
import org.kvn.UserService.dto.UserResponseDTO;
import org.kvn.UserService.exception.UserAlreadyExistsException;
import org.kvn.UserService.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.kvn.UserService.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Validated UserRequestDTO userRequestDTO)
            throws JsonProcessingException, UserAlreadyExistsException {
        logger.info("addUser API is called");
        Users user = userService.addUpdate(userRequestDTO);
        if (user != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO(
                    user.getName(),
                    user.getPhoneNo(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getUserIdentifier(),
                    user.getUserIdentifierValue(),
                    user.getCreatedOn(),
                    user.getUpdatedOn()
            );
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TODO: write updateUser API

    // TODO: Remove these two API: These are used just for testing
    @GetMapping("/test")
    public String get() {
        System.out.println("Testing API");
        return "Just Testing";
    }

    @GetMapping("/test2")
    public String test2() {
        System.out.println("Test2");
        return "Testing: This is for Authenticated user";
    }
}

