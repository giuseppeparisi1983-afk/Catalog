package it.catalog.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import it.catalog.common.enums.TipoDocumento;
import it.catalog.persistence.entity.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long>, JpaSpecificationExecutor<Documento> {

	@Query(value = "SELECT DISTINCT categoria FROM documenti", nativeQuery = true)
	List<String> findDistinctCategoria();

	@Query("SELECT DISTINCT e.stato FROM Documento e")
	List<String> findDistinctStato();

//	// Usato nella grid della Index per mostrare i dati velocemente con i tags inclusi
//	@EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
//	Page<Documento> findAll(Specification<Documento> spec, Pageable pageable);
//
//	// Usato solo per trovare l'ID alla posizione X senza caricare collezioni
//	// Usiamo una proiezione o una query che ignora i fetch per evitare il warning HHH90003004
//	Page<Documento> findAllBy(Specification<Documento> spec, Pageable pageable);

	//Sovrascriviamo il findById standard per assicurarci che carichi i tags
	@EntityGraph(attributePaths = {"tags"})
	@Override
	Optional<Documento> findById(Long id);

	
	Optional<Documento> findByNomeAndAutoreAndCategoriaAndVersione(
			String nome, String autore,TipoDocumento categoria, Integer versione);
	
	
}

