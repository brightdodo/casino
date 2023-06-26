package com.games.casino.model.mapper;

import com.games.casino.model.Player;
import com.games.casino.model.dto.BalanceResponse;
import com.games.casino.model.dto.PlayerRequest;
import com.games.casino.model.dto.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player toEntity(PlayerRequest request);
    PlayerResponse toResponse(Player entity);
    @Mapping(target = "playerId", source = "id")
    BalanceResponse toBalance(Player entity);
}
