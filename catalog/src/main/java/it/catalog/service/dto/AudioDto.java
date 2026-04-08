package it.catalog.service.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AudioDto {
    private Long id;
    private String nome;
    private String description;
    private String filename; 
    private String mimeType; 
    private Integer durationSeconds; 
    private long sizeBytes;
    private String formato; // MP3, FLAC, WAV 
    private String coverPath; 
    private String genere; 
    private String autore; 
    private String album; 
    private Integer annoPubblicazione; 
    private boolean cancelled; 
    private boolean preferito; 
    private Integer rating; 
    private long visualizzazioni; 
    private Instant dataArchiviazione; 
    private Instant lastView; 
    private Instant lastUpdate; 
    private boolean backup;
    private String note; 


    // Tags associati private
    private List<TagDto> tags; // nomi dei tag (solo lettura/scrittura applicativa)

    
}
