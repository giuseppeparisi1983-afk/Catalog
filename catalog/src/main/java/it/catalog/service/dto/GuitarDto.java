package it.catalog.service.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.common.enums.Difficolta;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuitarDto implements VideoRecord{   

    private Integer idGuitar;
//    private Integer videoId;     // id della tabella video (null se nuovo)
    private Boolean visto;
    private Boolean todo;
//    @Enumerated(EnumType.STRING) // Persistenza come stringa
 // Anche qui usiamo l'Enum! come sull'entity
    private Difficolta difficolta;
    private String autore;
    
    private VideoDto video; // COMPOSIZIONE
    
    // IMPLEMENTAZIONE INTERFACCIA (Delega al video interno)
	@Override
	public Integer getId() {
		return idGuitar;
	}

	@Override
	public String getTitolo() {
		return video != null ? video.getTitolo() : "";
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
	public Integer getDurataMin() {
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
	public String getPercorsoFile() {
		// TODO Auto-generated method stub
		return video != null ? video.getPercorsoFile(): "";
	}

	@Override
	public List<TagDto> getTags() {
		// TODO Auto-generated method stub
		return video != null ? video.getTags(): new ArrayList<>();
	}
    
    
    
}

