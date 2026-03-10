package it.catalog.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.catalog.service.dto.TagDto;

//Interfaccia generica per i Service
public interface SearchService<T, F> {
	Page<T> findPage(F filter, Pageable pageable);

	long count(F filter);

	List<TagDto> getAllTags();

	void delete(Long id);

	void recovery(Long id);

	T findById(Long id);

	T save(T dto);
}
