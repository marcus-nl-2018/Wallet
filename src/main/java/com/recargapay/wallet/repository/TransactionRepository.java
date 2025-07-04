package com.recargapay.wallet.repository;

import com.recargapay.wallet.entity.Transaction;
import com.recargapay.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletAndTimestampBefore(Wallet wallet, LocalDateTime timestamp);
}
