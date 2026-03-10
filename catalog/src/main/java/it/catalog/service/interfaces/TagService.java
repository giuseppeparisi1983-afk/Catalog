package it.catalog.service.interfaces;

import java.util.List;
import java.util.Optional;

import it.catalog.persistence.entity.OggettoTag;
import it.catalog.service.dto.TagDto;

public interface TagService {
    
	public List<TagDto> findTagsByObject(String tipoOggetto, Long idOggetto);
	public List<OggettoTag> findObjectTagByObject(String tipoOggetto, Long idOggetto);
	//public void saveTagsForObject(String tipoOggetto, Long idOggetto, List<String> nomiTag);
	public void upsertTagsForObject(String tipoOggetto, Long idOggetto, List<String> desiredTagNames);
	public List<TagDto> findByTipoOggetto(String tipo);
//	TagDto create(TagDto dto);
//    Optional<TagDto> findById(Long id);
//    List<TagDto> findAll();
}

