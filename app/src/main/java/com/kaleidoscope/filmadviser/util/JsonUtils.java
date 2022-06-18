package com.kaleidoscope.filmadviser.util;

import android.util.Log;

import com.kaleidoscope.filmadviser.data.Movie;
import com.kaleidoscope.filmadviser.data.Review;
import com.kaleidoscope.filmadviser.data.Trailer;

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

    // reviews keys
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATE = "date";
    private static final String KEY_DESCRIPTION = "description";

    // videos keys
    private static final String KEY_URL_VIDEO = "url";
    private static final String KEY_NAME = "name";


    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject) {
        ArrayList<Review> result = new ArrayList<>();
        if (jsonObject == null)
            return result;

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ITEMS);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObjectReview = jsonArray.getJSONObject(i);
                if (!jsonObjectReview.isNull(KEY_AUTHOR) && !jsonObjectReview.isNull(KEY_TYPE) &&
                        !jsonObjectReview.isNull(KEY_DATE) && !jsonObjectReview.isNull(KEY_DESCRIPTION)) {
                    String author = jsonObjectReview.getString(KEY_AUTHOR);
                    String type = jsonObjectReview.getString(KEY_TYPE);
                    String date = jsonObjectReview.getString(KEY_DATE);
                    String description = jsonObjectReview.getString(KEY_DESCRIPTION);
                    Review review = new Review(author, description, date, type);
                    result.add(review);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static ArrayList<Trailer> getTrailersFromJSON(JSONObject jsonObject) {
        ArrayList<Trailer> result = new ArrayList<>();
        if (jsonObject == null)
            return result;

        try {
            Log.i("JSON OUTPUT:", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ITEMS);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObjectVideo = jsonArray.getJSONObject(i);
                if (!jsonObjectVideo.isNull(KEY_URL_VIDEO) && !jsonObjectVideo.isNull(KEY_NAME)) {
                    String url = jsonObjectVideo.getString(KEY_URL_VIDEO);
                    String name = jsonObjectVideo.getString(KEY_NAME);
                    Trailer trailer = new Trailer(url, name);
                    result.add(trailer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static ArrayList<Movie> getMoviesFromJson(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null)
            return result;
        try {
            Log.i("OUTPUT", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ITEMS);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                if (objectMovie.isNull(KEY_BACKDROP_PATH) || objectMovie.isNull(KEY_POSTER_PATH))
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
                Movie movie = new Movie(id, imdbRate, kinoRate, title, originalTitle, posterPath, backDropPath, releaseYear);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


}



































