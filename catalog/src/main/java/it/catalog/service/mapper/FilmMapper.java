package it.catalog.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.persistence.entity.Film;
import it.catalog.persistence.entity.Tag;
import it.catalog.service.dto.FilmDto;
import it.catalog.service.dto.TagDto;

@Mapper(componentModel = "spring")
public interface FilmMapper {

	
	@Mapping(target = "tags", expression = "java(getTags(entity.getTags()))")
	FilmDto toDto(Film entity); 
	
	
	Film toEntity(FilmDto dto); 
	
	
	 @Mapping(target = "tipoOggetto", constant = "Film")
	 Tag toTagEntity(TagDto dto);
	
	 
	 List<FilmDto> toDtoList(List<Film> entities);
	 
	 
	// Conversione Page<Entity> → Page<Dto>
	    default Page<FilmDto> toDtoPage(Page<Film> entityPage) {
	        List<FilmDto> dtoList = toDtoList(entityPage.getContent());
	        return new PageImpl<>(
	                dtoList,
	                entityPage.getPageable(),
	                entityPage.getTotalElements()
	        );
	    }
		
	    
	    default List<TagDto> getTags(Set<Tag> tags) {
	        if (tags == null) return null;
	        return tags.stream()
	            .map(tag -> new TagDto(tag.getIdTag(), tag.getNomeTag(),tag.getTipoOggetto())) // Adatta al tuo costruttore TagDto
	            .collect(Collectors.toList());
	    }

	 
}
