package it.catalog.service.impl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.catalog.persistence.entity.Video;
import it.catalog.persistence.repository.VideoRepository;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.VideoDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.VideoMapper;
import it.catalog.utility.PathPrefixProvider;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoService implements SearchService<VideoDto, DtoFilter>{


	    private VideoRepository repository;
	    private VideoMapper mapper;
	    private final TagService tagService;
	    private SpecificationFactory<Video> specificationFactory;


	    @Autowired
	    private PathPrefixProvider prefixProvider;

	    
	  
	/*
	 * public VideoService(VideoRepository videoRepository) { this.videoRepository =
	 * videoRepository; }
	 */

    public VideoService(VideoRepository repository, VideoMapper mapper, TagService tagService,
				SpecificationFactory<Video> specificationFactory, PathPrefixProvider prefixProvider) {
			super();
			this.repository = repository;
			this.mapper = mapper;
			this.tagService = tagService;
			this.specificationFactory = specificationFactory;
			this.prefixProvider = prefixProvider;
		}

	
	@Override
	/** query fatta sulla index per avere tutti i tags dell'oggetto video */
	public List<TagDto> getAllTags() {
		return tagService.findByTipoOggetto("Video");
	}
	
	// Usato per la paginazione della grid nella Index. Servono i tag per le colonne della tabella
	@Override
	 @Transactional(readOnly = true)
	public Page<VideoDto> findPage(Pageable pageable,DtoFilter filter) {
		
	    // 1. Creiamo la specifica basata sul filtro ricevuto dalla UI
		// Uniamo la specifica del filtro CON quella del fetch
	    Specification<Video> spec = specificationFactory.build(filter); 

	 // 2. Eseguiamo la query filtrata e paginata
	    Page<Video> entityPage = repository.findAll(spec, pageable); // useremo il findAll(Spec, Pageable) standard del repository
	    
		if (entityPage.isEmpty()) { // caso Not Found
			return new PageImpl<>(Collections.emptyList(), pageable, 0); 
		}

		return mapper.toDtoPage(entityPage,prefixProvider);

	}
	
	// Utilizzato per la navigazione rapida nella grid sulla pagina del Form. Non serve caricare i tags, poichè siamo in modalità view basta sapere l'ID alla posizione X
	@Override
	@Transactional(readOnly = true)
	public Optional<Long> findIdAtPosition(DtoFilter filter, Sort sort, int index) {
	    if (index < 0) return Optional.empty();

	    // 1. Usiamo la tua specFactory per costruire il filtro
	    Specification<Video> spec = specificationFactory.build(filter);

	    // 2. Creiamo una richiesta di pagina per un singolo elemento all'indice specificato
	    // PageRequest.of(index, 1, sort) -> Pagina numero 'index', dimensione 1
	    PageRequest pageable = PageRequest.of(index, 1, 
	    		 (sort != null && sort.isSorted()) ? sort : Sort.by(Sort.Direction.DESC, "id"));

	    // 3. Eseguiamo la query
	 // Usiamo SEMPRE la versione leggera qui. Ci serve sapere che l'ID alla posizione 7 è il numero 27!
	    // Usiamo il findAll standard. Poiché NON abbiamo messo fetchTags(), 
	    // Hibernate NON caricherà le collezioni e il database farà un OFFSET velocissimo.
	    Page<Video> result = repository.findAll(spec, pageable);

	    log.debug("Navigazione: cerco indice {}, trovato ID {}", index, 
	             result.hasContent() ? result.getContent().get(0).getId() : "NULL");
	    
	    // 4. Restituiamo l'ID se trovato
	    return result.getContent().stream()
	                 .map(Video::getId)
	                 .findFirst();
	}

	@Override
	public long count(DtoFilter filter) {
	    return repository.count(specificationFactory.build(filter));
	}
	/*
	 * @Override public long count() {
	 * 
	 * return repository.count();
	 * 
	 * }
	 */

//    @Override
//    public List<String> getCategorie() {
//    	return repository.findDistinctCategoria();
//    }

//	 richiamato dalla pagina del form per aggiunta o modifica di un nuovo item*
    @Override
    public VideoDto findById(Long id) {
//		var dtoOpt = repository.findById(id).map(entity -> mapper.toDto(entity, prefixProvider));
		
		return repository.findById(id).map(entity -> mapper.toDto(entity, prefixProvider)).orElse(new VideoDto());
	}
    
    
//    @Override
//    public VideoDto findByVideoId(Integer id) {
//    	
//    	
//    	return repository.findById(id)
//    			.map(mapper::toDto);
//    }

    @Override
    @Transactional
    public VideoDto save(VideoDto video) {
	
   	 // Cerchiamo un eventuale record già presente con gli stessi criteri
	    Optional<Video> esistente = repository.findByTitoloAndCategoriaAndDurataMin(
	    		video.getNome(), video.getCategoria(), video.getDurataMin());
		
	    // Controllo per evitare di salvare un duplicato: se esiste già un video con lo stesso titolo, categoria e durata 
	    // e l'ID del DTO è null (nuovo) o diverso da quello trovato (modifica), lanciamo un'eccezione 
	    if (esistente.isPresent()) {
	        // Se l'ID del DTO è null (Nuovo) o se l'ID è diverso da quello trovato (Modifica con dati di un altro)
	        if (video.getId() == null || !esistente.get().getId().equals(video.getId())) {
	          log.error("Attenzione: il video '{}' è già presente in archivio con ID {}", video.getNome(), esistente.get().getId());
	        	throw new RuntimeException ("Attenzione: il video '" + video.getNome() + "' è già presente in archivio.");
	        }
	    }
    	
    	
    	
    	Video saved = repository.save(mapper.toEntity(video,prefixProvider));
    	log.info("Saved Video file {} with success.", saved.getId());
    	 return mapper.toDto(saved,prefixProvider);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(video -> {
            video.setCancelled(true);
            repository.save(video);
            log.info("Cancelled Video file {} with success", id);
        });
    }

    @Override
    public void recovery(Long id) {
    	repository.findById(id).ifPresent(video -> {
    		video.setCancelled(false);
    		repository.save(video);
    		log.info("Recovered Video file {} with success", id);
    	});
    }

//    @Override
//    public List<Video> searchByTitolo(String query) {    	
//    	return repository.findByTitoloContainingIgnoreCaseAndCancelledFalse(query);
//    }
//    
//    
//    @Override
//    public Page<VideoDto> getVideoByTitolo(String titolo, Pageable pageable){
//         Page<Video> result = repository.findByTitoloContainingIgnoreCase(titolo, pageable);
//         
//         return result.map(entity -> mapper.toDto(entity, prefixProvider));
//         
////    	return videoRepository.findByTitoloContainingIgnoreCase(titolo,pageable);
//    	
//    }
//
//    
//    @Override
//    public Page<VideoDto> getVideos(String search, int page, int size, Sort sort) {
//        Pageable pageable = PageRequest.of(page, size, sort);
//        Page<Video> result = repository.findByTitoloContainingIgnoreCase(search, pageable);
////        return result.map(mapper::toDto);
//        return result.map(entity -> mapper.toDto(entity, prefixProvider));
//    }
//    
//    @Override
//	public long getNumVideoByTitolo(String titolo) {
//	
//    	return repository.countByTitoloContainingIgnoreCase(titolo);
//	}
//        
//	@Override
//	public Page<VideoDto> searchByField(DtoFilter_ filtro, int page, int size, Sort sort) {
//		Pageable pageable = PageRequest.of(page, size, sort);
//
//		Specification<Video> spec = (root, query, cb) -> cb.conjunction();
//
//		if (filtro != null && filtro.getCampo() != null && filtro.getValore() != null) {
//			try {
//				Field field = VideoDto.class.getDeclaredField(filtro.getCampo());
//				Class<?> type = field.getType();
//
//				spec = specificationFactory.buildOld(filtro.getCampo(), filtro.getValore(), type);
//			} catch (NoSuchFieldException e) {
//				// Campo non valido, ignora il filtro
//			}
//		}
//		Page<Video> result = repository.findAll(spec, pageable);
//		return result.map(entity -> mapper.toDto(entity, prefixProvider));
//	}
	


}
