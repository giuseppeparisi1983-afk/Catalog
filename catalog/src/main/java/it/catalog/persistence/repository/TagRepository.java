package it.catalog.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.catalog.persistence.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    
	Optional<Tag> findByNomeTag(String nomeTag);
	List<Tag> findByTipoOggetto(String tipoOggetto);
	
	
	 @Query(value="SELECT t.* FROM tag t left JOIN Oggetto_tag ot on t.id_tag =ot.id_tag where t.tipo_Oggetto = :tipoOggetto and ot.id_oggetto=:idOggetto",nativeQuery = true)
	List<Tag> findByTipoOggettoAndIdOggetto(@Param("tipoOggetto") String tipoOggetto,  @Param("idOggetto")Long idOggetto);

	 
//	 List<Tag> findByTipoOggettoAndTags(String tipoOggetto, List<String> tagNames);

}
