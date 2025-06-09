package com.dome.movie.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.dome.movie.dto.MovieSearchRequest;
import com.dome.movie.dto.MovieWithFavoriteResponse;
import com.dome.movie.model.Movies;
import com.dome.movie.model.User;
import com.dome.movie.model.UserFavorites;
import com.dome.movie.repository.UserFavoritesRepository;
import com.dome.movie.repository.MoviesRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private UserService userService;
    @Autowired
    private OmdbService omdbService;
    @Autowired
    private UserFavoritesRepository userFavoritesRepository;
    @Autowired
    private MoviesRepository moviesRepository;

    public ResponseEntity<?> getMovies(MovieSearchRequest request, UserDetails userDetails) {
        String movieTitle = request.getMovie();
        Integer page = request.getPage() == null ? 1 : request.getPage();

        if (movieTitle == null || movieTitle.isEmpty()) {
            return ResponseEntity.badRequest().body("Movie title is required.");
        }

        ResponseEntity<?> userResponse = userService.getAuthenticatedUser(userDetails);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return userResponse;
        }

        try {
            String omdbJson = omdbService.getAllMoviesByTitle(movieTitle, page);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> omdbMap = mapper.readValue(omdbJson, new TypeReference<Map<String, Object>>() {});
            Object searchResults = omdbMap.get("Search");

            if (!(searchResults instanceof List<?>)) {
                return ResponseEntity.ok(searchResults);
            }

            List<?> moviesList = (List<?>) searchResults;
            User user = (User) userResponse.getBody();
            List<MovieWithFavoriteResponse> moviesWithFavorite = new java.util.ArrayList<>();

            for (Object obj : moviesList) {
                if (obj instanceof Map<?, ?>) {
                    Map<?, ?> rawMap = (Map<?, ?>) obj;
                    MovieWithFavoriteResponse movieDto = new MovieWithFavoriteResponse();
                    movieDto.setImdbID((String) rawMap.get("imdbID"));
                    movieDto.setTitle((String) rawMap.get("Title"));
                    movieDto.setYear((String) rawMap.get("Year"));
                    movieDto.setType((String) rawMap.get("Type"));
                    movieDto.setPoster((String) rawMap.get("Poster"));
                    boolean isFavorite = movieDto.getImdbID() != null && user != null &&
                            userFavoritesRepository.existsByUserAndMovieImdbID(user, movieDto.getImdbID());
                    movieDto.setFavorite(isFavorite);
                    moviesWithFavorite.add(movieDto);
                }
            }

            Object totalResults = omdbMap.get("totalResults");
            Map<String, Object> responseMap = new java.util.HashMap<>();
            responseMap.put("movies", moviesWithFavorite);
            responseMap.put("totalResults", totalResults);

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching or parsing movies: " + e.getMessage());
        }
    }

    public ResponseEntity<?> setFavorite(Map<String, String> payload, UserDetails userDetails) {
        String imdbID = payload.get("imdbID");
        if (imdbID == null || imdbID.isEmpty()) {
            return ResponseEntity.badRequest().body("imdbID is required.");
        }

        ResponseEntity<?> userResponse = userService.getAuthenticatedUser(userDetails);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return userResponse;
        }
        User user = (User) userResponse.getBody();

        // Fetch movie details from OMDb
        String omdbJson = omdbService.getMovieByImdbID(imdbID);
        Movies movie;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> omdbMap = mapper.readValue(omdbJson, new TypeReference<Map<String, Object>>() {});
            if (omdbMap.containsKey("Error")) {
                return ResponseEntity.status(404).body("Movie not found in OMDb: " + omdbMap.get("Error"));
            }
            // Manual mapping from OMDb fields to Movies entity
            movie = new Movies();
            movie.setimdbID((String) omdbMap.get("imdbID"));
            movie.setTitle((String) omdbMap.get("Title"));
            // OMDb Year is a string, Movies expects int
            String yearStr = (String) omdbMap.get("Year");
            if (yearStr != null) {
                try {
                    movie.setYear(Integer.parseInt(yearStr));
                } catch (NumberFormatException nfe) {
                    movie.setYear(0); // fallback if year is not a number
                }
            }
            movie.setType((String) omdbMap.get("Type"));
            movie.setPoster((String) omdbMap.get("Poster"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch or parse movie from OMDb: " + e.getMessage());
        }

        // Save movie if not already in DB
        Movies savedMovie = moviesRepository.findByImdbID(imdbID)
            .orElseGet(() -> moviesRepository.save(movie));

        // Check if already a favorite
        boolean exists = userFavoritesRepository.existsByUserAndMovie(user, savedMovie);
        if (exists) {
            return ResponseEntity.badRequest().body("Movie already in favorites");
        }

        // Save as favorite
        UserFavorites favorite = new UserFavorites();
        favorite.setUser(user);
        favorite.setMovie(savedMovie);
        userFavoritesRepository.save(favorite);
        return ResponseEntity.ok("Movie added to favorites successfully!");
    }

    public ResponseEntity<?> getFavorites(UserDetails userDetails) {
        ResponseEntity<?> userResponse = userService.getAuthenticatedUser(userDetails);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return userResponse;
        }

        User user = (User) userResponse.getBody();
        List<UserFavorites> favorites = userFavoritesRepository.findByUser(user);

        List<Movies> favoriteMovies = favorites.stream()
                .map(UserFavorites::getMovie)
                .collect(Collectors.toList());

        if (favoriteMovies.isEmpty()) {
            return ResponseEntity.ok("No favorite movies found for this user.");
        }
        return ResponseEntity.ok(favoriteMovies);
    }

    public ResponseEntity<?> isFavorite(Movies movie, UserDetails userDetails) {
        ResponseEntity<?> userResponse = userService.getAuthenticatedUser(userDetails);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return userResponse;
        }

        User user = (User) userResponse.getBody();
        boolean exists = userFavoritesRepository.existsByUserAndMovie(user, movie);

        return ResponseEntity.ok(exists);
    }

    public ResponseEntity<?> deleteFavorite(Movies movie, UserDetails userDetails) {
        ResponseEntity<?> userResponse = userService.getAuthenticatedUser(userDetails);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return userResponse;
        }

        User user = (User) userResponse.getBody();
        UserFavorites favorite = userFavoritesRepository.findByUserAndMovie(user, movie)
                .orElse(null);

        if (favorite == null) {
            return ResponseEntity.badRequest().body("Favorite movie not found for this user.");
        }

        userFavoritesRepository.delete(favorite);
        return ResponseEntity.ok("Favorite movie deleted successfully!");
    }
}
