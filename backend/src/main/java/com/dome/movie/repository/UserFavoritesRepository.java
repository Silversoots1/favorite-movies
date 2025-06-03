package com.dome.movie.repository;

import com.dome.movie.model.User;
import com.dome.movie.model.Movies;
import com.dome.movie.model.UserFavorites;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoritesRepository extends JpaRepository<UserFavorites, Integer> {
    boolean existsByUserAndMovie(User user, Movies movie);
    Optional<UserFavorites>  findByUserAndMovie(User user, Movies movie);
    List<UserFavorites> findByUser(User user);
    boolean existsByUserAndMovieImdbID(User user, String imdbID);
}