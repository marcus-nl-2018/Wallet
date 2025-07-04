package com.recargapay.wallet.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(Long walletId) {
        super("Wallet not found with ID: " + walletId);
    }

    public WalletNotFoundException() {
        super("Wallet not found.");
    }
}