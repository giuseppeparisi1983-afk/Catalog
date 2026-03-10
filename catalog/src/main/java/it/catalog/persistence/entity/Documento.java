package it.catalog.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "documenti")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idDocumento;

    private String nome;
    private String estensione;
    
    @Column(name = "path_file")
    private String path;
    private Long dimensione;
    @Column(name = "data_creazione")
    private LocalDateTime dataCreazione;
    private LocalDateTime lastUpdate;
    private LocalDateTime lastView;
    private String autore;
    private String descrizione;
//    @Enumerated(EnumType.STRING) // Persistenza come stringa
    // JPA converte automaticamente questo in stringa ("Curriculum") sul DB
    // grazie al @Converter(autoApply = true) definito nel package converter
    private TipoDocumento categoria;
    private String lingua;
    private Integer versione;
//    @Enumerated(EnumType.STRING) // Persistenza come stringa
    // JPA converte automaticamente questo in stringa ("archiviato") sul DB
    // grazie al @Converter(autoApply = true) definito nel package converter
    private StatiDocumento stato;
    private String origine;
    private String note;
    
//    @OneToMany(mappedBy = "documento")
//    private List<OggettoTag> tagAssociati; // rappresenta la relazione @OneToMany con OggettoTag
}
