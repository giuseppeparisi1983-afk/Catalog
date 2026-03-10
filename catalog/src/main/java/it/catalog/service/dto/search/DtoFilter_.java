package it.catalog.service.dto.search;

import java.util.List;

import it.catalog.service.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoFilter_ {

	private String campo; // es. "nome", "cognome"
	private String valore; // es. "Mario"
	private List<TagDto> tags;
}
