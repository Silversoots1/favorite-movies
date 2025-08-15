// filepath: /src/main/java/com/example/movie/service/OmdbService.java
package com.dome.movie.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dome.movie.model.Movies;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OmdbService {

    @Value("${omdb.api.key}")
    private String apiKey;
    private final String baseUrl = "http://www.omdbapi.com/";
    private final WebClient webClient;

    public OmdbService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public String getAllMoviesByTitle(String title, Integer page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("apikey", apiKey)
                    .queryParam("s", title)
                    .queryParam("page", page)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getMovieByImdbID(String imdbID) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("apikey", apiKey)
                    .queryParam("i", imdbID)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public Map<String, Object> fetchOmdbMovies(String movieTitle, Integer page) throws Exception {
        String omdbJson = getAllMoviesByTitle(movieTitle, page);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(omdbJson, new TypeReference<Map<String, Object>>() {});
    }

    public Movies fetchMovieFromOmdb(String imdbID) throws Exception {
        String omdbJson = getMovieByImdbID(imdbID);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> omdbMap = mapper.readValue(omdbJson, new TypeReference<Map<String, Object>>() {});
        if (omdbMap.containsKey("Error")) {
            return null;
        }
        Movies movie = new Movies();
        movie.setimdbID((String) omdbMap.get("imdbID"));
        movie.setTitle((String) omdbMap.get("Title"));
        String yearStr = (String) omdbMap.get("Year");
        if (yearStr != null) {
            try {
                movie.setYear(Integer.parseInt(yearStr));
            } catch (NumberFormatException nfe) {
                movie.setYear(0);
            }
        }
        movie.setType((String) omdbMap.get("Type"));
        movie.setPoster((String) omdbMap.get("Poster"));
    return movie;
}
}