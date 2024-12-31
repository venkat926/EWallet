package org.kvn.WalletService.service;

import org.kvn.WalletService.dto.ValidateWalletDTO;
import org.kvn.WalletService.model.Wallet;
import org.kvn.WalletService.repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepo walletRepo;

    public ValidateWalletDTO validateWallet(String contact, Double amount) {
        Wallet wallet = walletRepo.findByContact(contact);
        if (wallet == null) return new ValidateWalletDTO(false, false);
        else if (wallet.getBalance()<amount) return new ValidateWalletDTO(true, false);
        else return new ValidateWalletDTO(true, true);
    }
}
