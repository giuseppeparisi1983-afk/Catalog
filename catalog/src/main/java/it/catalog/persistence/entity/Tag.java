package it.catalog.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor // Hibernate (e in generale JPA) quando carica l’entità dal DB, richiede sempre
					// un costruttore di default senza argomenti per poter istanziare le entity
					// tramite riflessione.
@Table(name = "tag")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTag;

	@Column(nullable = false, unique = true)
	private String nomeTag;

	@Column(name = "tipo_oggetto", nullable = false)
	private String tipoOggetto;

	public Tag(String nomeTag) {
		this.nomeTag = nomeTag;
	}

	public Tag(String nomeTag, String tipoOggetto) {
		super();
		this.nomeTag = nomeTag;
		this.tipoOggetto = tipoOggetto;
	}

}
