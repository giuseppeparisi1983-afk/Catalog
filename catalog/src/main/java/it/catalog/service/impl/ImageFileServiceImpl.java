package it.catalog.service.impl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.catalog.persistence.entity.ImageFile;
import it.catalog.persistence.repository.ImageRepository;
import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.ImageFileMapper;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageFileServiceImpl implements SearchService<ImageDto, DtoFilter>{

    private final ImageRepository repo;
    private final ImageFileMapper mapper;
    private final TagService tagService;

    private final SpecificationFactory<ImageFile> specFactory;
    
    public ImageFileServiceImpl(ImageRepository repo, ImageFileMapper mapper,TagService tagService
    		,SpecificationFactory<ImageFile> specFactory) {
        this.repo = repo;
        this.mapper = mapper;
        this.tagService= tagService;
        this.specFactory=specFactory;
    }
    
	 @Override
	 /**query fatta sulla index per avere tutti i tags dell'oggetto immagine */
  public List<TagDto> getAllTags() {
		 return tagService.findByTipoOggetto("Immagini");
  }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ImageDto> findPage(Pageable pageable,DtoFilter filter) {
    	
    	 // 1. Creiamo la specifica basata sul filtro ricevuto dalla UI
	    Specification<ImageFile> spec = specFactory.build("Immagini", filter);
   	
	    Page<ImageFile> entityPage =repo.findAll(spec, pageable);
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
    /**richiamato dalla pagina del form per aggiunta o modifica di un nuovo item* */
    public ImageDto findById(Long id) {
        var dtoOpt = repo.findById(id).map(mapper::toDto);
        return dtoOpt.orElse(new ImageDto());
    }

    @Override
    @Transactional
    public ImageDto save(ImageDto dto) {
        ImageFile saved = repo.save(mapper.toEntity(dto));
        log.info("Save New Image file. Id {}",saved.getId());
     
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

