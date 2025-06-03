import React from "react";
import Movie from "./Movie";

/**
 * Renders a list of Movie components.
 * @param {Object[]} movies - Array of movie objects.
 * @param {Function} onFavorite - Handler for adding a movie to favorites.
 */
function MovieList({ movies, onFavorite }) {
  return (
    <div>
      {movies.map((movie) => (
        <Movie
          key={movie.imdbID}
          imdbID={movie.imdbID}
          title={movie.Title}
          year={movie.Year}
          type={movie.Type}
          poster={movie.Poster}
          onFavorite={onFavorite}
          isFavorite={movie.isFavorite}
        />
      ))}
    </div>
  );
}

export default MovieList;