package it.catalog.persistence.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLJoinTableRestriction;

import it.catalog.common.enums.StatiDocumento;
import it.catalog.common.enums.TipoDocumento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Data
@Table(name = "documenti")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * Questa annotation ci dice che l'ID viene deciso dal database 
     * solo nel momento in cui la riga viene scritta fisicamente.
     * */
    @Column(name = "id")
    private Long id;

    private String nome;
    private String estensione;
    
    @Column(name = "path_file")
    private String path;
    private Double dimensione;
    private Instant dataArchiviazione;
    private Instant lastUpdate;
    private Instant lastView;
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
    private boolean preferito; 
    private Integer rating; 
    private long visualizzazioni; 
    private boolean backup;
    private String note;
    
//    @OneToMany(mappedBy = "documento")
//    private List<OggettoTag> tagAssociati; // rappresenta la relazione @OneToMany con OggettoTag
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
    @JoinTable(
        name = "oggetto_tag",
        joinColumns = @JoinColumn(name = "id_oggetto",
    	        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
        inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
 // 1. Applica il filtro 'Audio' automaticamente ogni volta che Hibernate carica questa collezione
 	  @SQLJoinTableRestriction("id_tag IN (SELECT t.id_tag FROM tag t WHERE t.tipo_oggetto = 'Documento')")
 	  // 2. Evita il problema N+1 durante la paginazione caricando i tag a blocchi
 	  @BatchSize(size = 25) // "passo" di caricamento dei tag, da regolare in base alla dimensione media delle pagine
	private Set<Tag> tags = new HashSet<>();
}
