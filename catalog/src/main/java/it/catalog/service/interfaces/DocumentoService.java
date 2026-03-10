package it.catalog.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.catalog.service.dto.DocumentoDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;

public interface DocumentoService {
	
	public List<TagDto> getAllTagsForDoc();
	public DocumentoDto trovaPerId(Long id);
	DocumentoDto create(DocumentoDto dto);
//    List<DocumentoDto> search(Map<String, Object> filters);
    public Page<DocumentoDto> search(DtoFilter_ filtro,  List<String> tagNames, Pageable pageable);
    public void cancella(Long id);
    public void recovery(Long id);
}

