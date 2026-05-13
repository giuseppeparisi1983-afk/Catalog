package it.catalog.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import it.catalog.persistence.entity.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long>, JpaSpecificationExecutor<Documento> {

	@Query(value = "SELECT DISTINCT categoria FROM documenti", nativeQuery = true)
	List<String> findDistinctCategoria();

    @Query("SELECT DISTINCT e.stato FROM Documento e")
	List<String> findDistinctStato();

    @EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Page<Documento> findAll(Specification<Documento> spec, Pageable pageable);
    
    @EntityGraph(attributePaths = {"tags"})
    @Override
    Optional<Documento> findById(Long id);
    
}

