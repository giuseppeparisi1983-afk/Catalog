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
import org.springframework.stereotype.Repository;

import it.catalog.persistence.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer>, JpaSpecificationExecutor<Video> {
	
//	List<Video> findAll();
	
	
	@Override
	@EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Page<Video> findAll(Specification<Video> spec, Pageable pageable);
	
	 @EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Optional<Video> findById(Long id);
	 
	@Query(value = "SELECT DISTINCT categoria FROM video", nativeQuery = true)
	List<String> findDistinctCategoria();
	
    List<Video> findByCancelledFalse();
    List<Video> findByTitoloContainingIgnoreCaseAndCancelledFalse(String titolo);
    
    Page<Video> findByTitoloContainingIgnoreCase(String titolo, Pageable pageable);
    long countByTitoloContainingIgnoreCase(String titolo);
    
    Page<Video> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);
    Page<Video> findByNoteContainingIgnoreCase(String note, Pageable pageable);

    
}
