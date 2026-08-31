package it.catalog.metadati.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TmdbClient {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.url}")
    private String baseUrl;

    @Value("${tmdb.api.bearer-token}")
    private String bearerToken;

    public TmdbClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Esegue la prima ricerca per titolo e restituisce l'ID del primo film trovato.
     */
    public Integer cercaIdFilm(String titolo) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search/movie")
                .queryParam("query", titolo)
                .queryParam("language", "it-IT")
                .queryParam("include_adult", "false")
                .toUriString();

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                creaHttpEntity(),
                JsonNode.class
        );

        JsonNode root = response.getBody();
        if (root != null && root.has("results") && root.get("results").isArray() && !root.get("results").isEmpty()) {
            JsonNode primoRisultato = root.get("results").get(0);
            return primoRisultato.has("id") ? primoRisultato.get("id").asInt() : null;
        }

        return null;
    }

    /**
     * Recupera il JSON di dettaglio completo (inclusi generi, cast, regista e trailer).
     */
    public JsonNode recuperaDettaglioFilm(Integer idFilm) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/" + idFilm)
                .queryParam("language", "it-IT")
                .queryParam("append_to_response", "credits,videos")
                .toUriString();

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                creaHttpEntity(),
                JsonNode.class
        );

        return response.getBody();
    }

    private HttpEntity<Void> creaHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.set("accept", "application/json");
        return new HttpEntity<>(headers);
    }
}