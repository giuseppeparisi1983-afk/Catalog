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

import it.catalog.persistence.entity.AudioFile;
import it.catalog.persistence.repository.AudioRepository;
import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.AudioFileMapper;
import it.catalog.utility.PathPrefixProvider;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AudioFileServiceImpl implements SearchService<AudioDto, DtoFilter> {

	private final AudioRepository repo;
	private final AudioFileMapper mapper;
	private final TagService tagService;

	private final PathPrefixProvider prefixProvider;
	private final SpecificationFactory<AudioFile> specFactory;

	public AudioFileServiceImpl(AudioRepository repo, AudioFileMapper mapper, TagService tagService,
			SpecificationFactory<AudioFile> specFactory,PathPrefixProvider prefixProvider) {
		this.repo = repo;
		this.mapper = mapper;
		this.tagService = tagService;
		this.specFactory = specFactory;
		this.prefixProvider = prefixProvider;
	}

	@Override
	/** query fatta sulla index per avere tutti i tags dell'oggetto audio */
	public List<TagDto> getAllTags() {
		return tagService.findByTipoOggetto("Audio");
	}

	// Usato per la paginazione della grid nella Index. Servono i tag per le colonne della tabella
	@Override
	 @Transactional(readOnly = true)
	public Page<AudioDto> findPage(Pageable pageable,DtoFilter filter) {

		//Specification<AudioFile> spec = (root, query, cb) -> cb.conjunction();

//		if (filtro != null && filtro.getCriterion() != null) {
//            try {
//           	 Field field = AudioDto.class.getDeclaredField(filtro.getCampo());
//                Class<?> type = field.getType();

		//	spec = specFactory.build("Audio", filtro);
			// 2) Tag filter
//          List<String> requiredTags = (filtro.getTags() != null && !filtro.getTags().isEmpty()) ? 
//        		  filtro.getTags().stream().map(TagDto::getNomeTag).collect(Collectors.toList()): 
//  	    			Collections.EMPTY_LIST;
//           	
//           	if(!requiredTags.isEmpty())
//               spec = spec.and(specFactory.withTag("Audio",requiredTags));

//            } catch (NoSuchFieldException e) {
//                // Campo non valido, ignora il filtro
//           	 log.error("Campo non valido",e);
//            }
//		}

		//Page<AudioFile> entityPage = repo.findAll(spec, pageable);
		
	    // Verifichiamo se la dimensione richiesta è superiore a 25
//	    int pageSize = Math.min(pageable.getPageSize(), 25);
//	    
//	    // Creiamo un nuovo PageRequest basato sui parametri originali ma col limite di 25
//	    Pageable constrainedPageable = PageRequest.of(
//	            pageable.getPageNumber(), 
//	            pageSize, 
//	            pageable.getSort()
//	    );

//		Page<AudioFile> entityPage = repo.findAllAudio(pageable);
		
		
	    // 1. Creiamo la specifica basata sul filtro ricevuto dalla UI
		// Uniamo la specifica del filtro CON quella del fetch
//	    Specification<AudioFile> spec = specFactory.build(filter)
//	    		.and(specFactory.fetchTags()); // <--- AGGIUNGE I TAG  
	    Specification<AudioFile> spec = specFactory.build(filter);  

	 // 2. Eseguiamo la query filtrata e paginata
	    // Grazie al @BatchSize(size=25) sull'entità, i tag verranno caricati efficientemente
	    Page<AudioFile> entityPage = repo.findAll(spec, pageable);
	    
		if (entityPage.isEmpty()) { // caso Not Found
			return new PageImpl<>(Collections.emptyList(), pageable, 0); 
		}
		// inizializza i tag per ogni AudioFile
//		entityPage.getContent().forEach(a -> a.getTags().size());

		return mapper.toDtoPage(entityPage,prefixProvider);

		/*
		 * return entityPage.map(entity -> {
		 * 
		 * AudioDto dto=mapper.toDto(entity);
		 * 
		 * dto.setTags(tagService.findTagsByObject("Audio", entity.getId()));
		 * 
		 * ERRORE Non posso recuperare nuovamente i tag per ogni record di file-audio,
		 * altrimenti dovrò eeguire un numero spropositato di query per ogni file-audio
		 * ed è impensabile. i tags devono essere recuperati già dalla I° query
		 * 
		 * 
		 * return dto;
		 * 
		 * });
		 */
	}
	
	// Utilizzato per la navigazione rapida nella grid sulla pagina del Form. Non serve caricare i tags, poichè siamo in modalità view basta sapere l'ID alla posizione X
	@Override
	@Transactional(readOnly = true)
	public Optional<Long> findIdAtPosition(DtoFilter filter, Sort sort, int index) {
	    if (index < 0) return Optional.empty();

	    // 1. Usiamo la tua specFactory per costruire il filtro (Senza fetchTags!)
	    Specification<AudioFile> spec = specFactory.build(filter);

	    // 2. Creiamo una richiesta di pagina per un singolo elemento all'indice specificato
	    // PageRequest.of(index, 1, sort) -> Pagina numero 'index', dimensione 1
	    PageRequest pageable = PageRequest.of(index, 1, 
	        sort.isUnsorted() ? Sort.by(Sort.Direction.DESC, "id") : sort);

	    // 3. Eseguiamo la query
	    // Usiamo SEMPRE la versione leggera qui. Ci serve sapere che l'ID alla posizione 7 è il numero 27!
	    // Usiamo il findAll standard. Poiché NON abbiamo messo fetchTags(), 
	    // Hibernate NON caricherà le collezioni e il database farà un OFFSET velocissimo.
	    Page<AudioFile> result = repo.findAll(spec, pageable);
	    
	    log.debug("Navigazione: cerco indice {}, trovato ID {}", index, 
	             result.hasContent() ? result.getContent().get(0).getId() : "NULL");

	    // 4. Restituiamo l'ID se trovato
	    return result.getContent().stream()
	                 .map(AudioFile::getId)
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
	/**
	 * richiamato dalla pagina del form per aggiunta o modifica di un nuovo item*
	 */
	public AudioDto findById(Long id) {

		return repo.findById(id).map(entity -> mapper.toDto(entity,prefixProvider))
				.orElse(new AudioDto());
	}

	@Override
	@Transactional
	public AudioDto save(AudioDto dto) {
		
		
		 // Cerchiamo un eventuale record già presente con gli stessi criteri
	    Optional<AudioFile> esistente = repo.findByNomeAndDurationAndAutoreAndAnno(
	            dto.getNome(), dto.getDuration(), dto.getAutore(),dto.getAnno());
		
	    // Controllo per evitare di salvare un duplicato: se esiste già un audio con lo stesso titolo, durata,  autore e anno
	    // e l'ID del DTO è null (nuovo) o diverso da quello trovato (modifica), lanciamo un'eccezione 
	    if (esistente.isPresent()) {
	        // Se l'ID del DTO è null (Nuovo) o se l'ID è diverso da quello trovato (Modifica con dati di un altro item)
	        if (dto.getId() == null || !esistente.get().getId().equals(dto.getId())) {
	          log.error("Attenzione: l'audio '{}' è già presente in archivio con ID {}", dto.getNome(), esistente.get().getId());
	        	throw new RuntimeException ("Attenzione: l'audio '" + dto.getNome() + "' è già presente in archivio.");
	        }
	    }
		
		
		// 1. MapStruct crea l'entità (con il Set<Tag> popolato dai TagDto)
        AudioFile entity = mapper.toEntity(dto,prefixProvider);
        
        // 2. Hibernate salva. 
        // - Se un Tag ha ID null, lo inserisce in 'tag' (grazie a PERSIST)
        // - Se un Tag ha ID ma è stato modificato, lo aggiorna (grazie a MERGE)
        // - In ogni caso, inserisce/aggiorna le righe in 'oggetto_tag'
        
		AudioFile saved = repo.save(entity);
		log.info("Save New Audio file");
		// Update tag relations
//		List<String> tagNames = dto.getTags() != null
//				? dto.getTags().stream().map(TagDto::getNomeTag).collect(Collectors.toList())
//				: Collections.EMPTY_LIST;

//		tagService.upsertTagsForObject("Audio", saved.getId(), tagNames);
//		log.info("Update Tags list for Audio file {}", saved.getId());

		return mapper.toDto(saved,prefixProvider);
	}

	@Override
	public void delete(Long id) {
		repo.findById(id).ifPresent(audio -> {
			audio.setCancelled(true);
			repo.save(audio);
			log.info("Cancelled Audio file {} with success", id);
		});
	}

	@Override
	public void recovery(Long id) {
		repo.findById(id).ifPresent(audio -> {
			audio.setCancelled(false);
			repo.save(audio);
			log.info("Recovery Audio file {} with success", id);
		});
	}
}
