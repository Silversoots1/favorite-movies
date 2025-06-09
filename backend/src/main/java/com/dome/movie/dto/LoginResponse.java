package com.dome.movie.dto;

public class LoginResponse {
    private Integer userId;
    private String username;

    public LoginResponse() {}

    public LoginResponse(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}