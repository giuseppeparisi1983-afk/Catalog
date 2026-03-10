package it.catalog.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.persistence.entity.AudioFile;
import it.catalog.persistence.entity.OggettoTag;
import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;

@Mapper(componentModel = "spring")
public interface AudioFileMapper {
    
	@Mapping(target = "formato", source = "formato") 
//	@Mapping(target = "tags", ignore = true) 
	@Mapping(target = "tags", expression = "java(getTags(entity.getTags()))")
	AudioDto toDto(AudioFile entity); 
	
	@Mapping(target = "formato", expression = "java(AudioFile.Formato.valueOf(dto.getFormato()))") 
	AudioFile toEntity(AudioDto dto);
	
	List<AudioDto> toDtoList(List<AudioFile> entities);
	
	
	// Conversione Page<Entity> → Page<Dto>
    default Page<AudioDto> toDtoPage(Page<AudioFile> entityPage) {
        List<AudioDto> dtoList = toDtoList(entityPage.getContent());
        return new PageImpl<>(
                dtoList,
                entityPage.getPageable(),
                entityPage.getTotalElements()
        );
    }
	
	
    default List<TagDto> getTags(Set<OggettoTag> tags) {
        if (tags == null) return null;
        return tags.stream()
            .map(tag -> new TagDto(tag.getTag().getIdTag(), tag.getTag().getNomeTag(),tag.getTag().getTipoOggetto())) // Adatta al tuo costruttore TagDto
            .collect(Collectors.toList());
    }
    
    
    
}

