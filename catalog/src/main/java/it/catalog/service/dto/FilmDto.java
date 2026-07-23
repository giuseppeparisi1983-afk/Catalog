package it.catalog.service.dto;

import java.time.Instant;
import java.util.Set;

import it.catalog.common.enums.FilmFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilmDto {

	private Long id;
//	private String nome; // mapping con titolo
	private String titolo; // mapping con titolo
	private String genere;
	private Double rating;
	private String regista;
	private String protagonisti; // elenco dei protagonisti come stringa (es. "Attore1, Attore2")
	private Integer anno;
	private Double duration; // formato "hh:mm" mapping con il campo durata
	private String descrizione; // mapping con il campo trama
	private String trailer; // URL del trailer
	
	private String path;
	private boolean preferito;
	private String locandina;
	private FilmFormat estensione; // rappresentato come enum FilmFormat
	private Double dimensione;
	private boolean cancelled;
	private Long visualizzazioni;
	private Instant dataArchiviazione; // formato "yyyy-MM-dd"
	private Instant lastView; // formato "yyyy-MM-dd HH:mm:ss"
	private boolean backup;
	private String note;
	private Instant lastUpdate;
		
		
	// Tags associati private
	private Set<TagDto> tags; // nomi dei tag (solo lettura/scrittura applicativa)
    
    
    
}
