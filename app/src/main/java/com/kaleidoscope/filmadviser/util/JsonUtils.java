package com.kaleidoscope.filmadviser.util;

import com.kaleidoscope.filmadviser.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static int id;
    private static double imdbRate;
    private static double kinoRate;
    private static String title;
    private static String originalTitle;
    private static String overview;
    private static String posterPath;
    private static String backDropPath;
    private static String releaseYear;

    private static final String KEY_ITEMS = "items";
    private static final String KEY_VOTE_RATING = "ratingKinopoisk";
    private static final String KEY_ID = "kinopoiskId";
    private static final String KEY_TITLE = "nameRu";
    private static final String KEY_ORIGINAL_TITLE = "nameEn";
    private static final String KEY_POSTER_PATH = "posterUrl";
    private static final String KEY_BACKDROP_PATH = "posterUrlPreview";
    private static final String KEY_RATING = "ratingImdb";
    private static final String KEY_RELEASE_YEAR = "year";


    public static ArrayList<Movie> getMoviesFromJson(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null)
            return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ITEMS);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                if (objectMovie.isNull(KEY_BACKDROP_PATH))
                    continue;
                if (!objectMovie.isNull(KEY_ID))
                    id = objectMovie.getInt(KEY_ID);
                if (!objectMovie.isNull(KEY_VOTE_RATING))
                    imdbRate = objectMovie.getDouble(KEY_VOTE_RATING);
                if (!objectMovie.isNull(KEY_TITLE))
                    title = objectMovie.getString(KEY_TITLE);
                if (!objectMovie.isNull(KEY_ORIGINAL_TITLE))
                    originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE);
                if (!objectMovie.isNull(KEY_POSTER_PATH))
                    posterPath = objectMovie.getString(KEY_POSTER_PATH);
                if (!objectMovie.isNull(KEY_BACKDROP_PATH))
                    backDropPath = objectMovie.getString(KEY_BACKDROP_PATH);
                if (!objectMovie.isNull(KEY_RATING))
                    kinoRate = objectMovie.getDouble(KEY_RATING);
                if (!objectMovie.isNull(KEY_RELEASE_YEAR))
                    releaseYear = objectMovie.getString(KEY_RELEASE_YEAR);
                if (!objectMovie.isNull())             // TODO: Subrequest about move description; check other code
                Movie movie = new Movie(id, imdbRate, kinoRate, title, originalTitle, posterPath, backDropPath, releaseYear, overview);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
