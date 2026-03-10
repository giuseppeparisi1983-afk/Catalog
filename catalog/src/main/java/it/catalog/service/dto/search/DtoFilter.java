package it.catalog.service.dto.search;

import java.util.List;

import it.catalog.service.dto.TagDto;
import it.catalog.ui.utility.BaseFilter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoFilter implements BaseFilter {

	private SearchCriterion criterion;
	private List<TagDto> tags;
	
	 @Override
	    public void setCriterion(SearchCriterion criterion) {
	        this.criterion = criterion;
	    }

	    @Override
	    public void setTags(List<TagDto> tags) {
	        this.tags = tags;
	    }

	    
	    public SearchCriterion getCriterion() { return criterion; }
	    public List<TagDto> getTags() { return tags; }
	
	
	
}
