package com.kbc.model;

public class User {
    private final String username;
    private int highScore;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}


