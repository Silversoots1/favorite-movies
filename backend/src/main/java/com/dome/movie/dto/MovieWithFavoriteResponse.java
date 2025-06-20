package com.dome.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieWithFavoriteResponse {
    private String imdbID;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Poster")
    private String poster;
    @JsonProperty("isFavorite")
    private boolean isFavorite;

    public String getImdbID() { return imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}