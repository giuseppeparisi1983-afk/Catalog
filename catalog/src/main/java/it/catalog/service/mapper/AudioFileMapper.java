package it.catalog.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.persistence.entity.AudioFile;
import it.catalog.persistence.entity.Tag;
import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;

@Mapper(componentModel = "spring")
public interface AudioFileMapper {
    
	@Mapping(target = "lastUpdate", source ="updatedAt" ) // Mappa lastUpdate → updatedAt 
	@Mapping(target = "formato", source = "formato") 
	@Mapping(target = "tags", expression = "java(getTags(entity.getTags()))")
	AudioDto toDto(AudioFile entity); 
	
	@Mapping(target = "formato", expression = "java(AudioFile.Formato.valueOf(dto.getFormato()))") 
	@Mapping(target = "updatedAt", source ="lastUpdate" ) // Mappa  updatedAt → lastUpdate 
//	@Mapping(target = "tags", ignore = true) 
	// MapStruct vedrà che AudioDto ha una List<TagDto> e AudioFile ha un Set<Tag>
    // Cercherà un metodo per mappare TagDto -> Tag e lo userà per l'intera collezione.
	AudioFile toEntity(AudioDto dto);
	
	// Questo metodo istruisce MapStruct su come mappare il singolo Tag
    @Mapping(target = "tipoOggetto", constant = "Audio") // Forza il tipo su 'Audio' per i nuovi tag
    Tag toTagEntity(TagDto dto);
	
	
	List<AudioDto> toDtoList(List<AudioFile> entities);
	
//	@Mapping(target = "tags", source = "tagsConcat")
//	AudioDto toDto(AudioFileCustomerEntity entity);
//	
//	List<AudioDto> toDtoList(List<AudioFileCustomerEntity> entities);
	
	
	// Conversione Page<Entity> → Page<Dto>
    default Page<AudioDto> toDtoPage(Page<AudioFile> entityPage) {
        List<AudioDto> dtoList = toDtoList(entityPage.getContent());
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

