package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import it.catalog.persistence.entity.Tag;
import it.catalog.service.dto.TagDto;

@Mapper(componentModel = "spring")
public interface TagMapper {

	
	//TagDto toDto(Tag entity); // Questo viene generato automaticamente da MapStruct per la conversione automatica item-to-item
    Tag toEntity(TagDto dto);
    
    List<TagDto> mapToList(List<Tag> entity); // Questo usa internamente il metodo sopra per ogni item
}
