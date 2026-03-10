package it.catalog.service.impl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.persistence.entity.Documento;
import it.catalog.persistence.repository.DocumentoRepository;
import it.catalog.persistence.repository.OggettoTagRepository;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.DocumentoService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.DocumentoMapper;
import it.catalog.utility.PathPrefixProvider;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoRepository repository;
    private final DocumentoMapper mapper;
    private final OggettoTagRepository oggettoTagRepository;
    private final TagService tagService;
    
    private final PathPrefixProvider prefixProvider;
    private final SpecificationFactory<Documento> specificationFactory;
    
    public DocumentoServiceImpl(DocumentoRepository documentoRepository, 
    		DocumentoMapper documentoMapper,SpecificationFactory<Documento> specificationFactory,
    		OggettoTagRepository oggettoTagRepository,PathPrefixProvider prefixProvider,TagService tagService) {
        this.repository = documentoRepository;
        this.mapper = documentoMapper;
        this.specificationFactory = specificationFactory;
        this.oggettoTagRepository = oggettoTagRepository;
        this.prefixProvider = prefixProvider;
        this.tagService= tagService;
    }

    
    @Override
    /**query fatta sulla index per avere tutti i tags dell'oggetto Documenti */
    public List<TagDto> getAllTagsForDoc() {
    	return tagService.findByTipoOggetto("Documenti");
    }
    
    
    
    @Override
    public DocumentoDto trovaPerId(Long id) {
         	
    	var dtoOpt = repository.findById(id).map(entity -> mapper.toDto(entity,prefixProvider));
        dtoOpt.ifPresent(dto -> dto.setTags(tagService.findTagsByObject("Documenti", dto.getIdDocumento())));
        return dtoOpt.orElse(new DocumentoDto());
    }
    
    @Override
    public DocumentoDto create(DocumentoDto dto) {
        Documento saved = repository.save(mapper.toEntity(dto,prefixProvider));
        		
        log.info("Success save new document {}",saved.getIdDocumento());
        List<String> tagNames = dto.getTags() != null ? 
        		dto.getTags().stream().map(TagDto::getNomeTag).toList()
        		: Collections.EMPTY_LIST;
        
        tagService.upsertTagsForObject("Documenti", saved.getIdDocumento(),  tagNames);
        log.info("Update Tags list for Image file {}",saved.getIdDocumento());
        DocumentoDto res = mapper.toDto(saved,prefixProvider);
        res.setTags(dto.getTags());
        return res;

    }

    @Override
    public Page<DocumentoDto> search(DtoFilter_ filtro,  List<String> tagNames, Pageable pageable) {
        
    	Specification<Documento> spec = (root, query, cb) -> cb.conjunction();     
    	
    	 if (filtro != null && filtro.getCampo() != null && filtro.getValore() != null) {
             try {
            	 Field field = DocumentoDto.class.getDeclaredField(filtro.getCampo());
                 Class<?> type = field.getType();
            	 
            	spec = specificationFactory.buildOld(
                filtro.getCampo(),
                filtro.getValore(),
                type
            );       
            	if (tagNames != null && !tagNames.isEmpty())
            		spec = spec.and(specificationFactory.withTag("Documenti", tagNames));
            	
            	
            	
             } catch (NoSuchFieldException e) {
                 // Campo non valido, ignora il filtro
            	 log.error("Campo non valido",e);
             }
    	 }

	    Page<Documento> result =repository.findAll(spec, pageable);
	    if (result.isEmpty()) {
	        return new PageImpl<>(Collections.emptyList(), pageable, 0); // caso Not Found
	    }
	    return result.map(entity -> {
	    	
	    	DocumentoDto dto=mapper.toDto(entity,prefixProvider);
	    		
	    	dto.setTags(tagService.findTagsByObject("Documenti", entity.getIdDocumento()));
	    	
	    	return dto;
	    		
	    });
	    
    }
    
    @Override
    public void cancella(Long id) {       
        repository.findById(id).ifPresent(doc -> {
        	doc.setStato(StatiDocumento.ELIMINATO);
        	repository.save(doc);
            log.info("Cancelled Document file {} with success",id);
        });
    }

    @Override
    public void recovery(Long id) {
    	repository.findById(id).ifPresent(doc -> {
        	doc.setStato(StatiDocumento.ATTIVO);
        	repository.save(doc);
            log.info("Cancelled Document file {} with success",id);
        });
    	
    }
    
}

