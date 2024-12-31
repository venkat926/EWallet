package org.kvn.WalletService.repository;

import org.kvn.WalletService.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface WalletRepo extends JpaRepository<Wallet, Integer> {

    Wallet findByContact(String contact);


    @Transactional
    @Modifying
    @Query("UPDATE Wallet w set w.balance=w.balance+:amount WHERE w.contact=:contact")
    void updateWallet(String contact, Double amount);

    @Query("SELECT w.balance from Wallet w where w.contact=:senderContact")
    Object getBalance(String senderContact);
}
