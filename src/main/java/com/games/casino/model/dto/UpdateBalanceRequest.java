package com.games.casino.model.dto;

import com.games.casino.model.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateBalanceRequest(@NotNull Integer playerId,
                                   @Min(value = 0)BigDecimal amount,
                                   TransactionType transactionType) {
}
