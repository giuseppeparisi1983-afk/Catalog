package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.common.enums.FileExtension;
import it.catalog.common.enums.VideoFormat;
import it.catalog.persistence.entity.Video;
import it.catalog.service.dto.VideoDto;
import it.catalog.utility.PathPrefixProvider;

@Mapper(componentModel = "spring", uses = { TagMapper.class })
public interface VideoMapper {

	@Mapping(target = "titolo", source = "nome")
	@Mapping(target = "percorsoFile", expression = "java(prefixResolver.stripPrefix(dto.getPath()))")
	// @Mapping(target = "categoria", expression =
	// "java(dto.getCategoria().getDescrizione())")
	Video toEntity(VideoDto dto, @Context PathPrefixProvider prefixResolver);

	@Mapping(target = "nome", source = "titolo")
//	@Mapping(target = "rating", source = "rating", qualifiedByName = "arrotondaADecimale")
	@Mapping(target = "path", expression = "java(video.getPercorsoFile()!=null ? prefixProvider.getPrefix() + video.getPercorsoFile(): \"\")")
//	@Mapping(target = "tags", expression = "java(getTags(video.getTags()))")
	VideoDto toDto(Video video, @Context PathPrefixProvider prefixProvider);

//	@Named("arrotondaADecimale")
//	default Double arrotondaADecimale(Double valore) {
//		if (valore == null)
//			return null;
//		return Math.round(valore * 10.0) / 10.0;
//	}

	List<VideoDto> toDtoList(List<Video> videos, @Context PathPrefixProvider prefixProvider);

	// Conversione Page<Entity> → Page<Dto>
	default Page<VideoDto> toDtoPage(Page<Video> entityPage, @Context PathPrefixProvider prefixProvider) {
		List<VideoDto> dtoList = toDtoList(entityPage.getContent(), prefixProvider);
		return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
	}

	/*
	 * default List<TagDto> getTags(Set<Tag> tags) { if (tags == null) return null;
	 * return tags.stream() .map(tag -> new TagDto(tag.getIdTag(),
	 * tag.getNomeTag(),tag.getTipoOggetto())) // Adatta al tuo costruttore TagDto
	 * .collect(Collectors.toList()); }
	 */


	/**
	 * BRIDGE METHOD: Questo metodo risolve il conflitto tra l'interfaccia
	 * FileExtension (GUI) e l'Enum concreto VideoFormat (DB).
	 */
	default VideoFormat mapExtension(FileExtension value) {
		if (value == null) {
			return null;
		}
		// Se l'oggetto che arriva implementa FileExtension ed è un VideoFormat,
		// facciamo il cast sicuro.
		if (value instanceof VideoFormat) {
			return (VideoFormat) value;
		}
		return null;
	}
}
