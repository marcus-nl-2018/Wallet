package com.recargapay.wallet.entity;

import com.recargapay.wallet.enums.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private LocalDateTime timestamp = LocalDateTime.now();

    private Long referenceWalletId; // For transfers

    public Transaction(Object o, Wallet wallet, TransactionType transactionType, BigDecimal amount, LocalDateTime now, Object o1) {
        this.id = (Long) o;
        this.wallet = wallet;
        this.type = transactionType;
        this.amount = amount;
        this.timestamp = now;
        this.referenceWalletId = (Long) o1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getReferenceWalletId() {
        return referenceWalletId;
    }

    public void setReferenceWalletId(Long referenceWalletId) {
        this.referenceWalletId = referenceWalletId;
    }

}
