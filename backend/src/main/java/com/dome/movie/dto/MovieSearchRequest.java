package com.dome.movie.dto;

public class MovieSearchRequest {
    private String movie;
    private Integer page;

    public MovieSearchRequest(String movie, int page) {
        this.movie = movie;
        this.page = page;
    }
    public String getMovie() { return movie; }
    public void setMovie(String movie) { this.movie = movie; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
}