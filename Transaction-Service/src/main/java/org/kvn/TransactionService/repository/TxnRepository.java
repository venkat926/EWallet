package org.kvn.TransactionService.repository;

import org.kvn.TransactionService.model.Txn;
import org.kvn.TransactionService.model.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TxnRepository extends JpaRepository<Txn, Integer> {

    @Transactional
    @Modifying
    @Query("update Txn t set t.txnStatus=:txnStatus where t.txnId=:txnId")
    void updateTxnStatus(@Param("txnStatus") TxnStatus txnStatus, @Param("txnId") String txnId);
}
