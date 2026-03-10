package it.catalog.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.catalog.persistence.entity.Video;
import it.catalog.persistence.repository.VideoRepository;
import it.catalog.service.dto.VideoDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.IVideoService;
import it.catalog.service.mapper.VideoMapper;
import it.catalog.utility.PathPrefixProvider;
import it.catalog.utility.SpecificationFactory;

@Service
public class VideoService implements IVideoService{

	 @Autowired
	    private VideoRepository repository;

	    @Autowired
	    private VideoMapper mapper;

	    @Autowired
	    private PathPrefixProvider prefixProvider;

	    @Autowired
	    private SpecificationFactory<Video> specificationFactory;
	/*
	 * public VideoService(VideoRepository videoRepository) { this.videoRepository =
	 * videoRepository; }
	 */

    @Override
    public List<VideoDto> findAll() {
    	//return repository.findAll();
//        return videoRepository.findByCancelledFalse();
    	
    	List<Video> list = repository.findAll();
           
           return mapper.toDtoList(list);

    }

    @Override
    public List<String> getCategorie() {
    	return repository.findDistinctCategoria();
//        return videoRepository.findByCancelledFalse();
    }

    @Override
    public Optional<VideoDto> findById(Integer id) {
    	
    	Optional<Video> entity=repository.findById(id);
    	
    	if(entity.isPresent())
    	
    	return Optional.of(mapper.toDto(entity.get(),prefixProvider));
    	
    	else
    		
    		return Optional.empty();
}
    
//    @Override
//    public VideoDto findByVideoId(Integer id) {
//    	
//    	
//    	return repository.findById(id)
//    			.map(mapper::toDto);
//    }

    @Override
    public VideoDto save(VideoDto video) {
    	
    	if (video.getRating() == null) {
    		video.setRating(0.0);
    	}
    	
    	Video saved = repository.save(mapper.toEntity(video,prefixProvider));
    	 return mapper.toDto(saved,prefixProvider);
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(video -> {
            video.setCancelled(true);
            repository.save(video);
        });
    }

    @Override
    public void recovery(Integer id) {
    	repository.findById(id).ifPresent(video -> {
    		video.setCancelled(false);
    		repository.save(video);
    	});
    }

    @Override
    public List<Video> searchByTitolo(String query) {    	
    	return repository.findByTitoloContainingIgnoreCaseAndCancelledFalse(query);
    }
    
    
    @Override
    public Page<VideoDto> getVideoByTitolo(String titolo, Pageable pageable){
         Page<Video> result = repository.findByTitoloContainingIgnoreCase(titolo, pageable);
         
         return result.map(entity -> mapper.toDto(entity, prefixProvider));
         
//    	return videoRepository.findByTitoloContainingIgnoreCase(titolo,pageable);
    	
    }

    
    @Override
    public Page<VideoDto> getVideos(String search, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Video> result = repository.findByTitoloContainingIgnoreCase(search, pageable);
//        return result.map(mapper::toDto);
        return result.map(entity -> mapper.toDto(entity, prefixProvider));
    }
    
    @Override
	public long getNumVideoByTitolo(String titolo) {
	
    	return repository.countByTitoloContainingIgnoreCase(titolo);
	}
        
	@Override
	public Page<VideoDto> searchByField(DtoFilter_ filtro, int page, int size, Sort sort) {
		Pageable pageable = PageRequest.of(page, size, sort);

		Specification<Video> spec = (root, query, cb) -> cb.conjunction();

		if (filtro != null && filtro.getCampo() != null && filtro.getValore() != null) {
			try {
				Field field = VideoDto.class.getDeclaredField(filtro.getCampo());
				Class<?> type = field.getType();

				spec = specificationFactory.buildOld(filtro.getCampo(), filtro.getValore(), type);
			} catch (NoSuchFieldException e) {
				// Campo non valido, ignora il filtro
			}
		}
		Page<Video> result = repository.findAll(spec, pageable);
		return result.map(entity -> mapper.toDto(entity, prefixProvider));
	}

}
