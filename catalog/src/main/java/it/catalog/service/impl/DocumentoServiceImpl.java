package it.catalog.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.persistence.entity.Documento;
import it.catalog.persistence.repository.DocumentoRepository;
import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter;
import it.catalog.service.interfaces.SearchService;
import it.catalog.service.interfaces.TagService;
import it.catalog.service.mapper.DocumentoMapper;
import it.catalog.utility.PathPrefixProvider;
import it.catalog.utility.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoServiceImpl implements SearchService<DocumentoDto, DtoFilter> {

    private final DocumentoRepository repository;
    private final DocumentoMapper mapper;
    private final TagService tagService;
    
    private final PathPrefixProvider prefixProvider;
    private final SpecificationFactory<Documento> specificationFactory;
    
    public DocumentoServiceImpl(DocumentoRepository documentoRepository, 
    		DocumentoMapper documentoMapper,SpecificationFactory<Documento> specificationFactory,
    		PathPrefixProvider prefixProvider,TagService tagService) {
        this.repository = documentoRepository;
        this.mapper = documentoMapper;
        this.specificationFactory = specificationFactory;
       
        this.prefixProvider = prefixProvider;
        this.tagService= tagService;
    }

    
    @Override
    /**query fatta sulla index per avere tutti i tags dell'oggetto Documenti */
    public List<TagDto> getAllTags() {
    	return tagService.findByTipoOggetto("Documento");
    }  
    
    @Override
    public DocumentoDto findById(Long id) {
         	
    	var dtoOpt = repository.findById(id).map(entity -> mapper.toDto(entity,prefixProvider));   
        return dtoOpt.orElse(new DocumentoDto());
    }
    
    @Override
    @Transactional
    public DocumentoDto save(DocumentoDto dto) {
        Documento saved = repository.save(mapper.toEntity(dto,prefixProvider));
        log.info("Save New Document file. Id {}",saved.getIdDocumento());
        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoDto> findPage(Pageable pageable,DtoFilter filter) {
        
    	Specification<Documento> spec = specificationFactory.build(filter);     
    
	    Page<Documento> result =repository.findAll(spec, pageable);
	    if (result.isEmpty()) {
	        return new PageImpl<>(Collections.emptyList(), pageable, 0); // caso Not Found
	    }
	    
	    return mapper.toDtoPage(result);
 
    }
    
	@Override
	public long count() {

		return repository.count();		 
	}
	
    @Override
    public void delete(Long id) {       
        repository.findById(id).ifPresent(doc -> {
        	doc.setStato(StatiDocumento.ELIMINATO);
        	repository.save(doc);
            log.info("Cancelled Document idFile {} with success",id);
        });
    }

    @Override
    public void recovery(Long id) {
    	repository.findById(id).ifPresent(doc -> {
        	doc.setStato(StatiDocumento.ATTIVO);
        	repository.save(doc);
            log.info("Recovery Document idFile {} with success",id);
        });
    	
    }
    
}

