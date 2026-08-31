package it.catalog.metadati.service.impl;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import it.catalog.metadati.client.TmdbClient;
import it.catalog.metadati.service.MetadataService;
import it.catalog.service.dto.FilmDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetadataServiceImpl implements MetadataService {

    private final TmdbClient tmdbClient;
    
    @Value("${tmdb.poster.url}")
    private String urlTmdb ; // Base URL per le locandine

    @Value("${youtube.url}")
    private String urlYouTube ; // Base URL per il trailer

    public MetadataServiceImpl(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    @Override
    public FilmDto cercaMetadatiFilm(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            return null;
        }

        // 1. Cerchiamo l'ID del film
        Integer idFilm = tmdbClient.cercaIdFilm(titolo.trim());
        if (idFilm == null) {
            return null; // Nessun film trovato
        }
        
        log.info("ID del film trovato per '{}': {}", titolo, idFilm);

        // 2. Recuperiamo il dettaglio completo
        JsonNode json = tmdbClient.recuperaDettaglioFilm(idFilm);
        if (json == null) {
            return null;
        }

        log.info("Dettaglio JSON del film: {}", json.toString());
        
        // 3. Mappiamo il JSON nel tuo FilmDto
        FilmDto dto = new FilmDto();

        dto.setNome(getText(json, "title"));
        dto.setDescrizione(getText(json, "overview"));

        // Anno di uscita (estratto dai primi 4 caratteri della release_date "YYYY-MM-DD")
        String releaseDate = getText(json, "release_date");
        if (releaseDate != null && releaseDate.length() >= 4) {
            try {
                dto.setAnno(Integer.parseInt(releaseDate.substring(0, 4)));
            } catch (NumberFormatException ignored) {}
        }

     // Durata: lettura diretta dei minuti totali come Double
        if (json.has("runtime") && !json.get("runtime").isNull()) {
            dto.setDuration(json.get("runtime").asDouble()); // es. 120.0 per 2 ore
        }
        
        
        // Generi: estraiamo i nomi e li uniamo con ", "
        if (json.has("genres") && json.get("genres").isArray()) {
            List<String> generiList = new ArrayList<>();
            for (JsonNode g : json.get("genres")) {
                if (g.has("name")) generiList.add(g.get("name").asText());
            }
            dto.setGenere(String.join(", ", generiList));
        }

        // Crediti: Regista e Protagonisti
        if (json.has("credits")) {
            JsonNode credits = json.get("credits");

            // Regista (job == "Director")
            if (credits.has("crew") && credits.get("crew").isArray()) {
                for (JsonNode crewMember : credits.get("crew")) {
                    if ("Director".equalsIgnoreCase(getText(crewMember, "job"))) {
                        dto.setRegista(getText(crewMember, "name"));
                        break;
                    }
                }
            }

            // Protagonisti (prendiamo i primi 4 attori principali)
            if (credits.has("cast") && credits.get("cast").isArray()) {
                List<String> figure = new ArrayList<>();
                JsonNode castArray = credits.get("cast");
                int maxAttori = Math.min(castArray.size(), 4);
                String 	protagonista="";
//                String personaggio ="";
//                String nomeAttore ="";
                for (int i = 0; i < maxAttori; i++) {
//                	nomeAttore = getText(castArray.get(i), "name");
//                	personaggio = getText(castArray.get(i), "character");
                    
                    if (getText(castArray.get(i), "character") != null)
                    	protagonista=getText(castArray.get(i), "character");
                    if (getText(castArray.get(i), "name") != null) 
                    	protagonista+=" ("+getText(castArray.get(i), "name")+")";
                    	
                    	figure.add(protagonista);
                    	
//                    personaggio ="";
//                    nomeAttore ="";
                    protagonista="";
                }
                dto.setProtagonisti(String.join(", ", figure));
            }
        }

        // Locandina: URL 
        if (json.has("poster_path") && !json.get("poster_path").isNull()) {
            String posterPath = json.get("poster_path").asText();
            dto.setLocandina(urlTmdb+posterPath);
        }
        
        // Trailer YouTube
        if (json.has("videos") && json.get("videos").has("results")) {
            JsonNode videos = json.get("videos").get("results");
            if (videos.isArray()) {
                for (JsonNode video : videos) {
                    boolean isYouTube = "YouTube".equalsIgnoreCase(getText(video, "site"));
                    boolean isTrailer = "Trailer".equalsIgnoreCase(getText(video, "type"));
                    if (isYouTube && isTrailer) {
                        String key = getText(video, "key");
                        if (key != null) {
                            dto.setTrailer(urlYouTube + key);
                            break;
                        }
                    }
                }
            }
        }
        
        return dto;
    }

    /**
     * Helper Method Null-Safe per estrarre il testo da un JsonNode senza rischi di NullPointerException.
     */
    private String getText(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName) && !node.get(fieldName).isNull()) {
            return node.get(fieldName).asText();
        }
        return null;
    }
}
