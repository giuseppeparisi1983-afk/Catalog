package it.catalog.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.catalog.service.dto.TagDto;

//Interfaccia generica per i Service
public interface SearchService<T, F> {

//	long count(F filter);
	
	long count();

	Page<T> findPage(Pageable pageable,F filter);

//	long count(Pageable pageable);

	List<TagDto> getAllTags();

	void delete(Long id);

	void recovery(Long id);

	T findById(Long id);

	T save(T dto);
}
