package it.catalog.service.dto;

import java.time.Instant;
import java.util.Set;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.common.enums.FileExtension;
import it.catalog.common.enums.VideoFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoDto implements VideoRecord{
    private Long id;
    private String nome; // mapping con il campo titolo;
//    @Enumerated(EnumType.STRING)
    // Anche qui usiamo l'Enum! come sull'entity
    private CategorieVideo categoria; // SPEZZONI,GUITAR,DOCUMENTARIO,SPORT,GUIDA
    private Double rating;
    private Boolean backup;
    private Integer visualizzazioni;
//    private String dataArchiviazione;
//    private String ultimaVisualizzazione;
    private Instant dataArchiviazione;
    private Instant lastView;
    private Instant lastUpdate;
    private Double durataMin;
    private Boolean preferito;
    private String note;
    private String path; // mapping con il campo percorsoFile
    private boolean cancelled;   
    private VideoFormat estensione; 
    private Double dimensione;
    private String descrizione;
    
    private Set<TagDto> tags; 
    
    @Override
    public FileExtension getEstensione() {
        return this.estensione; // Restituisce l'Enum specifico (es. FormatoVideo)
    }
    
}
