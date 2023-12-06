package com.example.masodikpro;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;
    private String posterPath;
    private double voteAverage;

    public Movie(String title, String posterPath, double voteAverage) {
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

}

