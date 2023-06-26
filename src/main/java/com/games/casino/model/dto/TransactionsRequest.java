package com.games.casino.model.dto;

import jakarta.validation.constraints.NotBlank;

public record TransactionsRequest(@NotBlank String username) {
}
