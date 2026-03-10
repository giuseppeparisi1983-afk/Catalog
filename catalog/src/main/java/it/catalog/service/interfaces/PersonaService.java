package it.catalog.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import it.catalog.service.dto.PersonaDto;
import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.DtoFilter_;

public interface PersonaService {

    PersonaDto crea(PersonaDto dto);
    PersonaDto modifica(Long id, PersonaDto dto);
    PersonaDto trovaPerId(Long id);
    List<PersonaDto> trovaTuttiAttivi();
    public List<PersonaDto> trovaTutti(Pageable pageable);
    // findByField
    public Page<PersonaDto> searchByField(String field, String value, int page, int size, Sort sort);
    void cancella(Long id);
    public Page<PersonaDto> trovaConFiltro(DtoFilter_ filtro, Pageable pageable, List<String> filtroTags);
    public List<TagDto> getAllTagsForPersona();
    public List<TagDto> getAllTagsById(Long id) ;
}

