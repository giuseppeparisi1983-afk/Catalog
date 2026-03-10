package it.catalog.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.catalog.persistence.entity.Chitarra;
import it.catalog.persistence.repository.ChitarraRepository;
import it.catalog.service.dto.GuitarDto;
import it.catalog.service.dto.search.DtoFilter_;
import it.catalog.service.interfaces.IGuitarService;
import it.catalog.service.mapper.GuitarMapper;
import it.catalog.utility.SpecificationFactory;

@Service
public class GuitarServiceImpl implements IGuitarService{

    private final ChitarraRepository repository;
    private final SpecificationFactory<Chitarra> specificationFactory;
    private final GuitarMapper mapper;

    public GuitarServiceImpl(ChitarraRepository chitarraRepository,GuitarMapper mapper,SpecificationFactory<Chitarra> specificationFactory) {
        this.repository = chitarraRepository;
        this.mapper=mapper;
        this.specificationFactory = specificationFactory;
    }

    @Override
    public List<GuitarDto> findAll() {    	
    	
    	List<Chitarra> list = new ArrayList<>();
    
    	list = repository.findAll();
    	
    	return mapper.toDtoList(list);
    
    }

    @Override
    public GuitarDto getGuitarById(Integer id) {
    	
    	  Optional<Chitarra> entity= repository.findById(id);
          
          if(entity.isPresent())
          	
          	return mapper.toDto(entity.get());
          
  	else
      		
      		return new GuitarDto();
    }
    
    @Override
    public Optional<GuitarDto> findByVideoId(Integer id) {
//        return chitarraRepository.findByVideo_Id(id).filter(c -> !c.getCancelled());
        Optional<Chitarra> entity= repository.findByVideoId(id);
        
        return Optional.of(mapper.toDto(entity.get()));
        
//        if(entity.isPresent())
//        	
//        	return Optional.of(mapper.toDto(entity.get()));
//        
//	else
//    		
//    		return Optional.empty();
        
    }

    @Override
    public Chitarra save(GuitarDto chitarra) {
       
    	Chitarra entity=mapper.toEntity(chitarra);
    	
    	return repository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        repository.findByVideoId(id).ifPresent(c -> {
            c.getVideo().setCancelled(true);
            repository.save(c);
        });
    }

    @Override
    public void recovery(Integer id) {
    	repository.findByVideoId(id).ifPresent(c -> {
    		 c.getVideo().setCancelled(false);
    		repository.save(c);
    	});
    }

    @Override
    public List<Chitarra> searchByAutore(String autore) {
        return repository.findByAutoreContainingIgnoreCaseAndVideoCancelledFalse(autore);
    }
    
    @Override
    public Page<GuitarDto> searchByField(DtoFilter_ filtro, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Chitarra> spec = (root, query, cb) -> cb.conjunction();  
        
        if (filtro != null && filtro.getCampo() != null && filtro.getValore() != null) {
            try {
           	 Field field = GuitarDto.class.getDeclaredField(filtro.getCampo());
                Class<?> type = field.getType();
           	 
           	spec = specificationFactory.buildOld(
               filtro.getCampo(),
               filtro.getValore(),
               type
           );            	
            } catch (NoSuchFieldException e) {
                // Campo non valido, ignora il filtro
            }
   	 }
        
//        switch (field) {
//            case "difficolta" -> result = repository.findByDifficoltaContainingIgnoreCase(value, pageable);
//            default -> result = repository.findByVideoTitoloContainingIgnoreCase(value, pageable);
//        }

        Page<Chitarra> result=repository.findAll(spec, pageable);
//        return result.map(mapper::toDtoList);
        return result.map(entity -> mapper.toDto(entity));
    }
}