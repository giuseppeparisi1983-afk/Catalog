package it.catalog.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import it.catalog.persistence.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    
	Optional<Tag> findByNomeTag(String nomeTag);
	List<Tag> findByTipoOggetto(String tipoOggetto);
	
	
	 @Query("""
	            SELECT t FROM OggettoTag ot
	            JOIN ot.tag t
	            WHERE t.tipoOggetto = :tipoOggetto AND t.idTag =ot.tag.idTag and ot.idOggetto=:idOggetto
	        """)
	List<Tag> findByTipoOggettoAndIdOggetto(String tipoOggetto, Long idOggetto);

	 
//	 List<Tag> findByTipoOggettoAndTags(String tipoOggetto, List<String> tagNames);

}
