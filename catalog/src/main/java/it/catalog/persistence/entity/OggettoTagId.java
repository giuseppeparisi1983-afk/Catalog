package it.catalog.persistence.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
@Data	
public class OggettoTagId implements Serializable {
   
	 @Column(name = "id_oggetto")
	 private Long idOggetto;

	 @Column(name = "id_tag")
	 private Long idTag;

    // equals() e hashCode() obbligatori
}
