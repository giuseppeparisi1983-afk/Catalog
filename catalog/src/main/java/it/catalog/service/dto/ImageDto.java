package it.catalog.service.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String nome;
    private String description;
    private String filename;
    private String mimeType;
    private long sizeBytes;
    private String formato; // JPEG, RAW, TIFF, PNG
    private String tipoFile; // Fotografia, Sfondo, Illustrazione
    private boolean cancelled;
    private boolean preferito;
    private String path;
    private Integer rating;
    private long visualizzazioni;
    private Instant lastView; 
    private Instant dataArchiviazione; 
    private Instant lastUpdate; 
    private boolean backup;
    private String note;

 // Tags associati private
    private List<TagDto> tags; // nomi dei tag (solo lettura/scrittura applicativa)
}
