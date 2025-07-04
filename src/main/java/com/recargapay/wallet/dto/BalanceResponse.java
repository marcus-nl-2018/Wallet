package com.recargapay.wallet.dto;

import java.math.BigDecimal;

public class BalanceResponse {

    private Long walletId;
    private BigDecimal balance;

    public BalanceResponse(Long walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public Long getWalletId() {
        return walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
