package com.games.casino.service;

import com.games.casino.exception.InsufficientBalanceException;
import com.games.casino.exception.InvalidPlayerIdException;
import com.games.casino.model.Player;
import com.games.casino.model.Transaction;
import com.games.casino.model.TransactionType;
import com.games.casino.model.dto.BalanceResponse;
import com.games.casino.model.dto.PlayerRequest;
import com.games.casino.model.dto.PlayerResponse;
import com.games.casino.model.dto.TransactionsRequest;
import com.games.casino.model.dto.UpdateBalanceRequest;
import com.games.casino.model.dto.UpdateBalanceResponse;
import com.games.casino.model.mapper.PlayerMapper;
import com.games.casino.model.mapper.TransactionMapper;
import com.games.casino.repository.PlayerRepository;
import com.games.casino.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    public BalanceResponse getPlayerBalance(Integer playerId) {
        log.info("Fetching balance for player with ID: {}", playerId);
        return playerRepository.findById(playerId)
                .map(playerMapper::toBalance)
                .orElseThrow(() -> new InvalidPlayerIdException("Invalid player ID: " + playerId));
    }

    public UpdateBalanceResponse updateBalance(Integer playerId, UpdateBalanceRequest request) {
        return playerRepository.findById(playerId)
                .map(player -> updatePlayerTransactions(player, request))
                .orElseThrow(() -> new InvalidPlayerIdException("Invalid player ID: " + playerId));
    }

    private UpdateBalanceResponse updatePlayerTransactions(Player player, UpdateBalanceRequest request) {
        var transactionEntity = transactionMapper.toEntity(request);
        if (TransactionType.WIN.equals(transactionEntity.getTransactionType())) {
            player.setBalance(player.getBalance().add(transactionEntity.getAmount()));
        } else if (TransactionType.WAGER.equals(transactionEntity.getTransactionType())) {
            if(player.getBalance().compareTo(transactionEntity.getAmount()) > 0) {
                player.setBalance(player.getBalance().subtract(transactionEntity.getAmount()));
            } else {
                throw new InsufficientBalanceException("You do not have enough balance to place this wager");
            }
        }
        transactionEntity.setPlayer(player);
        transactionRepository.save(transactionEntity);
        playerRepository.save(player);
        return UpdateBalanceResponse.builder()
                .transactionId(transactionEntity.getId())
                .balance(player.getBalance())
                .build();
    }

    public List<Transaction> fetchTransactions(TransactionsRequest request) {
        var transactions = playerRepository.findByUsername(request.username())
                .map(Player::getTransactions)
                .orElseThrow(() -> new InvalidPlayerIdException("Username provided is not valid"));
        return transactions.stream()
                .limit(10)
                .toList();
    }

    public PlayerResponse createPlayer(PlayerRequest request) {
        log.info("Creating new user {}", request.toString());
        var playerEntity = playerMapper.toEntity(request);
        playerRepository.save(playerEntity);
        return playerMapper.toResponse(playerEntity);
    }
}
