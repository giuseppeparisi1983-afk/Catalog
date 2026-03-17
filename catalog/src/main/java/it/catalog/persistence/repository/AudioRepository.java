package it.catalog.persistence.repository;

import java.util.List;
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
//	@EntityGraph(attributePaths = {
//		    "tags",
//		    "tags.tag"
//		})
	@EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Page<AudioFile> findAll(Specification<AudioFile> spec, Pageable pageable);
	
	
	
	 @EntityGraph(attributePaths = {"tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Optional<AudioFile> findById(Long id);

	
//	@Query("SELECT DISTINCT a FROM AudioFile a ")
//	 Page<AudioFile> findAllAudio(Pageable pageable);
	

	// tutti gli audio con eventuali tag (tipo_oggetto = 'audio')
//    @Query("""
//        SELECT DISTINCT a
//        FROM AudioFile a
//        LEFT JOIN Tag t ON t.idTag = ot.id.idTag
//        WHERE t.tipoOggetto = 'Audio' OR t.tipoOggetto IS NULL
//        """)
//    Page<AudioFile> findAllWithTags(Pageable pageable);
//
//
//    // audio filtrati per singolo tag
//    @Query("""
//        SELECT DISTINCT a
//        FROM AudioFile a
//        JOIN Tag t ON t.idTag = ot.id.idTag
//        WHERE t.tipoOggetto = 'Audio'
//          AND t.nomeTag = :tag
//        """)
//    List<AudioFile> findByTag(@Param("tag") String tag, Pageable pageable);
//
//
//    // audio filtrati per lista di tag (IN)
//    @Query("""
//        SELECT DISTINCT a
//        FROM AudioFile a
//        JOIN Tag t ON t.idTag = ot.id.idTag
//        WHERE t.tipoOggetto = 'Audio'
//          AND t.nomeTag IN :tags
//        """)
//    List<AudioFile> findByTags(@Param("tags") List<String> tags, Pageable pageable);
	
}