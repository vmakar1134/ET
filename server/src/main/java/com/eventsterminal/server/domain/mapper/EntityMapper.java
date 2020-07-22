package com.eventsterminal.server.domain.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface EntityMapper<ENTITY, DTO> {

    DTO toDto(ENTITY entity);

    default List<DTO> toDtoList(List<ENTITY> entities) {
        return entities
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
