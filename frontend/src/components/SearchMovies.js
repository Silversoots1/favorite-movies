import React, { useState } from "react";
import SearchForm from "./SearchForm";
import MovieList from "./MovieList";
import { getMovies, setFavorite, deleteFavorite } from "../services/movieService";
import { useAuth } from "./AuthContext";
import { Navigate } from "react-router-dom";
import { MOVIES_PER_PAGE } from "../apiConfig";
/**
 * Main component for searching and favoriting movies.
 */
function SearchMovies() {
  const [searchTerm, setSearchTerm] = useState("");
  const [movies, setMovies] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalResults, setTotalResults] = useState(0);
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Handles adding a movie to favorites
  const handleFavorite = async (movie) => {
    try {
      if (movie.isFavorite) {
        await deleteFavorite(movie);
        setMovies(movies => movies.map(m =>
          m.imdbID === movie.imdbID ? { ...m, isFavorite: false } : m
        ));
      } else {
        await setFavorite(movie.imdbID);
        setMovies(movies => movies.map(m =>
          m.imdbID === movie.imdbID ? { ...m, isFavorite: true } : m
        ));
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleSearch = async (movie) => {
    try {
      setSearchTerm(movie);
      const data = await getMovies(searchTerm, currentPage);
      const moviesArray = Array.isArray(data.movies) ? data.movies : (Array.isArray(data) ? data : []);
      setMovies(moviesArray);
      setTotalResults(Number(data.totalResults) || (moviesArray.length || 0));
      setCurrentPage(1);
    } catch (error) {
      console.error("Error fetching movies:", error);
    }
  };

  const handlePageChange = async (page) => {
    setCurrentPage(page);
    try {
      const data = await getMovies(searchTerm, page);
      const moviesArray = Array.isArray(data.movies) ? data.movies : (Array.isArray(data) ? data : []);
      setMovies(moviesArray);
      setTotalResults(Number(data.totalResults) || (moviesArray.length || 0));
    } catch (error) {
      console.error("Error fetching movies:", error);
    }
  };

  const totalPages = Math.ceil(totalResults / MOVIES_PER_PAGE);

  return (
    <div className="search-movies-container">
      <SearchForm onSearch={handleSearch} />
      <MovieList movies={movies} onFavorite={handleFavorite} />
      {totalPages > 1 && (
        <div style={{ display: 'flex', justifyContent: 'center', margin: '20px 0' }}>
          <button className="pagination" onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>&lt; Prev</button>
          <span style={{ margin: '0 10px' }}>Page {currentPage} of {totalPages}</span>
          <button className="pagination" onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages}>Next &gt;</button>
        </div>
      )}
    </div>
  );
}

export default SearchMovies;