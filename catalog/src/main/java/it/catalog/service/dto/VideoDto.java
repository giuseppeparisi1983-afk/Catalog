package it.catalog.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import it.catalog.common.enums.CategorieVideo;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoDto {
    private Integer id;
    private String titolo;
//    @Enumerated(EnumType.STRING)
    // Anche qui usiamo l'Enum! come sull'entity
    private CategorieVideo categoria;
    private Double rating;
    private Boolean backup;
    private int visualizzazioni;
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
    
}
