package it.catalog.service.dto;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TagDto {
	 @EqualsAndHashCode.Include 
	 /* grazie a questa annotation i metodi equals() e hashCode() 
     * vengono implementati basandosi sull'ID del tag.*/
	private Long idTag;
    private String nomeTag;
    private String tipoOggetto;
    
    public TagDto(String nomeTag) {
		super();
		this.nomeTag = nomeTag;
	}

	public TagDto(String nomeTag, String tipoOggetto) {
		super();
		this.nomeTag = nomeTag;
		this.tipoOggetto = tipoOggetto;
	}

	public TagDto(Long idTag, String nomeTag, String tipoOggetto) {
		super();
		this.idTag = idTag;
		this.nomeTag = nomeTag;
		this.tipoOggetto = tipoOggetto;
	}
	
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagDto)) return false;
        TagDto other = (TagDto) o;
        
        // Se entrambi hanno l'ID, confronta l'ID
        if (this.idTag != null && other.idTag != null) {
            return Objects.equals(this.idTag, other.idTag);
        }
        // Altrimenti confronta il nome (fondamentale per i nuovi tag)
        return Objects.equals(this.nomeTag, other.nomeTag);
    }

    @Override
    public int hashCode() {
        // Se l'ID c'è, usa quello, altrimenti il nome
        return idTag != null ? Objects.hash(idTag) : Objects.hash(nomeTag);
    }
     
}
