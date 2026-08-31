package it.catalog.metadati.service;

import it.catalog.service.dto.FilmDto;

public interface MetadataService {

    /**
     * Cerca i metadati di un film su provider esterni (es. TMDB)
     * e popola i campi corrispondenti del FilmDto.
     * 
     * @param titolo Titolo del film inserito dall'utente
     * @return FilmDto parzialmente compilato con i metadati trovati, oppure null
     */
    FilmDto cercaMetadatiFilm(String titolo);
}