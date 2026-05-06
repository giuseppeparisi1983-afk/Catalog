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
	
	@Override
	 @Transactional(readOnly = true)
	public Page<VideoDto> findPage(Pageable pageable,DtoFilter filter) {
		
	    // 1. Creiamo la specifica basata sul filtro ricevuto dalla UI
	    Specification<Video> spec = specificationFactory.build(filter);

	 // 2. Eseguiamo la query filtrata e paginata
	    Page<Video> entityPage = repository.findAll(spec, pageable);
	    
		if (entityPage.isEmpty()) { // caso Not Found
			return new PageImpl<>(Collections.emptyList(), pageable, 0); 
		}

		return mapper.toDtoPage(entityPage);

	}
	
	@Override
	public long count() {

		return repository.count();		 

	}

//    @Override
//    public List<String> getCategorie() {
//    	return repository.findDistinctCategoria();
//    }

    @Override
    public VideoDto findById(Long id) {
		var dtoOpt = repository.findById(id).map(entity -> mapper.toDto(entity, prefixProvider));
		
		return dtoOpt.orElse(new VideoDto());
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
    	
		/*
		 * if (video.getRating() == null) { video.setRating(0.0); }
		 */
    	
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
