package it.catalog.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.catalog.persistence.entity.Documento;
import it.catalog.persistence.entity.ImageFile;

@Repository
public interface ImageRepository extends JpaRepository<ImageFile, Long>,
JpaSpecificationExecutor<ImageFile>{
	
//	
//	@Query("""
//	        SELECT i FROM ImageFile i
//	        WHERE (:text IS NULL OR :text = '' OR
//	          LOWER(i.title) LIKE LOWER(CONCAT('%', :text, '%')) OR
//	          LOWER(i.filename) LIKE LOWER(CONCAT('%', :text, '%')) OR
//	          LOWER(i.mimeType) LIKE LOWER(CONCAT('%', :text, '%')))
//	        """)
//			Page<ImageFile> search(@Param("text") String text, Pageable pageable);
	
	
	
	@Override
	@EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Page<ImageFile> findAll(Specification<ImageFile> spec, Pageable pageable);
	
	
	@Override
	@EntityGraph(attributePaths = {"tags"})
	Optional<ImageFile> findById(Long id);
	
}
