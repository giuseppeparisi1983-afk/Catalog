package it.catalog.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.catalog.persistence.entity.Film;

public interface FilmRepository extends JpaRepository<Film, Long>,JpaSpecificationExecutor<Film>{

	
	
	@Override
	@EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Page<Film> findAll(Specification<Film> spec, Pageable pageable);
	
	
	@Override
	@EntityGraph(attributePaths = {"tags"})
	Optional<Film> findById(Long id);
	
	
}
