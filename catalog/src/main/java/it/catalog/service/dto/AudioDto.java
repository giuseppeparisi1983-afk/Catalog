package it.catalog.service.dto;

import java.time.Instant;
import java.util.Set;

import it.catalog.common.enums.AudioFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AudioDto {
    private Long id;
    private String nome;
    private String descrizione;
    private String path; 
    private Double duration; 
    private Double dimensione;
    private AudioFormat estensione; // 'MP3','WAV','AAC','FLAC','OGG'
    private String coverPath; 
    private String genere; 
    private String autore; 
    private String album; 
    private Integer anno; 
    private boolean cancelled; 
    private boolean preferito; 
    private Double rating; 
    private long visualizzazioni; 
    private Instant dataArchiviazione; 
    private Instant lastView; 
    private Instant lastUpdate; 
    private boolean backup;
    private String note; 


    // Tags associati private
    private Set<TagDto> tags; // nomi dei tag (solo lettura/scrittura applicativa)

    
}
