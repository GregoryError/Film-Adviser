package com.kaleidoscope.filmadviser.util;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class NetworkUtils {
    private static final String BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/films/";
    private static final String API_KEY = "e7522639-089f-4044-a22a-ef5a2d5000da";

    // Options
    public static final String OPTION_TOP250 = "top?type=TOP_250_BEST_FILMS";

    //sort by, page
    private static final String OPTION_PAGE = "&page=";
    private static final String OPTION_SORT = "&order=";
    private static final String SORT_BY_RATING = "RATING";
    private static final String SORT_BY_NUM_VOTE = "NUM_VOTE";
    private static final String SORT_BY_NUM_YEAR = "NUM_YEAR";

    public static final int POPULARITY = 0;
    public static final  int TOP_RATED = 1;


    public static URL buildUrl(int sortBy, int page) {
        URL result = null;
        String sortMethod = null;
        if (sortBy == POPULARITY) {
            sortMethod = SORT_BY_NUM_VOTE;
        } else {
            sortMethod = SORT_BY_RATING;
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(OPTION_SORT, sortMethod)
                .appendQueryParameter(OPTION_PAGE, Integer.toString(page))
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
