package it.catalog.service.dto;

import java.time.Instant;
import java.util.Set;

import it.catalog.common.enums.ImageFormat;
import it.catalog.common.enums.ImageType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto  implements HasId{
    private Long id;
    private String nome;
    private String descrizione;
    private String locandina;
    private Double dimensione;
    private ImageFormat formato; // JPEG, RAW, TIFF, PNG
    private ImageType tipoFile; // Fotografia, Sfondo, Illustrazione
    private boolean cancelled;
    private boolean preferito;
    private String path;
    private Double rating;
    private long visualizzazioni;
    private Instant lastView; 
    private Instant dataArchiviazione; 
    private Instant lastUpdate; 
    private boolean backup;
    private String note;

 // Tags associati private
    private Set<TagDto> tags; // nomi dei tag (solo lettura/scrittura applicativa)
}
