package com.games.casino.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PlayerRequest(@NotBlank String username, @Min(value = 0) BigDecimal balance) {
}
