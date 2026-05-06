package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.catalog.persistence.entity.Chitarra;
import it.catalog.service.dto.GuitarDto;

@Mapper(componentModel = "spring", uses = { VideoMapper.class })
public interface GuitarMapper {

	/**
	 * MapStruct gestisce direttamente la costruzione del Video 
	 * e l’assegnazione dei campi. Se Video (Entity) ha i tags e VideoDto ha i tags, 
	 * verranno copiati.
	 * */
	@Mapping(target = "id", source = "idGuitar")
	@Mapping(target = "video", source = "video")
	Chitarra toEntity(GuitarDto dto);

    @Mapping(target = "idGuitar", source = "id")
    @Mapping(target = "video", source = "video")
	GuitarDto toDto(Chitarra video);

    List<GuitarDto> toDtoList(List<Chitarra> videos);
}

