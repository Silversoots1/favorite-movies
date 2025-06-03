package com.dome.movie.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "imdbID", unique = true, nullable = false)
    @JsonProperty("imdbID")
    private String imdbID;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private int year;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Poster")
    private String poster;

    public Movies() {
    }

    public Movies(String imdbID, String Title, int Year, String type, String poster) {
        this.imdbID = imdbID;
        this.title = Title;
        this.year = Year;
        this.type = type;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setimdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getimdbID() {
        return imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String Type) {
        this.type = Type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

