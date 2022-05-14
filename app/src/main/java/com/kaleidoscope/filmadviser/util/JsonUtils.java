package com.kaleidoscope.filmadviser.util;

import com.kaleidoscope.filmadviser.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private int id;
    private double imdbRating;
    private double kinoRating;
    private String ruTitle;
    private String originalTitle;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;

    private static final String KEY_ITEMS = "films";
    private static final String KEY_VOTE_RATING = "ratingVoteCount";
    private static final String KEY_ID = "filmId";
    private static final String KEY_TITLE = "nameRu";
    private static final String KEY_ORIGINAL_TITLE = "nameEn";
    private static final String KEY_POSTER_PATH = "posterUrl";
    private static final String KEY_BACKDROP_PATH = "posterUrlPreview";
    private static final String KEY_RATING = "rating";
    private static final String KEY_RELEASE_DAY = "year";


    public static ArrayList<Movie> getMoviesFromJson(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null)
            return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ITEMS);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                double imdbRate = objectMovie.getDouble(KEY_VOTE_RATING);
                String title = objectMovie.getString(KEY_TITLE);
                String originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE);
                String posterPath = objectMovie.getString(KEY_POSTER_PATH);
                String backDropPath = objectMovie.getString(KEY_BACKDROP_PATH);
                double kinoRate = objectMovie.getDouble(KEY_RATING);
                String releaseYear = objectMovie.getString(KEY_RELEASE_DAY);
                Movie movie = new Movie(id, imdbRate, kinoRate, title, originalTitle, posterPath, backDropPath, releaseYear);
                result.add(movie);
            }
         } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
