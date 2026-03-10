package it.catalog.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.catalog.persistence.entity.AudioFile;

@Repository
public interface AudioRepository extends JpaRepository<AudioFile, Long>,JpaSpecificationExecutor<AudioFile> {
	
//	@Query("""
//	        SELECT a FROM AudioFile a
//	        WHERE (:text IS NULL OR :text = '' OR
//	          LOWER(a.title) LIKE LOWER(CONCAT('%', :text, '%')) OR
//	          LOWER(a.filename) LIKE LOWER(CONCAT('%', :text, '%')) OR
//	          LOWER(a.mimeType) LIKE LOWER(CONCAT('%', :text, '%')) OR
//	          LOWER(a.autore) LIKE LOWER(CONCAT('%', :text, '%')))
//	        """)
//	Page<AudioFile> search(@Param("text") String text, Pageable pageable);
	
	@Override
	@EntityGraph(attributePaths = {
		    "tags",
		    "tags.tag"
		})
	Page<AudioFile> findAll(Specification<AudioFile> spec, Pageable pageable);
	
	
	
	@EntityGraph(attributePaths = {
		    "tags",
		    "tags.tag"
		})
	Optional<AudioFile> findById(Long id);
	
}