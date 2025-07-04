package com.recargapay.wallet.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Insufficient balance in wallet.");
    }
}