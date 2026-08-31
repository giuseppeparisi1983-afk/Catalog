package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.persistence.entity.AudioFile;
import it.catalog.service.dto.AudioDto;
import it.catalog.utility.PathPrefixProvider;

@Mapper(componentModel = "spring")
public interface AudioFileMapper {
    
	@Mapping(target = "path", expression = "java(entity.getPath()!=null ? prefixProvider.getPrefix() + entity.getPath(): \"\")")
	@Mapping(target = "lastUpdate", source ="updatedAt" ) // Mappa lastUpdate → updatedAt 
	@Mapping(target = "estensione", source = "formato") 
//	@Mapping(target = "tags", expression = "java(getTags(entity.getTags()))")
	AudioDto toDto(AudioFile entity, @Context PathPrefixProvider prefixProvider); 
	

	@Mapping(target = "path", expression = "java(prefixResolver.stripPrefix(dto.getPath()))")
	@Mapping(target = "formato", source = "estensione") 
	@Mapping(target = "updatedAt", source ="lastUpdate" ) // Mappa  updatedAt → lastUpdate 
//	@Mapping(target = "tags", ignore = true) 
	AudioFile toEntity(AudioDto dto, @Context PathPrefixProvider prefixResolver);
	
	// Questo metodo istruisce MapStruct su come mappare il singolo Tag
//    @Mapping(target = "tipoOggetto", constant = "Audio") // Forza il tipo su 'Audio' per i nuovi tag
//    Tag toTagEntity(TagDto dto);
	
	
	List<AudioDto> toDtoList(List<AudioFile> entities, @Context PathPrefixProvider prefixProvider);
	
//	@Mapping(target = "tags", source = "tagsConcat")
//	AudioDto toDto(AudioFileCustomerEntity entity);
//	
//	List<AudioDto> toDtoList(List<AudioFileCustomerEntity> entities);
	
	
	// Conversione Page<Entity> → Page<Dto>
    default Page<AudioDto> toDtoPage(Page<AudioFile> entityPage, @Context PathPrefixProvider prefixProvider) {
        List<AudioDto> dtoList = toDtoList(entityPage.getContent(), prefixProvider);
        return new PageImpl<>(
                dtoList,
                entityPage.getPageable(),
                entityPage.getTotalElements()
        );
    }
	
	
//    default List<TagDto> getTags(Set<Tag> tags) {
//        if (tags == null) return null;
//        return tags.stream()
//            .map(tag -> new TagDto(tag.getIdTag(), tag.getNomeTag(),tag.getTipoOggetto())) // Adatta al tuo costruttore TagDto
//            .collect(Collectors.toList());
//    }
    
    
}

