package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dto.UserRequestDTO;
import jakarta.persistence.Id;
import model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // TODO: create User Response DTO for returning
    @PostMapping("/addUser")
    public ResponseEntity<Users> addUser(@RequestBody @Validated UserRequestDTO userRequestDTO) throws JsonProcessingException {
        Users user = userService.addUpdate(userRequestDTO);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TODO: write updateUser API
}

