package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.persistence.entity.ImageFile;
import it.catalog.service.dto.ImageDto;
import it.catalog.utility.PathPrefixProvider;

@Mapper(componentModel = "spring")
public interface ImageFileMapper {
		
//	@Mapping(target = "path", expression = "java(entity.getPath()!=null ? prefixProvider.getPrefix() + entity.getPath(): \"\")")
//	@Mapping(target = "tags", expression = "java(getTags(entity.getTags()))")
	@Mapping(target = "path", expression = "java(entity.getPath()!=null ? prefixProvider.getPrefix() + entity.getPath(): \"\")")
	ImageDto toDto(ImageFile entity, @Context PathPrefixProvider prefixProvider); 
	
//	@Mapping(target = "formato", expression = "java(ImageFile.Formato.valueOf(dto.getFormato()))") 
//	@Mapping(target = "tipoFile", expression = "java(ImageFile.TipoFile.valueOf(dto.getTipoFile()))") 
	@Mapping(target = "path", expression = "java(prefixResolver.stripPrefix(dto.getPath()))")
	ImageFile toEntity(ImageDto dto, @Context PathPrefixProvider prefixResolver); 
	
	
	// Questo metodo istruisce MapStruct su come mappare il singolo Tag
//    @Mapping(target = "tipoOggetto", constant = "Image") // Forza il tipo su 'Image' per i nuovi tag
//    Tag toTagEntity(TagDto dto);
	
	List<ImageDto> toDtoList(List<ImageFile> entities, @Context PathPrefixProvider prefixProvider);
	

	// Conversione Page<Entity> → Page<Dto>
    default Page<ImageDto> toDtoPage(Page<ImageFile> entityPage, @Context PathPrefixProvider prefixProvider) {
        List<ImageDto> dtoList = toDtoList(entityPage.getContent(), prefixProvider);
        return new PageImpl<>(
                dtoList,
                entityPage.getPageable(),
                entityPage.getTotalElements()
        );
    }
	
    
	/*
	 * default List<TagDto> getTags(Set<Tag> tags) { if (tags == null) return null;
	 * return tags.stream() .map(tag -> new TagDto(tag.getIdTag(),
	 * tag.getNomeTag(),tag.getTipoOggetto())) // Adatta al tuo costruttore TagDto
	 * .collect(Collectors.toList()); }
	 */

}


