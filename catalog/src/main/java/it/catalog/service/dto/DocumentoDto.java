package it.catalog.service.dto;

import java.time.LocalDateTime;
import java.util.List;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentoDto {

	 private Long idDocumento;
	    private String nome;
	    private String path;
	    private Long dimensione;
	    private LocalDateTime dataCreazione;
	    private LocalDateTime lastUpdate;
	    private LocalDateTime lastView;
	    private String autore;
	    private String descrizione;
	    private TipoDocumento categoria;
	    private String lingua;
	    private Integer versione;
	    private StatiDocumento stato;
	    private String origine;
	    private String note;
	    private String estensione;
	    
	 // aggiungiamo i tag direttamente nel DTO
	    private List<TagDto> tags;
}
