import React, { useState } from "react";
import "../App.css";
import noPoster from "../assets/No_image_available.svg.png";


const Movie = ({ imdbID, title, year, type, poster, onFavorite, isFavorite }) => {
  const [active, setActive] = React.useState(false);
  const [showLargePoster, setShowLargePoster] = useState(false);

  const handleFavorite = () => {
    setActive(true);
    if (onFavorite) onFavorite({ imdbID, title, year, type, poster, isFavorite: isFavorite});
    setTimeout(() => setActive(false), 300); // brief color/scale effect
  };

  const handlePosterClick = () => {
    setShowLargePoster(true);
  };

  const handleCloseLargePoster = () => {
    setShowLargePoster(false);
  };

  return (
    <div className="movie-container">
      <img
        src={poster && poster !== "N/A" ? poster : noPoster}
        alt={`${type} poster`}
        className="movie-poster"
        onClick={poster && poster !== "N/A" ? handlePosterClick : undefined}
        title={poster && poster !== "N/A" ? "Click to enlarge" : "No poster available"}
        onError={e => { e.target.onerror = null; e.target.src = noPoster; }}
      />
      <div className="movie-details">
        <h3>{title}</h3>
        <p><strong>year:</strong> {year}</p>
        <p><strong>type:</strong> {type}</p>
        <p>
          <strong>imdbID:</strong>{' '}
          <a href={`https://www.imdb.com/title/${imdbID}`} target="_blank" rel="noopener noreferrer" className="movie-imdb-link">
            {imdbID}
          </a>
        </p>
        <button
          className={`favorite-button${isFavorite ? " favorite-button-active" : ""}${active ? " favorite-button-pressed" : ""}`}
          onClick={handleFavorite}
          title="Add to Favorites"
        >
          ♥
        </button>
      </div>
      {showLargePoster && poster && poster !== "N/A" && (
        <div className="movie-overlay" onClick={handleCloseLargePoster}>
          <img
            src={poster}
            alt={`${type} poster large`}
            className="movie-large-poster movie-responsiveLargePoster"
          />
        </div>
      )}
    </div>
  );
};

export default Movie;