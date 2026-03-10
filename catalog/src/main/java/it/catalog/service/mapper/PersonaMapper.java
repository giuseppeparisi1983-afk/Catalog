package it.catalog.service.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import it.catalog.persistence.entity.OggettoTag;
import it.catalog.persistence.entity.Persona;
import it.catalog.persistence.repository.OggettoTagRepository;
import it.catalog.service.dto.PersonaDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.interfaces.TagService;

@Mapper(componentModel = "spring")
public interface PersonaMapper {
	 
	 
	@Mapping(target = "tags", expression = "java(setTags(persona, tagService))")
    PersonaDto toDto(Persona persona, @Context TagService tagService);
	 
	 @InheritInverseConfiguration
//	 @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTags")
	 @Mapping(target = "tags", expression = "java(mapTags(personaDto, tagService))")
    Persona toEntity(PersonaDto personaDto,@Context TagService tagService);
	 
	 // Custom: popola tags in DTO usando TagService
	 // questo metodo è utilizzato in fase di lettura dei tags perchè viene richiamato dal mapping che legge da db e popola il dto che deve essere mostrato
	 default List<TagDto> setTags(Persona entity, @Context TagService tagService) {
	        if (entity.getId() != null) {
	            // persona già esistente → leggo i tag associati
	            return tagService.findTagsByObject("Persona", entity.getId());
	            // la query me la posso risparmiare 
	            // devo passare da Set<OggettoTag> di entity a List<TagDto> da restituire
	        } else {
	            // nuova persona → propongo tutti i tag disponibili per il tipo
	            return tagService.findByTipoOggetto("Persona");
	        }
	    }
	 
	 @Named("mapTags")
	 // questo metodo è utilizzato in fase di scrittura dei tags perchè viene richiamato dal mapping che dal FE popola l'entity che deve essere scritta sul db
	 // passo da una List<TagDto> che ho dentro PersonaDto a Set<OggettoTag> da mettere dentro l'entity Persona
	 default Set<OggettoTag> mapTags(PersonaDto dto,@Context TagService tagService) {
	        if (dto.getTags() == null || dto.getTags().isEmpty()) 
	        	return Collections.emptySet();

//	        return tagNames.stream()
//	                .map(name -> {
//	                    Tag tag = new Tag(name);
//	                    OggettoTag oggettoTag = new OggettoTag();
//	                    oggettoTag.setTag(tag);
//	                    oggettoTag.setTipoOggetto("Persona");
//	                    // idOggetto sarà valorizzato da JPA quando salvi Persona
//	                    return oggettoTag;
//	                })
//	                .collect(Collectors.toSet());
	        // recupero degli OggettoTag riferiti delle persona di riferimento
	        
	        List<String> tagNames=dto.getTags() != null ? dto.getTags().stream() 
	        		.map(TagDto::getNomeTag).collect(Collectors.toList()) : Collections.EMPTY_LIST;
	        
	        tagService.upsertTagsForObject("Persona", dto.getId(),  tagNames); // aggiornamento dei tags
	       
	        return  tagService.findObjectTagByObject("Persona", dto.getId()).stream()
	        		.collect(Collectors.toSet());
	        
	    }
}
