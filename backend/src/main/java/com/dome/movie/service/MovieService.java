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
        ResponseEntity<?> userResponse = userService.getAuthenticatedUser(userDetails);

        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return userResponse;
        }

        if (movieTitle == null || movieTitle.isEmpty()) {
            return ResponseEntity.badRequest().body("Movie title is required.");
        }

        try {
            Map<String, Object> omdbMap = omdbService.fetchOmdbMovies(movieTitle, page);
            Object searchResults = omdbMap.get("Search");

            if (!(searchResults instanceof List<?>)) {
                return ResponseEntity.ok(searchResults);
            }
            return ResponseEntity.ok(buildMoviesResponse(searchResults, userResponse, omdbMap));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching or parsing movies: " + e.getMessage());
        }
    }

    public ResponseEntity<?> addFavorite(Map<String, String> payload, UserDetails userDetails) {
        String imdbID = payload.get("imdbID");
        if (imdbID == null || imdbID.isEmpty()) {
            return ResponseEntity.badRequest().body("imdbID is required.");
        }

        ResponseEntity<?> userResponse = userService.getAuthenticatedUser(userDetails);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            return userResponse;
        }
        User user = (User) userResponse.getBody();

        Movies movie;
        try {
            movie = omdbService.fetchMovieFromOmdb(imdbID);
            if (movie == null) {
                return ResponseEntity.status(404).body("Movie not found in OMDb: " + imdbID);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch or parse movie from OMDb: " + e.getMessage());
        }

        Movies savedMovie = moviesRepository.findByImdbID(imdbID)
            .orElseGet(() -> moviesRepository.save(movie));

        if (isAlreadyFavorite(user, savedMovie)) {
            return ResponseEntity.badRequest().body("Movie already in favorites");
        }

        saveUserFavorite(user, savedMovie);
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

    private Map<String, Object> buildMoviesResponse(Object searchResults, ResponseEntity<?> userResponse, Map<String, Object> omdbMap) {

        List<MovieWithFavoriteResponse> moviesWithFavorite = buildMoviesWithFavorite(
            (List<?>) searchResults,
            (User) userResponse.getBody()
        );

        Object totalResults = omdbMap.get("totalResults");
        Map<String, Object> responseMap = new java.util.HashMap<>();
        responseMap.put("movies", moviesWithFavorite);
        responseMap.put("totalResults", totalResults);
        return responseMap;
    }

    private List<MovieWithFavoriteResponse> buildMoviesWithFavorite(List<?> moviesList, User user) {
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
        
        return moviesWithFavorite;
    }

    private boolean isAlreadyFavorite(User user, Movies movie) {
        return userFavoritesRepository.existsByUserAndMovie(user, movie);
    }

    private void saveUserFavorite(User user, Movies movie) {
        UserFavorites favorite = new UserFavorites();
        favorite.setUser(user);
        favorite.setMovie(movie);
        userFavoritesRepository.save(favorite);
    }

}
