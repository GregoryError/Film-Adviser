package com.kaleidoscope.filmadviser.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorite_movies")
public class FavoriteMovie extends Movie{
    public FavoriteMovie(int uniqueId, int id, double imdbRating, double kinoRating, String ruTitle, String originalTitle, String posterPath, String backdropPath, String releaseYear) {
        super(uniqueId, id, imdbRating, kinoRating, ruTitle, originalTitle, posterPath, backdropPath, releaseYear);
    }

    @Ignore
    public FavoriteMovie(Movie movie) {
        super(movie.getUniqueId(), movie.getId(), movie.getImdbRating(), movie.getKinoRating(), movie.getRuTitle(), movie.getOriginalTitle(), movie.getPosterPath(), movie.getBackdropPath(), movie.getReleaseYear());
    }
}
