package it.catalog.service.dto;

import java.time.Instant;
import java.util.List;

import it.catalog.common.enums.CategorieVideo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoDto implements VideoRecord{
    private Integer id;
    private String titolo;
//    @Enumerated(EnumType.STRING)
    // Anche qui usiamo l'Enum! come sull'entity
    private CategorieVideo categoria;
    private Double rating;
    private Boolean backup;
    private Integer visualizzazioni;
//    private String dataArchiviazione;
//    private String ultimaVisualizzazione;
    private Instant dataArchiviazione;
    private Instant lastView;
    private Instant lastUpdate;
    private Integer durataMin;
    private Boolean preferito;
    private String note;
    private String percorsoFile;
    private boolean cancelled;   
    
    private List<TagDto> tags; 
    
}
