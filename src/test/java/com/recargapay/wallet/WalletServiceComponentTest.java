package com.recargapay.wallet;

import com.recargapay.wallet.entity.Wallet;
import com.recargapay.wallet.exception.InsufficientBalanceException;
import com.recargapay.wallet.repository.TransactionRepository;
import com.recargapay.wallet.repository.WalletRepository;
import com.recargapay.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class WalletServiceComponentTest {

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private Wallet testWallet;

    @BeforeEach
    void setUp() {
        testWallet = new Wallet();
        testWallet.setId(1L);
        testWallet.setUserId(10L);
        testWallet.setBalance(BigDecimal.valueOf(100.00));
    }

    @Test
    void shouldDepositFundsSuccessfully() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

        walletService.deposit(1L, BigDecimal.valueOf(50.00));

        verify(walletRepository).save(testWallet);
        assertEquals(BigDecimal.valueOf(150.00), testWallet.getBalance());
    }

    @Test
    void shouldWithdrawFundsWhenBalanceIsSufficient() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

        walletService.withdraw(1L, BigDecimal.valueOf(40.00));

        verify(walletRepository).save(testWallet);
        assertEquals(BigDecimal.valueOf(60.00), testWallet.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(testWallet));

        assertThrows(InsufficientBalanceException.class, () ->
                walletService.withdraw(1L, BigDecimal.valueOf(200.00)));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionIfWalletNotFound() {
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                walletService.deposit(99L, BigDecimal.valueOf(10.00)));

        verify(walletRepository, never()).save(any());
    }
}
