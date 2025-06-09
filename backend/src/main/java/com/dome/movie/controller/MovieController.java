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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/getMovies")
    public ResponseEntity<?> getMovies(@RequestBody MovieSearchRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return movieService.getMovies(request, userDetails);
    }

    @PostMapping("/setFavorite")
    public ResponseEntity<?> setFavorite(@RequestBody Map<String, String> payload, @AuthenticationPrincipal UserDetails userDetails) {
        return movieService.setFavorite(payload, userDetails);
    }

    @PostMapping("/getFavorites")
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        return movieService.getFavorites(userDetails);
    }

    @PostMapping("/isFavorite")
    public ResponseEntity<?> isFavorite(@RequestBody Movies movie, @AuthenticationPrincipal UserDetails userDetails) {
        return movieService.isFavorite(movie, userDetails);
    }

    @DeleteMapping("deleteFavorite")
    public ResponseEntity<?> deleteFavorite(@RequestBody Movies movie, @AuthenticationPrincipal UserDetails userDetails) {
        return movieService.deleteFavorite(movie, userDetails);
    }
}