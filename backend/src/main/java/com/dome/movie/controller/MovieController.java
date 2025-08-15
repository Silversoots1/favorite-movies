// filepath: /src/main/java/com/dome/movie/controller/MovieController.java
package com.dome.movie.controller;

import com.dome.movie.model.Movies;
import com.dome.movie.service.MovieService;
import com.dome.movie.dto.MovieSearchRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<?> getMovies(@RequestParam String movie, @RequestParam int page, @AuthenticationPrincipal UserDetails userDetails) {
        MovieSearchRequest request = new MovieSearchRequest(movie, page);
        return movieService.getMovies(request, userDetails);
    }

    @PostMapping("/favorite")
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, String> payload, @AuthenticationPrincipal UserDetails userDetails) {
        return movieService.addFavorite(payload, userDetails);
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        return movieService.getFavorites(userDetails);
    }

    @DeleteMapping("favorite")
    public ResponseEntity<?> deleteFavorite(@RequestBody Movies movie, @AuthenticationPrincipal UserDetails userDetails) {
        return movieService.deleteFavorite(movie, userDetails);
    }
}