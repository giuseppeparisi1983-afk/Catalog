package it.catalog.service.dto;

import java.time.Instant;
import java.util.List;

import it.catalog.common.enums.CategorieVideo;

public interface VideoRecord {

    Integer getId(); // Restituisce l'ID primario della riga (idVideo o idGuitar)
    String getTitolo();
    CategorieVideo getCategoria();
    Double getRating();
    Boolean getPreferito();
    Boolean getBackup();
    Integer getVisualizzazioni(); // Metodo per ottenere il numero di visualizzazioni
    Instant getDataArchiviazione(); // Metodo per ottenere la data di archivizione
    Instant getLastView(); // Metodo per ottenere la data dell'ultima visualizzazione
    Instant getLastUpdate(); // Metodo per ottenere la data dell'ultimo aggiornamento
    Integer getDurataMin(); // Metodo per ottenere la durata del video in secondi    
    String getNote(); // Metodo per ottenere le note associate al video
    String getPercorsoFile(); // Metodo per ottenere il percorso del video
    boolean isCancelled();
    
    List<TagDto> getTags(); // Metodo per ottenere i tag associati al video
      
}
