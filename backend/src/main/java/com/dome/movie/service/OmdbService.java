// filepath: /src/main/java/com/example/movie/service/OmdbService.java
package com.dome.movie.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
}