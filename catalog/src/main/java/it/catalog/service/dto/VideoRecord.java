package it.catalog.service.dto;

import java.time.Instant;
import java.util.Set;

import it.catalog.common.enums.CategorieVideo;
import it.catalog.common.enums.FileExtension;

public interface VideoRecord  extends HasId{

//    Long getId(); // Restituisce l'ID primario della riga (idVideo o idGuitar)
//    String getTitolo();
    String getNome();
    CategorieVideo getCategoria();
    Double getRating();
    Boolean getPreferito();
    Boolean getBackup();
    Integer getVisualizzazioni(); // Metodo per ottenere il numero di visualizzazioni
    void setVisualizzazioni(Integer visualizzazioni); // Fondamentale per il setter
    Instant getDataArchiviazione(); // Metodo per ottenere la data di archivizione
    Instant getLastView(); // Metodo per ottenere la data dell'ultima visualizzazione
    void setLastView(Instant lastView); // Se vuoi aggiornare anche la data al click
    Instant getLastUpdate(); // Metodo per ottenere la data dell'ultimo aggiornamento
    Double getDurataMin(); // Metodo per ottenere la durata del video in secondi    
    String getNote(); // Metodo per ottenere le note associate al video
//    String getPercorsoFile(); // Metodo per ottenere il percorso del video
    String getPath(); // Metodo per ottenere il percorso del video
    FileExtension getEstensione(); // Metodo per ottenere l'estensione del video
    boolean isCancelled();
    String getDescrizione();
    Double getDimensione();
    Set<TagDto> getTags(); // Metodo per ottenere i tag associati al video
      
}
