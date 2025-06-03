package com.dome.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dome.movie.model.Movies;
import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, Integer> {
    Optional<Movies> findByImdbID(String imdbID);
    boolean existsByImdbID(String imdbID);
}
