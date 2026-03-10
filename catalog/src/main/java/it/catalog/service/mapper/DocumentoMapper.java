package it.catalog.service.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.catalog.persistence.entity.Documento;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.utility.PathPrefixProvider;

@Mapper(componentModel = "spring")
public interface DocumentoMapper {
  
	@Mapping(target = "path", expression = "java(entity.getPath()!=null ? prefixProvider.getPrefix() + entity.getPath(): \"\")")
	DocumentoDto toDto(Documento entity, @Context PathPrefixProvider prefixProvider);
	
	@Mapping(target = "path", expression = "java(prefixResolver.stripPrefix(dto.getPath()))")
	Documento toEntity(DocumentoDto dto, @Context PathPrefixProvider prefixResolver);
}

