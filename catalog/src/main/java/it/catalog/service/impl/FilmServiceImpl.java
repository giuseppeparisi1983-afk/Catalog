package it.catalog.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilmServiceImpl implements SearchService<FilmDto, DtoFilter> {

	private final FilmRepository repo;
	private final FilmMapper mapper;
	private final TagService tagService;

	private final SpecificationFactory<Film> specFactory;

	public FilmServiceImpl(FilmRepository repo, FilmMapper mapper, TagService tagService,
			SpecificationFactory<Film> specFactory) {
		super();
		this.repo = repo;
		this.mapper = mapper;
		this.tagService = tagService;
		this.specFactory = specFactory;
	}

	@Override
	/** query fatta sulla index per avere tutti i tags dell'oggetto immagine */
	public List<TagDto> getAllTags() {
		return tagService.findByTipoOggetto("Film");
	}

	@Override
	@Transactional(readOnly = true)
	public Page<FilmDto> findPage(Pageable pageable, DtoFilter filter) {

		// 1. Creiamo la specifica basata sul filtro ricevuto dalla UI
		Specification<Film> spec = specFactory.build(filter);

		Page<Film> entityPage = repo.findAll(spec, pageable);
		if (entityPage.isEmpty()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0); // caso Not Found
		}

		return mapper.toDtoPage(entityPage);

	}

	@Override
	public long count() {

		return repo.count();
	}

	@Override
	/**
	 * richiamato dalla pagina del form per aggiunta o modifica di un nuovo item*
	 */
	public FilmDto findById(Long id) {
		Optional<Film> opt = repo.findById(id);
		if (opt.isEmpty()) {
			log.warn("Film file with id {} not found", id);
			return new FilmDto(); // ritorna un DTO vuoto se non trovato
		}

		var dtoOpt = opt.map(mapper::toDto);
		return dtoOpt.orElse(new FilmDto());
	}

	@Override
	@Transactional
	public FilmDto save(FilmDto dto) {
		Film saved = repo.save(mapper.toEntity(dto));
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
