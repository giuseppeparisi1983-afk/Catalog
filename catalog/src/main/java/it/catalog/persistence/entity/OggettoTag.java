package it.catalog.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor // Hibernate (e in generale JPA) quando carica l’entità dal DB, richiede sempre
					// un costruttore di default senza argomenti per poter istanziare le entity
					// tramite riflessione.
@Table(name = "oggetto_tag")
//@IdClass(OggettoTagId.class)
public class OggettoTag {

	@EmbeddedId
	private OggettoTagId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idTag") // Mappa il campo idTag dell'EmbeddedId
	@JoinColumn(name = "id_tag", nullable = false)
	private Tag tag;

	/**
	 * @ManyToOne @MapsId("idDocumento")
	 * @JoinColumn(name = "id_oggetto", nullable = false) private Documento
	 *                  documento; // mappare della relazione tra OggettoTag e l'
	 *                  entità specifica Documento
	 **/

}
