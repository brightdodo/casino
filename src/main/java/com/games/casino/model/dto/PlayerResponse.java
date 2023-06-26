package com.games.casino.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record PlayerResponse(Integer id, String username, BigDecimal balance) {
}
