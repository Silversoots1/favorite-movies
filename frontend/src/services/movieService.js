
import axios from "axios";
import { API_BASE_URL } from "../apiConfig";

export const getMovies = async (movie, page = 1) => {
  const response = await axios.get(
    `${API_BASE_URL}/movies`,
    {
      params: { movie, page },
      withCredentials: true
    } 
  );
  return response.data;
}

export const addFavorite = async (imdb_id) => {
  const payload = {
    imdbID: imdb_id,
  };
  const response = await axios.post(`${API_BASE_URL}/favorite`, payload, { withCredentials: true });
  return response.data;
}

export const getFavorites = async () => {
  const response = await axios.get(`${API_BASE_URL}/favorite`, { withCredentials: true });
  return response.data;
}

export const deleteFavorite = async (movie) => {
  const response = await axios.delete(`${API_BASE_URL}/favorite`, {
    data: movie,
    withCredentials: true
  });
  return response.data;
};