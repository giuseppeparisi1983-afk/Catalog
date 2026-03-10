package it.catalog.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.catalog.persistence.entity.OggettoTag;
import it.catalog.persistence.entity.OggettoTagId;
import it.catalog.persistence.entity.Tag;

public interface OggettoTagRepository extends JpaRepository<OggettoTag, OggettoTagId>, JpaSpecificationExecutor<OggettoTag> {
   

    @Query("""
            SELECT ot FROM OggettoTag ot
            JOIN ot.tag t
            WHERE t.tipoOggetto = :tipoOggetto AND ot.idOggetto=:idOggetto
        """)
	List<OggettoTag> findByTipoOggettoAndIdOggetto(String tipoOggetto, Long idOggetto);
//    
//    @Query("""
//            SELECT ot FROM OggettoTag ot
//            JOIN ot.tag t
//            WHERE ot.tipoOggetto = :tipoOggetto AND t.nomeTag IN :tagNames
//        """)
//        List<OggettoTag> findByTipoOggettoAndTagNomeIn(@Param("tipoOggetto") String tipoOggetto,
//                                                       @Param("tagNames") List<String> tagNames);
    
//    void deleteByTipoOggettoAndIdOggetto(String tipoOggetto, Long idOggetto);
//    
//    void deleteByTipoOggettoAndIdOggettoAndTag(String tipoOggetto, Long idOggetto, Tag tag);
	
		void deleteByIdOggettoAndTag(Long idOggetto, Tag tag);
    
//    List<OggettoTag> findByTipoOggetto(String tipoOggetto);

}

