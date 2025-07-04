package com.recargapay.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recargapay.wallet.dto.DepositRequest;
import com.recargapay.wallet.dto.CreateWalletRequest;
import com.recargapay.wallet.entity.Wallet;
import com.recargapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WalletControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateWallet() throws Exception {
        CreateWalletRequest request = new CreateWalletRequest();
        request.setUserId(123L);

        mockMvc.perform(post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void testDeposit() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet = walletRepository.save(wallet);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAmount(BigDecimal.valueOf(150));

        mockMvc.perform(post("/api/wallets/" + wallet.getId() + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk());
    }
}
