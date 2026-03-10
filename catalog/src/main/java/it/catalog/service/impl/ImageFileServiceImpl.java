package it.catalog.service.impl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.catalog.persistence.entity.ImageFile;
import it.catalog.persistence.repository.ImageRepository;
import it.catalog.service.dto.ImageDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.ImageFileService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.ImageFileMapper;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageFileServiceImpl implements ImageFileService{

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
  public List<TagDto> getAllTagsForImage() {
		 return tagService.findByTipoOggetto("Immagini");
  }
    
    @Override
    public Page<ImageDto> findPage(DtoFilter_ filtro, Pageable pageable, List<String> requiredTags) {
       
    	Specification<ImageFile> spec = (root, query, cb) -> cb.conjunction();
    	
    	if (filtro != null && filtro.getCampo() != null && filtro.getValore() != null) {
            try {
           	 Field field = ImageDto.class.getDeclaredField(filtro.getCampo());
                Class<?> type = field.getType();
           	 
           	spec = specFactory.buildOld(
               filtro.getCampo(),
               filtro.getValore(),
               type
           );
           	 // 2) Tag filter
           	if(!requiredTags.isEmpty())
               spec = spec.and(specFactory.withTag("Immagini",requiredTags));
               
            } catch (NoSuchFieldException e) {
                // Campo non valido, ignora il filtro
           	 log.error("Campo non valido",e);
            }
   	 }
   	
	    Page<ImageFile> entityPage =repo.findAll(spec, pageable);
	    if (entityPage.isEmpty()) {
	        return new PageImpl<>(Collections.emptyList(), pageable, 0); // caso Not Found
	    }
	    
	    return entityPage.map(entity -> {
	    	
	    	ImageDto dto=mapper.toDto(entity);
	    		
	    	dto.setTags(tagService.findTagsByObject("Immagini", entity.getId()));
	    	
	    	return dto;
	    		
	    });

    }

    @Override
    /**richiamato dalla pagina del form per aggiunta o modifica di un nuovo item* */
    public ImageDto findById(Long id) {
        var dtoOpt = repo.findById(id).map(mapper::toDto);
        dtoOpt.ifPresent(dto -> dto.setTags(tagService.findTagsByObject("Immagini", dto.getId())));
        return dtoOpt.orElse(new ImageDto());
    }

    @Override
    public ImageDto save(ImageDto dto) {
        ImageFile saved = repo.save(mapper.toEntity(dto));
        // Update tag relations
        List<String> tagNames=dto.getTags() != null ? dto.getTags().stream() 
        		.map(TagDto::getNomeTag).collect(Collectors.toList()) : Collections.EMPTY_LIST;
        
        tagService.upsertTagsForObject("Immagini", saved.getId(),  tagNames);
        log.info("Update Tags list for Image file {}",saved.getId());
        ImageDto res = mapper.toDto(saved);
        res.setTags(dto.getTags());
        return res;
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

