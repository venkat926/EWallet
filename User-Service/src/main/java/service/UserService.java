package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserRequestDTO;
import model.Users;
import org.apache.tomcat.util.bcel.Const;
import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepo;
import utilities.Constants;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("user.authority")
    private String userAuthority;

    @Value("admin.authority")
    private String adminAuthority;

    public Users addUpdate(UserRequestDTO userRequestDTO) throws JsonProcessingException {
        Users user = userRequestDTO.toUser();
        user.setAuthorities(userAuthority);
        user.setPassword(encoder.encode(userRequestDTO.getPassword()));
        user = userRepo.save(user);

        // Kafka Queue: push message to the Queue
        if (user != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.USER_CONTACT, user.getPhoneNo());
            jsonObject.put(Constants.USER_EMAIL, user.getEmail());
            jsonObject.put(Constants.USER_NAME, user.getName());
            jsonObject.put(Constants.USER_IDENTIFIER, user.getUserIdentifier());
            jsonObject.put(Constants.USER_IDENTIFIER_VALUE, user.getUserIdentifierValue());
            jsonObject.put(Constants.USER_ID, user.getId());

            kafkaTemplate.send(Constants.USER_CREATION_TOPIC, objectMapper.writeValueAsString(jsonObject));
        }

        // return
        return user;

    }
}
