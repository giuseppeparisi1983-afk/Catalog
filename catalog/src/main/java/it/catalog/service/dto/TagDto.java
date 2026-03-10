package it.catalog.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDto {
    private Long idTag;
    private String nomeTag;
    private String tipoOggetto;
    
    public TagDto(String nomeTag) {
		super();
		this.nomeTag = nomeTag;
	}

	public TagDto(String nomeTag, String tipoOggetto) {
		super();
		this.nomeTag = nomeTag;
		this.tipoOggetto = tipoOggetto;
	}

	public TagDto(Long idTag, String nomeTag, String tipoOggetto) {
		super();
		this.idTag = idTag;
		this.nomeTag = nomeTag;
		this.tipoOggetto = tipoOggetto;
	}
     
}
