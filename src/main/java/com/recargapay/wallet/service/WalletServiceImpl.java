package com.recargapay.wallet.service;

import com.recargapay.wallet.entity.Transaction;
import com.recargapay.wallet.entity.Wallet;
import com.recargapay.wallet.enums.TransactionType;
import com.recargapay.wallet.exception.InsufficientBalanceException;
import com.recargapay.wallet.exception.WalletNotFoundException;
import com.recargapay.wallet.repository.TransactionRepository;
import com.recargapay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;
    private final TransactionRepository transactionRepo;

    public WalletServiceImpl(WalletRepository walletRepo, TransactionRepository transactionRepo) {
        this.walletRepo = walletRepo;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public Long createWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        return walletRepo.save(wallet).getId();
    }

    @Override
    public BigDecimal getBalance(Long walletId) {
        Wallet wallet = getWallet(walletId);
        return wallet.getBalance();
    }

    @Override
    public BigDecimal getHistoricalBalance(Long walletId, LocalDateTime at) {
        Wallet wallet = getWallet(walletId);
        List<Transaction> transactions = transactionRepo.findByWalletAndTimestampBefore(wallet, at);
        return transactions.stream()
                .map(tx -> tx.getType() == TransactionType.WITHDRAW || (tx.getType() == TransactionType.TRANSFER && tx.getWallet().getId().equals(walletId))
                        ? tx.getAmount().negate()
                        : tx.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public void deposit(Long walletId, BigDecimal amount) {
        Wallet wallet = getWallet(walletId);
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        transactionRepo.save(new Transaction(null, wallet, TransactionType.DEPOSIT, amount, LocalDateTime.now(), null));
    }

    @Override
    @Transactional
    public void withdraw(Long walletId, BigDecimal amount) {
        Wallet wallet = getWallet(walletId);
        if (wallet.getBalance().compareTo(amount) < 0) throw new InsufficientBalanceException();

        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        transactionRepo.save(new Transaction(null, wallet, TransactionType.WITHDRAW, amount, LocalDateTime.now(), null));
    }

    @Override
    @Transactional
    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        withdraw(fromWalletId, amount);
        deposit(toWalletId, amount);

        Wallet fromWallet = getWallet(fromWalletId);
        transactionRepo.save(new Transaction(null, fromWallet, TransactionType.TRANSFER, amount, LocalDateTime.now(), toWalletId));
    }

    private Wallet getWallet(Long id) {
        return walletRepo.findById(id).orElseThrow(WalletNotFoundException::new);
    }
}
