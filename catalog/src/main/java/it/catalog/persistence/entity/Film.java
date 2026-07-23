package it.catalog.persistence.entity;


import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLJoinTableRestriction;

import it.catalog.common.enums.FilmFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "film")
@Data
public class Film {

	  // ENUM
//    public enum Formato {MP4, MKV, AVI, MOV, WMV, FLV, WEBM, MPG, MPEG, VOB}
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "titolo",length = 100, nullable = false)
    private String titolo;

    @Column(length = 50)
    private String genere;

    private Double rating;

    @Column(length = 100)
    private String regista;

    @Lob
    private String protagonisti;

    private Integer anno;

//    @Column(name = "durata")
    private Double durata; 

    @Lob
    private String trama;

    @Column(length = 255)
    private String trailer;

    private String path;

    @Column(nullable = false)
    private boolean preferito = false;

    @Column(length = 255, nullable = false)
    private String locandina;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FilmFormat estensione;

    private Double dimensione;

    @Column(nullable = false)
    private boolean cancelled = false;

    @Column(nullable = false)
    private Long visualizzazioni = 0L;

//    @Column(name = "data_archiviazione")
    private Instant dataArchiviazione;

//    @Column(name = "last_view")
    private Instant lastView;

    @Column(nullable = false)
    private boolean backup = false;

    @Column(length = 255)
    private String note;

    @Column(name = "last_update")
    private Instant lastUpdate;

    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
    @JoinTable(
        name = "oggetto_tag",
        joinColumns = @JoinColumn(name = "id_oggetto",
    	        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
        inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
 // 1. Applica il filtro 'Audio' automaticamente ogni volta che Hibernate carica questa collezione
 	  @SQLJoinTableRestriction("id_tag IN (SELECT t.id_tag FROM tag t WHERE t.tipo_oggetto = 'film')")
 	  // 2. Evita il problema N+1 durante la paginazione caricando i tag a blocchi
 	  @BatchSize(size = 25) // "passo" di caricamento dei tag, da regolare in base alla dimensione media delle pagine
    private Set<Tag> tags = new HashSet<>();
    
    
    
    
    
    
    
}
