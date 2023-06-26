package com.games.casino.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.casino.exception.InsufficientBalanceException;
import com.games.casino.exception.InvalidPlayerIdException;
import com.games.casino.model.TransactionType;
import com.games.casino.model.dto.PlayerRequest;
import com.games.casino.model.dto.PlayerResponse;
import com.games.casino.model.dto.TransactionsRequest;
import com.games.casino.model.dto.UpdateBalanceRequest;
import com.games.casino.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CasinoController.class)
class CasinoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should return CREATED given valid request")
    void createPlayer() throws Exception {
        when(gameService.createPlayer(any())).thenReturn(PlayerResponse.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PlayerRequest("test", BigDecimal.TEN))))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @DisplayName("Should return OK when getting balance given valid playerId")
    void getBalance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/player/{playerId}/balance", 1))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Should return 400 when getting balance given invalid playerId")
    void getBalanceInvalidPlayerIdTest() throws Exception {
        when(gameService.getPlayerBalance(anyInt())).thenThrow(new InvalidPlayerIdException("invalid Payer Id"));
        mockMvc.perform(MockMvcRequestBuilders.get("/player/{playerId}/balance", 2))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("Should return 400 when updating balance given invalid playerId")
    void updateBalanceInvalidPlayerIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/player/{playerId}/balance/update", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBalanceRequest(null, BigDecimal.TEN, TransactionType.WIN))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("Should return 400 when updating balance given negative amount")
    void updateBalanceNegativeAmountTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/player/{playerId}/balance/update", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBalanceRequest(1, BigDecimal.valueOf(-10), TransactionType.WIN))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("Should return 418 when placing wager with insufficient funds")
    void updateBalanceInsufficientFunds() throws Exception {
        when(gameService.updateBalance(anyInt(), any())).thenThrow(new InsufficientBalanceException("Insufficient Balance"));
        mockMvc.perform(MockMvcRequestBuilders.put("/player/{playerId}/balance/update", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBalanceRequest(1, BigDecimal.TEN, TransactionType.WIN))))
                .andExpect(status().isIAmATeapot())
                .andReturn();
    }

    @Test
    @DisplayName("Should return OK when updating balance given valid request")
    void updateBalance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/player/{playerId}/balance/update", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBalanceRequest(1, BigDecimal.TEN, TransactionType.WIN))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Should return OK when fetching transactions given valid username")
    void fetchPlayerTransactions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/player/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TransactionsRequest("test"))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Should return 400 when fetching transactions given invalid username")
    void fetchPlayerTransactionsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/player/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TransactionsRequest(""))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}