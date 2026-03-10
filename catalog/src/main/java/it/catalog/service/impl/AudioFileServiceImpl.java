package it.catalog.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.catalog.persistence.entity.AudioFile;
import it.catalog.persistence.repository.AudioRepository;
import it.catalog.service.dto.AudioDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.AudioFileMapper;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AudioFileServiceImpl implements SearchService<AudioDto, DtoFilter> {

	private final AudioRepository repo;
	private final AudioFileMapper mapper;
	private final TagService tagService;

	private final SpecificationFactory<AudioFile> specFactory;

	public AudioFileServiceImpl(AudioRepository repo, AudioFileMapper mapper, TagService tagService,
			SpecificationFactory<AudioFile> specFactory) {
		this.repo = repo;
		this.mapper = mapper;
		this.tagService = tagService;
		this.specFactory = specFactory;
	}

	@Override
	/** query fatta sulla index per avere tutti i tags dell'oggetto audio */
	public List<TagDto> getAllTags() {
		return tagService.findByTipoOggetto("Audio");
	}

	@Override
	public Page<AudioDto> findPage(DtoFilter filtro, Pageable pageable) {

		Specification<AudioFile> spec = (root, query, cb) -> cb.conjunction();

//		if (filtro != null && filtro.getCriterion() != null) {
//            try {
//           	 Field field = AudioDto.class.getDeclaredField(filtro.getCampo());
//                Class<?> type = field.getType();

			spec = specFactory.build("Audio", filtro);
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

//		Page<AudioFile> entityPage = repo.findAll(spec, pageable);
		Page<AudioFile> entityPage = repo.findAll(pageable);
		if (entityPage.isEmpty()) { // caso Not Found
			return new PageImpl<>(Collections.emptyList(), pageable, 0); 
		}
		
		return mapper.toDtoPage(entityPage);

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
	
	@Override
	public long count(DtoFilter filter) {
		Specification<AudioFile> spec = (root, query, cb) -> cb.conjunction();

		spec = specFactory.build("Audio", filter);
		return repo.count(spec);
	}
	

	@Override
	/**
	 * richiamato dalla pagina del form per aggiunta o modifica di un nuovo item*
	 */
	public AudioDto findById(Long id) {
		var dtoOpt = repo.findById(id).map(mapper::toDto);
		dtoOpt.ifPresent(dto -> dto.setTags(tagService.findTagsByObject("Audio", dto.getId())));
		return dtoOpt.orElse(new AudioDto());
	}

	@Override
	public AudioDto save(AudioDto dto) {
		AudioFile saved = repo.save(mapper.toEntity(dto));
		log.info("Save New Audio file");
		// Update tag relations
		List<String> tagNames = dto.getTags() != null
				? dto.getTags().stream().map(TagDto::getNomeTag).collect(Collectors.toList())
				: Collections.EMPTY_LIST;

		tagService.upsertTagsForObject("Audio", saved.getId(), tagNames);
		log.info("Update Tags list for Audio file {}", saved.getId());

		AudioDto res = mapper.toDto(saved);
		res.setTags(dto.getTags());
		return res;
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
