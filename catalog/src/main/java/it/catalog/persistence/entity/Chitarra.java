package it.catalog.persistence.entity;



import it.catalog.common.enums.Livello;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "chitarra")
@Data
public class Chitarra {   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL) // l'opzione cascade viene aggiunta per dire a JPA dire a JPA che quando salvi un video Chitarra deve salvare anche il Video associato.
    @JoinColumn(name = "id_video", unique = true, nullable = false)
    private Video video;

    private Boolean visto;
    private Boolean todo;
    @Enumerated(EnumType.STRING) // Persistenza come stringa
 // JPA converte automaticamente questo in stringa ("base") sul DB
    // grazie al @Converter(autoApply = true) definito nel package converter
    private Livello difficolta;
    private String autore;
}

