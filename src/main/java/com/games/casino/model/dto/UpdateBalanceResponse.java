package com.games.casino.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record UpdateBalanceResponse(Long transactionId, BigDecimal balance) {
}
