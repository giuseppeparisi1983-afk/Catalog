package it.catalog.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.catalog.persistence.entity.ImageFile;
import it.catalog.persistence.repository.ImageRepository;
import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.ImageFileMapper;
import it.catalog.utility.PathPrefixProvider;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageFileServiceImpl implements SearchService<ImageDto, DtoFilter>{

    private final ImageRepository repo;
    private final ImageFileMapper mapper;
    private final TagService tagService;

    private final PathPrefixProvider prefixProvider;
    private final SpecificationFactory<ImageFile> specFactory;
    
    public ImageFileServiceImpl(ImageRepository repo, ImageFileMapper mapper,TagService tagService
    		,SpecificationFactory<ImageFile> specFactory,
    		PathPrefixProvider prefixProvider) {
        this.repo = repo;
        this.mapper = mapper;
        this.tagService= tagService;
        this.specFactory=specFactory;
        this.prefixProvider = prefixProvider;
    }
    
	 @Override
	 /**query fatta sulla index per avere tutti i tags dell'oggetto immagine */
  public List<TagDto> getAllTags() {
		 return tagService.findByTipoOggetto("Image");
  }
    
	// Usato per la paginazione della grid nella Index. Servono i tag per le colonne della tabella
    @Override
    @Transactional(readOnly = true)
    public Page<ImageDto> findPage(Pageable pageable,DtoFilter filter) {
    	
    	 // 1. Creiamo la specifica basata sul filtro ricevuto dalla UI
    	// Uniamo la specifica del filtro CON quella del fetch
	    Specification<ImageFile> spec = specFactory.build(filter);
   	
	    Page<ImageFile> entityPage =repo.findAll(spec, pageable);
	    if (entityPage.isEmpty()) {
	        return new PageImpl<>(Collections.emptyList(), pageable, 0); // caso Not Found
	    }
	    
	    return mapper.toDtoPage(entityPage,prefixProvider);

    }
    
 // Utilizzato per la navigazione rapida nella grid sulla pagina del Form. Non serve caricare i tags, poichè siamo in modalità view basta sapere l'ID alla posizione X
    @Override
	@Transactional(readOnly = true)
	public Optional<Long> findIdAtPosition(DtoFilter filter, Sort sort, int index) {
	    if (index < 0) return Optional.empty();

	    // 1. Usiamo la tua specFactory per costruire il filtro
	    Specification<ImageFile> spec = specFactory.build(filter);

	    // 2. Creiamo una richiesta di pagina per un singolo elemento all'indice specificato
	    // PageRequest.of(index, 1, sort) -> Pagina numero 'index', dimensione 1
	    PageRequest pageable = PageRequest.of(index, 1, 
	    		(sort != null && sort.isSorted()) ? sort : Sort.by(Sort.Direction.DESC, "id"));

	    // 3. Eseguiamo la query
	 // Usiamo SEMPRE la versione leggera qui. Ci serve sapere che l'ID alla posizione 7 è il numero 27!
	    // Usiamo il findAll standard. Poiché NON abbiamo messo fetchTags(), 
	    // Hibernate NON caricherà le collezioni e il database farà un OFFSET velocissimo.
	    Page<ImageFile> result = repo.findAll(spec, pageable);

	    log.debug("Navigazione: cerco indice {}, trovato ID {}", index, 
	             result.hasContent() ? result.getContent().get(0).getId() : "NULL");
	    
	    
	    // 4. Restituiamo l'ID se trovato
	    return result.getContent().stream()
	                 .map(ImageFile::getId)
	                 .findFirst();
	}

	@Override
	public long count(DtoFilter filter) {
	    return repo.count(specFactory.build(filter));
	}
    
	/*
	 * @Override public long count() {
	 * 
	 * return repo.count(); }
	 */
    

    @Override
    /**richiamato dalla pagina del form per aggiunta o modifica di un nuovo item* */
    public ImageDto findById(Long id) {
//		Optional<ImageFile> opt = repo.findById(id);
//		if (opt.isEmpty()) {
//			log.warn("Image file with id {} not found", id);
//			return new ImageDto(); // ritorna un DTO vuoto se non trovato
//		}
    	
//    	var dtoOpt = opt.map(mapper::toDto);
        
    	return repo.findById(id).map(entity -> mapper.toDto(entity,prefixProvider))
    			.orElse(new ImageDto());
    }

    @Override
    @Transactional
    public ImageDto save(ImageDto dto) {
      
    	 // Cerchiamo un eventuale record già presente con gli stessi criteri
	    Optional<ImageFile> esistente = repo.findByNomeAndFormatoAndTipoFile(
	            dto.getNome(), dto.getFormato(), dto.getTipoFile());
		
	    // Controllo per evitare di salvare un duplicato: se esiste già un imagine con lo stesso nome, formato e tipo di file, 
	    // e l'ID del DTO è null (nuovo) o diverso da quello trovato (modifica), lanciamo un'eccezione 
	    if (esistente.isPresent()) {
	        // Se l'ID del DTO è null (Nuovo) o se l'ID è diverso da quello trovato (Modifica con dati di un altro)
	        if (dto.getId() == null || !esistente.get().getId().equals(dto.getId())) {
	          log.error("Attenzione: l'imagine '{}' è già presente in archivio con ID {}", dto.getNome(), esistente.get().getId());
	        	throw new RuntimeException ("Attenzione: l'imagine '" + dto.getNome() + "' è già presente in archivio.");
	        }
	    }
    	
	    ImageFile entity =mapper.toEntity(dto,prefixProvider);
	    entity = repo.save(entity);
        log.info("Save New Image file. Id {}",entity.getId());
     
        return dto;
    }

    @Override
    public void delete(Long id) {
        repo.findById(id).ifPresent(image -> {
        	image.setCancelled(true);
            repo.save(image);
            log.info("Cancelled Image file {} with success",id);
        });
    }

    @Override
    public void recovery(Long id) {
    	  repo.findById(id).ifPresent(image -> {
          	image.setCancelled(false);
              repo.save(image);
              log.info("Recovery Image file {} with success",id);
          });
    }

   

}

