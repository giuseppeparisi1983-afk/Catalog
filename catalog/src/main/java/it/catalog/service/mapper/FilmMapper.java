package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.persistence.entity.Film;
import it.catalog.service.dto.FilmDto;
import it.catalog.utility.PathPrefixProvider;

@Mapper(componentModel = "spring")
public interface FilmMapper {

	
//	@Mapping(target = "tags", expression = "java(getTags(entity.getTags()))")
//	@Mapping(target = "autore", source = "regista") 
	@Mapping(target = "duration", source = "durata") 
	@Mapping(target = "descrizione", source = "trama") 
	@Mapping(target = "path", expression = "java(entity.getPath()!=null ? prefixProvider.getPrefix() + entity.getPath(): \"\")")
	FilmDto toDto(Film entity, @Context PathPrefixProvider prefixProvider); 
	
	
//	@Mapping(target = "regista", source = "autore") 
	@Mapping(target = "durata", source = "duration") 
	@Mapping(target = "trama", source = "descrizione") 
	@Mapping(target = "path", expression = "java(prefixResolver.stripPrefix(dto.getPath()))")
	Film toEntity(FilmDto dto, @Context PathPrefixProvider prefixResolver); 
	
	
//	 @Mapping(target = "tipoOggetto", constant = "Film")
//	 Tag toTagEntity(TagDto dto);
	
	 
	 List<FilmDto> toDtoList(List<Film> entities, @Context PathPrefixProvider prefixProvider);
	 
	 
	// Conversione Page<Entity> → Page<Dto>
	    default Page<FilmDto> toDtoPage(Page<Film> entityPage, @Context PathPrefixProvider prefixProvider) {
	        List<FilmDto> dtoList = toDtoList(entityPage.getContent(), prefixProvider);
	        return new PageImpl<>(
	                dtoList,
	                entityPage.getPageable(),
	                entityPage.getTotalElements()
	        );
	    }
		
	    
//	    default List<TagDto> getTags(Set<Tag> tags) {
//	        if (tags == null) return null;
//	        return tags.stream()
//	            .map(tag -> new TagDto(tag.getIdTag(), tag.getNomeTag(),tag.getTipoOggetto())) // Adatta al tuo costruttore TagDto
//	            .collect(Collectors.toList());
//	    }

	 
}
