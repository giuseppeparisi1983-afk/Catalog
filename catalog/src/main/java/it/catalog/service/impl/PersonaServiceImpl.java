package it.catalog.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.catalog.persistence.entity.Persona;
import it.catalog.persistence.repository.PersonaRepository;
import it.catalog.service.dto.PersonaDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.PersonaService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.PersonaMapper;
import it.catalog.utility.SpecificationFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Path;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository repository;
    private final TagService tagService;
    private final PersonaMapper mapper;
    
    private final SpecificationFactory<Persona> specFactory;


    public PersonaServiceImpl(PersonaRepository repository,TagService tagService,
    		PersonaMapper mapper,SpecificationFactory<Persona> specFactory) {
        this.repository = repository;
        this.tagService = tagService;
        this.mapper = mapper;
        this.specFactory = specFactory;
    }

    @Override
    public PersonaDto crea(PersonaDto dto) {
        Persona persona = mapper.toEntity(dto,tagService);
        persona =repository.save(persona);
        
//        List<String> tagNames=dto.getTags() != null ? dto.getTags().stream() 
//        		.map(TagDto::getNomeTag).collect(Collectors.toList()) : Collections.EMPTY_LIST;
//        
//     // salva i tag associati
//        tagService.upsertTagsForObject("Persona", persona.getId(),  tagNames);
        
        return mapper.toDto(persona,tagService);
    }

    @Override
    /** Non richiamato*/
    public PersonaDto modifica(Long id, PersonaDto dto) {
        Persona persona = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Persona non trovata"));

        persona.setNome(dto.getNome());
        persona.setCognome(dto.getCognome());
        persona.setDataNascita(dto.getDataNascita());

        return mapper.toDto(repository.save(persona),tagService);
    }

    @Override
    /**
     * richiamato dalla pagina del form per aggiunta o modifica di un nuovo item
     * */
    public PersonaDto trovaPerId(Long id) {
      
    	Optional<Persona> optPersona = repository.findById(id);
    	if (optPersona.isPresent()) {
    		return mapper.toDto(optPersona.get(),tagService);
    	}else 
    		return new PersonaDto();
    }

    @Override
    public Page<PersonaDto> searchByField(String field, String value, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Persona> result;
        switch (field) {
            case "data_nascita" -> result = repository.findByDataNascitaContainingIgnoreCase(LocalDate.parse(value), pageable);
            default -> {
            	int lastSpace = value.lastIndexOf(" ");
            	String cognome = value.substring(0, lastSpace);
            	String nome = value.substring(lastSpace + 1);
            	result = repository.findByCognomeContainingIgnoreCaseAndNomeContainingIgnoreCase(cognome,nome, pageable);
            	}
        }

//        return result.map(mapper::toDto);
        return result.map(entity -> mapper.toDto(entity,tagService));
    }
    
    @Override
    public List<PersonaDto> trovaTuttiAttivi() {
        return repository.findByAttivoTrue()
            .stream()
            .map(entity -> mapper.toDto(entity,tagService))
            .collect(Collectors.toList());
    }

    @Override
    public List<PersonaDto> trovaTutti(Pageable pageable) {
    	return repository.findAll(pageable)
    			.stream()
    			.map(entity -> mapper.toDto(entity,tagService))
    			.collect(Collectors.toList());
    }

    @Override
    public void cancella(Long id) {
        Persona persona = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Persona non trovata"));
        persona.setAttivo(false);
        repository.save(persona);
    }
    
    @Override
    /**
     * metodo invocato dalla ricerca automatica sull'index 
     * */
	public Page<PersonaDto> trovaConFiltro(DtoFilter_ filtro, Pageable pageable, List<String> filtroTags) {
	    //Specification<Persona> spec = specFromFilter(filtro);
	    
    	Specification<Persona> spec = (root, query, cb) -> cb.conjunction();

    	 
    	 if (filtro != null && filtro.getCampo() != null && filtro.getValore() != null) {
             try {
            	 Field field = PersonaDto.class.getDeclaredField(filtro.getCampo());
                 Class<?> type = field.getType();
            	 
            	spec = specFactory.buildOld(
                filtro.getCampo(),
                filtro.getValore(),
                type
            );
            	 // 2) Tag filter
            	if(!filtroTags.isEmpty())
                spec = spec.and(specFactory.withTag("Persona",filtroTags));
                
             } catch (NoSuchFieldException e) {
                 // Campo non valido, ignora il filtro
            	 log.error("Campo non valido",e);
             }
    	 }
    	
	    Page<Persona> entityPage =repository.findAll(spec, pageable);
	    if (entityPage.isEmpty()) {
	        return new PageImpl<>(Collections.emptyList(), pageable, 0); // caso Not Found
	    }
	    
	    // [DA VEDERE]: la ricerca deve essere fatta lato DB non lato applicativo
//	    entityPage= entityPage.getContent().stream()
//	            .map(p -> personaMapper.toDtoWithTags(p, tagService))
//	            .filter(dto -> filtroTags == null || filtroTags.isEmpty() ||
//	                           dto.getTags().stream().anyMatch(filtroTags::contains))
//	            .toList();
	    
	    
	    return entityPage.map(entity -> mapper.toDto(entity,tagService));
	}
	
    /*Non utilizzato perchè vale solo con campi di tipo stringa*/
	private Specification<Persona> specFromFilter(DtoFilter_ filtro) {
	    return (root, query, cb) -> {
	        if (filtro.getCampo() == null || filtro.getValore() == null) {
	            return cb.conjunction(); // nessun filtro
	        }

	        // Usa reflection per accedere al campo dinamico
	        Path<String> path = root.get(filtro.getCampo());
	        return cb.like(cb.lower(path), "%" + filtro.getValore().toLowerCase() + "%");
	    };
	}

	 @Override
	// Utility per autocomplete: tutti i tag disponibili per "Persona"
    public List<TagDto> getAllTagsForPersona() {
//        return tagService.findByTipoOggetto("Persona").stream()
//                .map(TagDto::getNomeTag)
//                .toList();
		 
		 return tagService.findByTipoOggetto("Persona");
    }

	 @Override
	 public List<TagDto> getAllTagsById(Long id) {
		 
		 return tagService.findTagsByObject("Persona",id);

	 }

}
