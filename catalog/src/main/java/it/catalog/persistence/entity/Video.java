package it.catalog.persistence.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLJoinTableRestriction;

import it.catalog.common.enums.CategorieVideo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    private Instant dataArchiviazione;
    private Instant lastView;
    private Instant lastUpdate;
    private Boolean backup;
    private String note;
    private Boolean cancelled;
    
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
    @JoinTable(
        name = "oggetto_tag",
        joinColumns = @JoinColumn(name = "id_oggetto",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
        ),
        inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
  // 1. Applica il filtro 'Audio' automaticamente ogni volta che Hibernate carica questa collezione
  @SQLJoinTableRestriction("id_tag IN (SELECT t.id_tag FROM tag t WHERE t.tipo_oggetto = 'Video')")
  // 2. Evita il problema N+1 durante la paginazione caricando i tag a blocchi
  @BatchSize(size = 25) // "passo" di caricamento dei tag, da regolare in base alla dimensione media delle pagine
private Set<Tag> tags = new HashSet<>();

}