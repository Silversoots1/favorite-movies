import React, {useState } from "react";
import { useAuth } from "./AuthContext";
import { Navigate } from "react-router-dom";
import MovieList from "./MovieList";
import { getFavorites } from "../services/movieService";
import { useEffect } from "react";
import { deleteFavorite } from "../services/movieService";
import { MOVIES_PER_PAGE } from "../apiConfig";


/**
 * Displays the list of favorite movies for the logged-in user.
 */
function FavoritesMovies() {
  const [movies, setMovies] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (isAuthenticated) {
      const fetchFavorites = async () => {
        try {
          const data = await getFavorites();
          if (Array.isArray(data)) {
            const moviesWithFavorite = data.map(m => ({ ...m, isFavorite: true }));
            setMovies(moviesWithFavorite);
          } else if (data && Array.isArray(data.movies)) {
            const moviesWithFavorite = data.movies.map(m => ({ ...m, isFavorite: true }));
            setMovies(moviesWithFavorite);
          } else {
            setMovies([]);
          }
        } catch (error) {
          console.error("Error fetching favorites:", error);
          setMovies([]);
        }
      };
      fetchFavorites();
    }
  }, [isAuthenticated]);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  const handleFavorite = async (movie) => {
      try {
        await deleteFavorite(movie);
        setMovies(movies.filter(m => m.imdbID !== movie.imdbID));
      } catch (error) {
        console.error(error);
      }
  };

  // Pagination logic
  const totalPages = Math.ceil(movies.length / MOVIES_PER_PAGE);
  const paginatedMovies = movies.slice((currentPage - 1) * MOVIES_PER_PAGE, currentPage * MOVIES_PER_PAGE);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div>
      <MovieList movies={paginatedMovies} onFavorite={handleFavorite} />
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

export default FavoritesMovies;