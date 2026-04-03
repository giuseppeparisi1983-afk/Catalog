package it.catalog.service.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilmDto {

	private Long id;
	private String nome;
	private String genere;
	private Double voto;
	private String regista;
	private String protagonisti; // elenco dei protagonisti come stringa (es. "Attore1, Attore2")
	private Integer annoUscita;
	private String durata; // formato "hh:mm"
	private String trama;
	private String trailer; // URL del trailer
	
	private String pathFile;
	private boolean preferito;
	private String filename;
	private String formato; // rappresentato come stringa (es. "MP4")
	private Long sizeBytes;
	private boolean cancelled;
	private Long visualizzazioni;
	private Instant dataArchiviazione; // formato "yyyy-MM-dd"
	private Instant lastView; // formato "yyyy-MM-dd HH:mm:ss"
	private boolean backup;
	private String note;
	private Instant lastUpdate;
		
		
	// Tags associati private
    private List<TagDto> tags; // nomi dei tag (solo lettura/scrittura applicativa)
    
    
    
}
