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

import it.catalog.persistence.entity.Chitarra;

@Repository
public interface ChitarraRepository extends JpaRepository<Chitarra, Integer>, JpaSpecificationExecutor<Chitarra> {

	List<Chitarra> findByVideoCancelledFalse();
	List<Chitarra> findByAutoreContainingIgnoreCaseAndVideoCancelledFalse(String autore);
	
	@EntityGraph(attributePaths = {"video.tags"}) 
	Optional<Chitarra> findByVideoId(Long videoId);
	
	@Override
	@EntityGraph(attributePaths = {"video.tags"}) // <--- Istruisce Hibernate a fare la JOIN solo per questo metodo
	Page<Chitarra> findAll(Specification<Chitarra> spec, Pageable pageable);
	
	Page<Chitarra> findByDifficoltaContainingIgnoreCase(String difficolta, Pageable pageable);
	Page<Chitarra> findByVideoTitoloContainingIgnoreCase(String titolo, Pageable pageable);
}
