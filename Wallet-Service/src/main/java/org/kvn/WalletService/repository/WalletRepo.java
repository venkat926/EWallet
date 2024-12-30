package org.kvn.WalletService.repository;

import org.kvn.WalletService.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Integer> {
}
