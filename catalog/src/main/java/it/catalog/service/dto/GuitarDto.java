package it.catalog.service.dto;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.common.enums.FileExtension;
import it.catalog.common.enums.Livello;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuitarDto implements VideoRecord{   

    private Long idGuitar;
//    private Integer videoId;     // id della tabella video (null se nuovo)
    private Boolean visto;
    private Boolean todo;
//    @Enumerated(EnumType.STRING) // Persistenza come stringa
 // Anche qui usiamo l'Enum! come sull'entity
    private Livello difficolta;
    private String autore;
    
    private VideoDto video; // COMPOSIZIONE
    
    // IMPLEMENTAZIONE INTERFACCIA (Delega al video interno)
	@Override
	public Long getId() {
		return idGuitar;
	}

	@Override
	public String getNome() {
		return video != null ? video.getNome() : "";
	}

	@Override
	public CategorieVideo getCategoria() {
		return video != null ? video.getCategoria() : null;
	}

	@Override
	public Double getRating() {
		return video != null ? video.getRating() : 0.0;
	}

	@Override
	public Boolean getPreferito() {
		return video != null ? video.getPreferito() : false;
	}

	@Override
	public Boolean getBackup() {
		return video != null ? video.getBackup() : false;
	}
	
	@Override
	public boolean isCancelled() {
		return video != null && video.isCancelled();
	}

	@Override
	public Instant getDataArchiviazione() {
		return video != null ? video.getDataArchiviazione(): null;
	}

	@Override
	public Double getDurataMin() {
		return video != null ? video.getDurataMin(): 0;
	}

	@Override
	public Instant getLastUpdate() {
		return video != null ? video.getLastUpdate(): null;
	}

	@Override
	public Integer getVisualizzazioni() {
		// TODO Auto-generated method stub
		return video != null ? video.getVisualizzazioni(): 0;
	}

	@Override
	public Instant getLastView() {
		// TODO Auto-generated method stub
		return video != null ? video.getLastView(): null;
	}

	@Override
	public String getNote() {
		// TODO Auto-generated method stub
		return video != null ? video.getNote(): "";
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return video != null ? video.getPath(): "";
	}

	@Override
	public Set<TagDto> getTags() {
		// TODO Auto-generated method stub
		return video != null ? video.getTags(): new LinkedHashSet<>();
	}

	@Override
	public void setVisualizzazioni(Integer visualizzazioni) {
		// TODO Auto-generated method stub
		this.video.setVisualizzazioni(visualizzazioni);
	}

	@Override
	public void setLastView(Instant lastView) {
		// TODO Auto-generated method stub
		this.video.setLastView(lastView);
		
	}
	
	@Override
	public FileExtension getEstensione() {
	    return (video != null) ? video.getEstensione() : null;
	}

	@Override
	public String getDescrizione() {
		
		return video != null ? video.getDescrizione(): "";
	}

	@Override
	public Double getDimensione() {
		
		return video != null ? video.getDimensione() : 0.0;
	}
    
    
    
}

