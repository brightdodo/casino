package com.games.casino.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceResponse(Integer playerId, BigDecimal balance) {
}
