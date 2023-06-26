package com.games.casino.service;

import com.games.casino.exception.InsufficientBalanceException;
import com.games.casino.exception.InvalidAmountException;
import com.games.casino.exception.InvalidPlayerIdException;
import com.games.casino.model.Player;
import com.games.casino.model.Transaction;
import com.games.casino.model.TransactionType;
import com.games.casino.model.dto.BalanceResponse;
import com.games.casino.model.dto.PlayerRequest;
import com.games.casino.model.dto.PlayerResponse;
import com.games.casino.model.dto.TransactionsRequest;
import com.games.casino.model.dto.UpdateBalanceRequest;
import com.games.casino.model.mapper.PlayerMapper;
import com.games.casino.model.mapper.TransactionMapper;
import com.games.casino.repository.PlayerRepository;
import com.games.casino.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void getPlayerBalance() {
        var mockPlayer = new Player(1, "test", BigDecimal.TEN, null);
        when(playerRepository.findById(anyInt()))
                .thenReturn(Optional.of(mockPlayer));
        when(playerMapper.toBalance(any())).thenReturn(BalanceResponse.builder()
                        .balance(BigDecimal.TEN)
                .build());
        var result = gameService.getPlayerBalance(1);
        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.balance());
        verify(playerRepository, times(1)).findById(1);
        verify(playerMapper, times(1)).toBalance(mockPlayer);
    }

    @Test
    void getPlayerBalanceInvalidPlayerIdTest() {
        when(playerRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(InvalidPlayerIdException.class, () -> gameService.getPlayerBalance(1));
    }

    @Test
    void updateBalance() {
        var mockPlayer = new Player(1, "test", BigDecimal.TEN, new ArrayList<>());
        when(transactionMapper.toEntity(any())).thenReturn(new Transaction(1L, TransactionType.WIN, BigDecimal.TEN, mockPlayer));
        when(playerRepository.findById(anyInt()))
                .thenReturn(Optional.of(mockPlayer));
        var result = gameService.updateBalance(1, new UpdateBalanceRequest(1, BigDecimal.TEN, TransactionType.WIN));
        assertNotNull(result);
        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    void updateBalanceWager() {
        var mockPlayer = new Player(1, "test", BigDecimal.valueOf(100), new ArrayList<>());
        when(transactionMapper.toEntity(any())).thenReturn(new Transaction(1L, TransactionType.WAGER, BigDecimal.TEN, mockPlayer));
        when(playerRepository.findById(anyInt()))
                .thenReturn(Optional.of(mockPlayer));
        var result = gameService.updateBalance(1, new UpdateBalanceRequest(1, BigDecimal.TEN, TransactionType.WAGER));
        assertNotNull(result);
        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    void updateBalanceWagerInsufficientFunds() {
        var mockPlayer = new Player(1, "test", BigDecimal.valueOf(9), new ArrayList<>());
        when(transactionMapper.toEntity(any())).thenReturn(new Transaction(1L, TransactionType.WAGER, BigDecimal.TEN, mockPlayer));
        when(playerRepository.findById(anyInt()))
                .thenReturn(Optional.of(mockPlayer));
        assertThrows(InsufficientBalanceException.class, () -> gameService.updateBalance(1, new UpdateBalanceRequest(1, BigDecimal.TEN, TransactionType.WAGER)));
    }

    @Test
    void fetchTransactions() {
        var mockPlayer = new Player(1, "test", BigDecimal.valueOf(9), List.of(new Transaction()));
        when(playerRepository.findByUsername(anyString())).thenReturn(Optional.of(mockPlayer));
        var results = gameService.fetchTransactions(new TransactionsRequest("test"));
        assertFalse(results.isEmpty());
    }

    @Test
    void createPlayer() {
        var mockPlayer = new Player(1, "test", BigDecimal.valueOf(9), new ArrayList<>());
        when(playerMapper.toEntity(any())).thenReturn(mockPlayer);
        when(playerMapper.toResponse(any())).thenReturn(PlayerResponse.builder().build());
        var result = gameService.createPlayer(new PlayerRequest("test", BigDecimal.TEN));
        assertNotNull(result);
    }
}