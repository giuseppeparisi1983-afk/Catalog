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

import it.catalog.persistence.entity.Film;
import it.catalog.persistence.repository.FilmRepository;
import it.catalog.service.dto.FilmDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.FilmMapper;
import it.catalog.utility.PathPrefixProvider;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilmServiceImpl implements SearchService<FilmDto, DtoFilter> {

	private final FilmRepository repo;
	private final FilmMapper mapper;
	private final TagService tagService;

	private final PathPrefixProvider prefixProvider;
	private final SpecificationFactory<Film> specFactory;

	public FilmServiceImpl(FilmRepository repo, FilmMapper mapper, TagService tagService,
			SpecificationFactory<Film> specFactory,
    		PathPrefixProvider prefixProvider) {
		super();
		this.repo = repo;
		this.mapper = mapper;
		this.tagService = tagService;
		this.specFactory = specFactory;
		this.prefixProvider = prefixProvider;
	}

	@Override
	/** query fatta sulla index per avere tutti i tags dell'oggetto immagine */
	public List<TagDto> getAllTags() {
		return tagService.findByTipoOggetto("Film");
	}

	 // Usato per la paginazione della grid nella Index. Servono i tag per le colonne della tabella
	@Override
	@Transactional(readOnly = true)
	public Page<FilmDto> findPage(Pageable pageable, DtoFilter filter) {

		// 1. Creiamo la specifica basata sul filtro ricevuto dalla UI
		// Uniamo la specifica del filtro CON quella del fetch
		Specification<Film> spec = specFactory.build(filter);

		Page<Film> entityPage = repo.findAll(spec, pageable); // useremo il findAll(Spec, Pageable) standard del repository
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
	    Specification<Film> spec = specFactory.build(filter);

	    // 2. Creiamo una richiesta di pagina per un singolo elemento all'indice specificato
	    // PageRequest.of(index, 1, sort) -> Pagina numero 'index', dimensione 1
	    PageRequest pageable = PageRequest.of(index, 1, 
	    		 (sort != null && sort.isSorted()) ? sort : Sort.by(Sort.Direction.DESC, "id"));

	    // 3. Eseguiamo la query
	 // Usiamo SEMPRE la versione leggera qui. Ci serve sapere che l'ID alla posizione 7 è il numero 27!
	    // Usiamo il findAll standard. Poiché NON abbiamo messo fetchTags(), 
	    // Hibernate NON caricherà le collezioni e il database farà un OFFSET velocissimo.
	    Page<Film> result = repo.findAll(spec, pageable);

	    log.debug("Navigazione: cerco indice {}, trovato ID {}", index, 
	             result.hasContent() ? result.getContent().get(0).getId() : "NULL");
	    
	    // 4. Restituiamo l'ID se trovato
	    return result.getContent().stream()
	                 .map(Film::getId)
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
	public FilmDto findById(Long id) {
		/*
		 * Optional<Film> opt = repo.findById(id); if (opt.isEmpty()) {
		 * log.warn("Film file with id {} not found", id); return new FilmDto(); //
		 * ritorna un DTO vuoto se non trovato }
		*  var dtoOpt = opt.map(mapper::toDto);
		 */
		return repo.findById(id).map(entity -> mapper.toDto(entity,prefixProvider))
				.orElse(new FilmDto());
	}

	@Override
	@Transactional
	public FilmDto save(FilmDto dto) throws RuntimeException {
		
		 // Cerchiamo un eventuale record già presente con gli stessi criteri
	    Optional<Film> esistente = repo.findByNomeAndRegistaAndAnno(
	            dto.getNome(), dto.getRegista(), dto.getAnno());
		
	    // Controllo per evitare di salvare un duplicato: se esiste già un film con lo stesso titolo, regista e anno, 
	    // e l'ID del DTO è null (nuovo) o diverso da quello trovato (modifica), lanciamo un'eccezione 
	    if (esistente.isPresent()) {
	        // Se l'ID del DTO è null (Nuovo) o se l'ID è diverso da quello trovato (Modifica con dati di un altro)
	        if (dto.getId() == null || !esistente.get().getId().equals(dto.getId())) {
	          log.error("Attenzione: il film '{}' è già presente in archivio con ID {}", dto.getNome(), esistente.get().getId());
	        	throw new RuntimeException ("Attenzione: il film '" + dto.getNome() + "' è già presente in archivio.");
	        }
	    }
		
	    Film saved =mapper.toEntity(dto,prefixProvider);
	    saved = repo.save(saved);
		log.info("Save New Film file. Id {}", saved.getId());

		return dto;
	}

	@Override
	public void delete(Long id) {
		repo.findById(id).ifPresent(film -> {
			film.setCancelled(true);
			repo.save(film);
			log.info("Cancelled Film file {} with success", id);
		});
	}

	@Override
	public void recovery(Long id) {
		repo.findById(id).ifPresent(film -> {
			film.setCancelled(false);
			repo.save(film);
			log.info("Recovery Film file {} with success", id);
		});
	}

}
