package it.catalog.persistence.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Data
@Table(name = "audio_file") 
public class AudioFile { 
	
	public enum Formato { MP3, FLAC, WAV } 
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id; 
	private String title; 
	private String description; 
	private String filename; private String mimeType; 
	private Integer durationSeconds; 
	private long sizeBytes; 
	@Enumerated(EnumType.STRING) 
	private Formato formato; 
	private String coverPath; 
	private String genere; 
	private String autore; private String album; 
	private Integer annoPubblicazione; 
	private boolean cancelled; private boolean preferito; 
	private Integer rating; private long visualizzazioni;
	private Instant dataArchiviazione; 
	private Instant dataUltimaVisualizzazione; 
	private boolean backup; private String note;
	private Instant createdAt = Instant.now(); 
	private Instant updatedAt; 
	
	  // Mappatura "Virtuale" per permettere la Specification
	 // relazione con OggettoTag
	@OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oggetto", // Colonna su oggetto_tag
    referencedColumnName = "id", // Colonna su audio_file
    		insertable = false, 
            updatable = false)
    private Set<OggettoTag> tags = new HashSet<>(); 

@PreUpdate 
public void preUpdate(){ 
	updatedAt = Instant.now(); 
	} 

// getters & setters 

}
