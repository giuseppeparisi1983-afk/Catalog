package it.catalog.persistence.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLJoinTableRestriction;

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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "image_file")
public class ImageFile {
    public enum Formato { JPEG, RAW, TIFF, PNG }
    public enum TipoFile { Fotografia, Sfondo, Illustrazione }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String nome;
    
    private String description;
    private String filename;
    private String mimeType;
    private long sizeBytes;

    @Enumerated(EnumType.STRING)
    private Formato formato;

    @Enumerated(EnumType.STRING)
    private TipoFile tipoFile;

    @Column(name = "path_file")
    private String path;
    
    private boolean cancelled;
    private boolean preferito;
    private Integer rating;
    private long visualizzazioni;
    private Instant dataArchiviazione;
    private Instant lastUpdate;
    private Instant lastView;
    
    private boolean backup;
    private String note;

    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
    @JoinTable(
        name = "oggetto_tag",
        joinColumns = @JoinColumn(name = "id_oggetto",
    	        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
        inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
 // 1. Applica il filtro 'Audio' automaticamente ogni volta che Hibernate carica questa collezione
 	  @SQLJoinTableRestriction("id_tag IN (SELECT t.id_tag FROM tag t WHERE t.tipo_oggetto = 'Image')")
 	  // 2. Evita il problema N+1 durante la paginazione caricando i tag a blocchi
 	  @BatchSize(size = 25) // "passo" di caricamento dei tag, da regolare in base alla dimensione media delle pagine
    private Set<Tag> tags = new HashSet<>();

    @PreUpdate
    public void preUpdate() { lastUpdate = Instant.now(); }
    // getters & setters
}

