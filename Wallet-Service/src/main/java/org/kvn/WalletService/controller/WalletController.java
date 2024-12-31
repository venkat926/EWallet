package org.kvn.WalletService.controller;

import org.kvn.WalletService.dto.ValidateWalletDTO;
import org.kvn.WalletService.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    @GetMapping("/validateWallet")
    public ValidateWalletDTO validateWalletDetails(@RequestParam("contact") String contact, @RequestParam("balance") Double amount) {

        logger.info("Received as API call to validate wallet associated with contact {}", contact);

        ValidateWalletDTO responseDTO = walletService.validateWallet(contact, amount);

        if (!responseDTO.isValidWallet())
            logger.info("There is no wallet associated with the given contact {}", contact);
        else if (!responseDTO.isDoesWalletHasEnoughAmount())
            logger.info("The wallet associated with given contact {} does not have enough amount to make the transaction", contact);
        else
            logger.info("The wallet associated with the contact {} is valid. You can proceed to make the transaction", contact);

        return responseDTO;
    }

    @GetMapping("/addMoney")
    public Double addMoneyToWallet(@RequestParam("contact") String contact,
                                   @RequestParam("amount") Double amount) {
        return walletService.addMoneyToWallet(contact, amount);
    }

}
