package it.catalog.service.dto;

import java.time.Instant;
import java.util.Set;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentoDto {

	 private Long id;
	    private String nome;
	    private String path;
	    private Double dimensione;
	    private String autore;
	    private String descrizione;
	    private TipoDocumento categoria;
	    private String lingua;
	    private Integer versione;
	    private StatiDocumento stato;
	    private String origine;
	    private boolean preferito; 
	    private Integer rating; 
	    private long visualizzazioni; 
	    private boolean backup;
	    private String note;
	    private String estensione;
	    private Instant lastView; 
	    private Instant dataArchiviazione; 
	    private Instant lastUpdate; 
	    
	 // aggiungiamo i tag direttamente nel DTO
	    private Set <TagDto> tags;
}
