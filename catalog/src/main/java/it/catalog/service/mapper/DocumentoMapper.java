package it.catalog.service.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.catalog.persistence.entity.Documento;
import it.catalog.persistence.entity.Tag;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.TagDto;
import it.catalog.utility.PathPrefixProvider;

@Mapper(componentModel = "spring", uses = { TagMapper.class })
public interface DocumentoMapper {
  
	@Mapping(target = "path", expression = "java(entity.getPath()!=null ? prefixProvider.getPrefix() + entity.getPath(): \"\")")
	//@Mapping(target = "tags", expression = "java(getTags(entity.getTags()))")
	DocumentoDto toDto(Documento entity, @Context PathPrefixProvider prefixProvider);
	
	@Mapping(target = "path", expression = "java(prefixResolver.stripPrefix(dto.getPath()))")
	Documento toEntity(DocumentoDto dto, @Context PathPrefixProvider prefixResolver);
	
	// Questo metodo istruisce MapStruct su come mappare il singolo Tag
//    @Mapping(target = "tipoOggetto", constant = "Documento") // Forza il tipo su 'Documento' per i nuovi tag
//    Tag toTagEntity(TagDto dto);
    
    List<DocumentoDto> toDtoList(List<Documento> entities);

 // Conversione Page<Entity> → Page<Dto>
    default Page<DocumentoDto> toDtoPage(Page<Documento> entityPage) {
        List<DocumentoDto> dtoList = toDtoList(entityPage.getContent());
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

