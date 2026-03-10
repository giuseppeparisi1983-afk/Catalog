package it.catalog.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import it.catalog.persistence.entity.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long>, JpaSpecificationExecutor<Documento> {

	@Query(value = "SELECT DISTINCT categoria FROM documenti", nativeQuery = true)
	List<String> findDistinctCategoria();

    @Query("SELECT DISTINCT e.stato FROM Documento e")
	List<String> findDistinctStato();


}

