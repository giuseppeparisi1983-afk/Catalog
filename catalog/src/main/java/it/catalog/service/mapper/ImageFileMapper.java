package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.catalog.persistence.entity.ImageFile;
import it.catalog.service.dto.ImageDto;

@Mapper(componentModel = "spring")
public interface ImageFileMapper {
	
	@Mapping(target = "formato", source = "formato") 
	@Mapping(target = "tipoFile", source = "tipoFile") 
	ImageDto toDto(ImageFile entity); 
	
	@Mapping(target = "formato", expression = "java(ImageFile.Formato.valueOf(dto.getFormato()))") 
	@Mapping(target = "tipoFile", expression = "java(ImageFile.TipoFile.valueOf(dto.getTipoFile()))") 
	ImageFile toEntity(ImageDto dto); 
	
	
	List<ImageDto> toDtoList(List<ImageFile> entities);
	}


