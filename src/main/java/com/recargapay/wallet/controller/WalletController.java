package com.recargapay.wallet.controller;

import com.recargapay.wallet.dto.*;
import com.recargapay.wallet.service.WalletService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Create a new wallet for a user.
     */
    @PostMapping
    public ResponseEntity<Long> createWallet(@RequestBody CreateWalletRequest request) {
        Long walletId = walletService.createWallet(request.getUserId());
        return ResponseEntity.ok(walletId);
    }

    /**
     * Get current wallet balance.
     */
    @GetMapping("/{walletId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable Long walletId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime at) {

        BigDecimal balance = (at == null)
                ? walletService.getBalance(walletId)
                : walletService.getHistoricalBalance(walletId, at);

        return ResponseEntity.ok(new BalanceResponse(walletId, balance));
    }

    /**
     * Deposit funds to wallet.
     */
    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Void> deposit(
            @PathVariable Long walletId,
            @RequestBody DepositRequest request) {
        walletService.deposit(walletId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    /**
     * Withdraw funds from wallet.
     */
    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Void> withdraw(
            @PathVariable Long walletId,
            @RequestBody WithdrawRequest request) {
        walletService.withdraw(walletId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    /**
     * Transfer funds between wallets.
     */
    @PostMapping("/{walletId}/transfer")
    public ResponseEntity<Void> transfer(
            @PathVariable Long walletId,
            @RequestBody TransferRequest request) {
        walletService.transfer(walletId, request.getToWalletId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
