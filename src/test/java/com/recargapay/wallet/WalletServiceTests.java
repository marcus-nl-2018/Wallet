package com.recargapay.wallet;

import com.recargapay.wallet.entity.Wallet;
import com.recargapay.wallet.exception.InsufficientBalanceException;
import com.recargapay.wallet.repository.TransactionRepository;
import com.recargapay.wallet.repository.WalletRepository;
import com.recargapay.wallet.service.WalletService;
import com.recargapay.wallet.service.WalletServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class WalletServiceTests {

    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private WalletService walletService;

    @BeforeEach
    void setup() {
        walletRepository = mock(WalletRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        walletService = new WalletServiceImpl(walletRepository, transactionRepository);
    }

    @Test
    void testDeposit() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.ZERO);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        walletService.deposit(1L, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), wallet.getBalance());
    }

    @Test
    void testWithdrawWithSufficientBalance() {
        Wallet wallet = new Wallet();
        wallet.setId(2L);
        wallet.setBalance(BigDecimal.valueOf(200));

        when(walletRepository.findById(2L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        walletService.withdraw(2L, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), wallet.getBalance());
    }

    @Test
    void testWithdrawWithInsufficientBalance() {
        Wallet wallet = new Wallet();
        wallet.setId(3L);
        wallet.setBalance(BigDecimal.valueOf(30));

        when(walletRepository.findById(3L)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientBalanceException.class,
                () -> walletService.withdraw(3L, BigDecimal.valueOf(100)));
    }
}
