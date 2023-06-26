package com.games.casino.model.mapper;

import com.games.casino.model.Transaction;
import com.games.casino.model.dto.UpdateBalanceRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(UpdateBalanceRequest request);
}
