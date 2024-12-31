package org.kvn.UserService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kvn.CommonUtils.CommonConstants;
import org.kvn.UserService.dto.UserRequestDTO;
import org.kvn.UserService.dto.ValidateWalletDTO;
import org.kvn.UserService.enums.UserType;
import org.kvn.UserService.exception.UserAlreadyExistsException;
import org.kvn.UserService.model.Users;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.kvn.UserService.repository.UserRepo;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.authority}")
    private String userAuthority;

    @Value("${admin.authority}")
    private String adminAuthority;

    public Users addUpdate(UserRequestDTO userRequestDTO) throws JsonProcessingException, UserAlreadyExistsException {
        Users user = userRequestDTO.toUser();
        user.setAuthorities(userAuthority);
        user.setPassword(encoder.encode(userRequestDTO.getPassword()));
        user.setUserType(UserType.USER);
        // validate the user by email, phone number and UserIdentifierValue
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use. Please use a new Email.");
        }
        if (user.getPhoneNo()!=null && !user.getPhoneNo().isEmpty() && userRepo.existsByPhoneNo(user.getPhoneNo())) {
            throw new UserAlreadyExistsException("Phone Number already in use. Please use a new phone number.");
        }
        if (userRepo.existsByUserIdentifierValue(user.getUserIdentifierValue())) {
            throw new UserAlreadyExistsException("UserIdentifierValue already in use. Please use a new UserIdentifierValue.");
        }
        user = userRepo.save(user);

        // Kafka Queue: push message to the Queue
        if (user != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CommonConstants.USER_CONTACT, user.getPhoneNo());
            jsonObject.put(CommonConstants.USER_EMAIL, user.getEmail());
            jsonObject.put(CommonConstants.USER_NAME, user.getName());
            jsonObject.put(CommonConstants.USER_IDENTIFIER, user.getUserIdentifier());
            jsonObject.put(CommonConstants.USER_IDENTIFIER_VALUE, user.getUserIdentifierValue());
            jsonObject.put(CommonConstants.USER_ID, user.getId());

            kafkaTemplate.send(CommonConstants.USER_CREATION_TOPIC, objectMapper.writeValueAsString(jsonObject));
        }

        // return
        return user;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("user Not Found");
        }
        return user;
    }

    public String addMoneyToUserWallet(Double amount, String phoneNo) {
        ValidateWalletDTO dto = restTemplate.exchange("http://localhost:8070/wallet/validateWallet?contact="+phoneNo+"&balance=0",
                HttpMethod.GET, null, ValidateWalletDTO.class).getBody();
        if (dto == null) return "Internal Server Error. Please try again";
        if (!dto.isValidWallet()) {
            return "Wallet is not associated with you. Please create wallet first";
        }
        Double finalAmount = restTemplate.exchange("http://localhost:8070/wallet/addMoney?contact="+phoneNo+"&amount="+amount,
                HttpMethod.GET, null, Double.class).getBody();
        if (finalAmount == null) {
            return "Money not added to your wallet. please try again";
        }
        return "Money successfully added to your wallet. And your final amount is : " + finalAmount + " Rupees";

    }
}
