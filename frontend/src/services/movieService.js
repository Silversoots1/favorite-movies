import axios from "axios";
import { API_BASE_URL } from "../apiConfig";

export const getMovies = async (movie, page = 1) => {
  const response = await axios.post(`${API_BASE_URL}/getMovies`, { movie, page }, { withCredentials: true });
  return response.data;
}

export const setFavorite = async (imdbID) => {
  const response = await axios.post(`${API_BASE_URL}/setFavorite`, {imdbID : imdbID}, { withCredentials: true });
  return response.data;
}

export const getFavorites = async () => {
  const response = await axios.post(`${API_BASE_URL}/getFavorites`, '', { withCredentials: true });
  return response.data;
}

export const deleteFavorite = async (movie) => {
  const response = await axios.delete(`${API_BASE_URL}/deleteFavorite`, {
    data: movie,
    withCredentials: true
  });
  return response.data;
};