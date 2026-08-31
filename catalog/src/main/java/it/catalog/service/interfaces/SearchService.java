package it.catalog.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import it.catalog.service.dto.TagDto;

//Interfaccia generica per i Service
public interface SearchService<T, F> {

	   // Restituisce il conteggio totale per sapere quando disabilitare le freccette
    long count(F filter);
	
    // Restituisce l'ID dell'oggetto alla posizione 'index' (0-based)
    // considerando il filtro e l'ordinamento correnti
    Optional<Long> findIdAtPosition(F filter, Sort sort, int index);
	
//	long count();

	Page<T> findPage(Pageable pageable,F filter);

//	long count(Pageable pageable);

	List<TagDto> getAllTags();

	void delete(Long id);

	void recovery(Long id);

	T findById(Long id);

	T save(T dto) throws RuntimeException;
}
