package it.catalog.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import it.catalog.common.enums.CategorieVideo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "video")
//@Inheritance(strategy = InheritanceType.JOINED) // <--- FONDAMENTALE
@Data
public class Video {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titolo;
//    @Enumerated(EnumType.STRING) // Persistenza come stringa
    // JPA converte automaticamente questo in stringa ("Spezzoni Film") sul DB
    // grazie al @Converter(autoApply = true) definito nel package converter
    private CategorieVideo categoria;
    private String percorsoFile;
    private Boolean preferito;
    private Double rating;
    private Integer visualizzazioni;
    private Integer durataMin;
    private LocalDate dataArchiviazione;
    private LocalDateTime ultimaVisualizzazione;
    private Boolean backup;
    private String note;
    private Boolean cancelled;

}