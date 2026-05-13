package it.catalog.service.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;

import it.catalog.persistence.entity.Tag;
import it.catalog.service.dto.TagDto;

@Mapper(componentModel = "spring")
public interface TagMapper {

	
	TagDto toDto(Tag entity);
    Tag toEntity(TagDto dto);
    
    List<TagDto> mapToList(List<Tag> entity); // Questo usa internamente il metodo sopra per ogni item
}
