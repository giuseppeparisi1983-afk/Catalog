package it.catalog.service.mapper;

import org.mapstruct.Mapper;

import it.catalog.persistence.entity.OggettoTag;
import it.catalog.service.dto.OggettoTagDto;

@Mapper(componentModel = "spring")
public interface OggettoTagMapper {
    OggettoTagDto toDto(OggettoTag entity);
    OggettoTag toEntity(OggettoTagDto dto);
}

