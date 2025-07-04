package com.recargapay.wallet.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface WalletService {

    Long createWallet(Long userId);

    BigDecimal getBalance(Long walletId);

    BigDecimal getHistoricalBalance(Long walletId, LocalDateTime at);

    void deposit(Long walletId, BigDecimal amount);

    void withdraw(Long walletId, BigDecimal amount);

    void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount);
}
