package it.catalog.ui.utility;

import java.util.List;

import it.catalog.service.dto.TagDto;
import it.catalog.service.dto.search.SearchCriterion;

//Interfaccia per i filtri DTO
public interface BaseFilter {
    void setCriterion(SearchCriterion criterion);
    void setTags(List<TagDto> tags);
}
