package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.catalog.persistence.entity.Chitarra;
import it.catalog.service.dto.GuitarDto;

@Mapper(componentModel = "spring")
public interface GuitarMapper {

	/**
	 * MapStruct gestisce direttamente la costruzione del Video 
	 * e l’assegnazione dei campi.
	 * */
	@Mapping(target = "id", source = "idGuitar")
    @Mapping(target = "video.id", source = "id")
    @Mapping(target = "video.titolo", source = "titolo")
    @Mapping(target = "video.categoria", source = "categoria")
    @Mapping(target = "video.percorsoFile", source = "percorsoFile")
	@Mapping(target = "video.preferito", source = "preferito")
	@Mapping(target = "video.rating", source = "rating")
	@Mapping(target = "video.visualizzazioni", source = "visualizzazioni")
	@Mapping(target = "video.durataMin", source = "durataMin")
	@Mapping(target = "video.dataArchiviazione", source = "dataArchiviazione")
	@Mapping(target = "video.ultimaVisualizzazione", source = "ultimaVisualizzazione")
	@Mapping(target = "video.backup", source = "backup")
	@Mapping(target = "video.note", source = "note")
	@Mapping(target = "video.cancelled", source = "cancelled")
	Chitarra toEntity(GuitarDto dto);

    @Mapping(target = "idGuitar", source = "id")
    @Mapping(target = "id", source = "video.id")
    @Mapping(target = "titolo", source = "video.titolo")
    @Mapping(target = "categoria", source = "video.categoria")
	@Mapping(target = "percorsoFile", source = "video.percorsoFile")
	@Mapping(target = "preferito", source = "video.preferito")
	@Mapping(target = "rating", source = "video.rating")
	@Mapping(target = "visualizzazioni", source = "video.visualizzazioni")
	@Mapping(target = "durataMin", source = "video.durataMin")
	@Mapping(target = "dataArchiviazione", source = "video.dataArchiviazione")
	@Mapping(target = "ultimaVisualizzazione", source = "video.ultimaVisualizzazione")
	@Mapping(target = "backup", source = "video.backup")
	@Mapping(target = "note", source = "video.note")
	@Mapping(target = "cancelled", source = "video.cancelled")
	GuitarDto toDto(Chitarra video);

    List<GuitarDto> toDtoList(List<Chitarra> videos);
}

