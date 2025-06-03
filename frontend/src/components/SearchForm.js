import React, { useState }  from "react";
import "../App.css";

function SearchForm({ onSearch }) {
    const [query, setQuery] = useState("");
  
    const handleSubmit = (e) => {
      e.preventDefault();
      if (onSearch) {
        onSearch(query);
      }
    };
  
    return (
      <div className="search-form-container">
        <form onSubmit={handleSubmit} className="search-form">
          <input
            type="text"
            placeholder="Search movies..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="search-input"
          />
          <button
            type="submit"
            className="search-button"
          >
            Search
          </button>
        </form>
      </div>
    );
  }

export default SearchForm;