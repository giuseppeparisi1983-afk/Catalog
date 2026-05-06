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

import it.catalog.persistence.entity.Chitarra;
import it.catalog.persistence.repository.ChitarraRepository;
import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.GuitarMapper;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GuitarServiceImpl implements SearchService<GuitarDto, DtoFilter>{

    private final ChitarraRepository repository;
    private final SpecificationFactory<Chitarra> specificationFactory;
    private final TagService tagService;
    private final GuitarMapper mapper;

    public GuitarServiceImpl(ChitarraRepository chitarraRepository,GuitarMapper mapper,TagService tagService,SpecificationFactory<Chitarra> specificationFactory) {
        this.repository = chitarraRepository;
        this.mapper=mapper;
        this.tagService = tagService;
        this.specificationFactory = specificationFactory;
    }

    @Override
	/** query fatta sulla index per avere tutti i tags dell'oggetto video */
	public List<TagDto> getAllTags() {
		return tagService.findByTipoOggetto("Video-Chitarra");
	}
    
    @Override
    public GuitarDto findById(Long id) {
        Optional<Chitarra> entity= repository.findByVideoId(id);
        return mapper.toDto(entity.get());
    }

    @Override
	public long count() {

		return repository.count();		 

	}
    
    @Override
    @Transactional
    public GuitarDto save(GuitarDto chitarra) {
       
    	Chitarra entity=repository.save(mapper.toEntity(chitarra));
    	log.info("Saved Video-Chitarra file {} with success.", entity.getId());
    	
    	return chitarra;
    }

    @Override
    public void delete(Long id) {
        repository.findByVideoId(id).ifPresent(c -> {
            c.getVideo().setCancelled(true);
            repository.save(c);
        });
    }

    @Override
    public void recovery(Long id) {
    	repository.findByVideoId(id).ifPresent(c -> {
    		 c.getVideo().setCancelled(false);
    		repository.save(c);
    	});
    }

//    @Override
//    public List<Chitarra> searchByAutore(String autore) {
//        return repository.findByAutoreContainingIgnoreCaseAndVideoCancelledFalse(autore);
//    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<GuitarDto> findPage(Pageable pageable,DtoFilter filter) {

        Specification<Chitarra> spec = specificationFactory.build(filter);

        Page<Chitarra> result=repository.findAll(spec, pageable);
//        return result.map(mapper::toDtoList);
        
    	if (result.isEmpty()) { // caso Not Found
			return new PageImpl<>(Collections.emptyList(), pageable, 0); 
		}
        
        return result.map(entity -> mapper.toDto(entity));
    }
}