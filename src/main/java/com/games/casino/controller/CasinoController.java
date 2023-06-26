package com.games.casino.controller;

import com.games.casino.model.Transaction;
import com.games.casino.model.dto.BalanceResponse;
import com.games.casino.model.dto.PlayerRequest;
import com.games.casino.model.dto.PlayerResponse;
import com.games.casino.model.dto.TransactionsRequest;
import com.games.casino.model.dto.UpdateBalanceRequest;
import com.games.casino.model.dto.UpdateBalanceResponse;
import com.games.casino.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CasinoController {
    private final GameService gameService;

    @PostMapping("/player")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse createPlayer(@Valid @RequestBody PlayerRequest request){
        return gameService.createPlayer(request);
    }

    @GetMapping("/player/{playerId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Integer playerId){
        return ResponseEntity.ok(gameService.getPlayerBalance(playerId));
    }

    @PutMapping("/player/{playerId}/balance/update")
    public UpdateBalanceResponse updateBalance(@PathVariable Integer playerId,
            @Valid @RequestBody UpdateBalanceRequest request){
        return gameService.updateBalance(playerId, request);
    }

    @PostMapping("/admin/player/transactions")
    public List<Transaction> fetchPlayerTransactions(@Valid @RequestBody TransactionsRequest request){
        return gameService.fetchTransactions(request);
    }
}
