package com.kaleidoscope.filmadviser.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    private int id;
    private double imdbRating;
    private double kinoRating;
    private String ruTitle;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;

    public Movie() {
    }

    public Movie(int id, double imdbRating, double kinoRating, String ruTitle,
                 String originalTitle, String posterPath, String backdropPath, String releaseYear) {
        this.id = id;
        this.imdbRating = imdbRating;
        this.kinoRating = kinoRating;
        this.ruTitle = ruTitle;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseYear;
    }

    public int getId() {
        return id;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public double getKinoRating() {
        return kinoRating;
    }

    public String getRuTitle() {
        return ruTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public void setKinoRating(double kinoRating) {
        this.kinoRating = kinoRating;
    }

    public void setRuTitle(String ruTitle) {
        this.ruTitle = ruTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
