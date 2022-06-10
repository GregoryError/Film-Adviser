package com.kaleidoscope.filmadviser.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

// 'https://kinopoiskapiunofficial.tech/api/v2.2/films
// ?order=RATING
// &type=ALL
// &ratingFrom=0
// &ratingTo=10
// &yearFrom=1000
// &yearTo=3000
// &page=1'

public class NetworkUtils {
    private static final String BASE_URL = "https://kinopoiskapiunofficial.tech/api/v2.2/films";
    private static final String API_KEY = "e7522639-089f-4044-a22a-ef5a2d5000da";

    // Options
    public static final String OPTION_TOP250 = "top?type=TOP_250_BEST_FILMS";
    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;
    public static final String OPTION_TYPE_ALL = "ALL";
    public static final String OPTION_TYPE_FILM = "FILM";
    public static final String OPTION_TYPE_TV_SHOW = "TV_SHOW";
    public static final String OPTION_TYPE_TV_SERIES = "TV_SERIES";
    public static final String OPTION_TYPE_MINI_SERIES = "MINI_SERIES";


    //sort by, page
    private static final String OPTION_SORT = "?order=";
    private static final String OPTION_PAGE = "&page=";
    private static final String OPTION_TYPE = "&type=";
    private static final String SORT_BY_RATING = "RATING";
    private static final String SORT_BY_NUM_VOTE = "NUM_VOTE";
    private static final String SORT_BY_NUM_YEAR = "NUM_YEAR";
    private static final String OPTION_RATING_FROM = "&ratingFrom=";
    private static final String OPTION_RATING_TO = "&ratingTo=";

    // TODO: more OPTIONS

    public static Request buildRequestReviews(int id) {
        StringBuilder strURL = new StringBuilder();
        strURL.append(BASE_URL + '/')
                .append(Integer.toString(id))
                .append("/reviews?page=1&order=DATE_DESC");
        Request result = new Request.Builder()
                .url(strURL.toString())
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", API_KEY)
                .build();
        return result;
    }

    public static Request buildRequestVideos(int id) {
        StringBuilder strURL = new StringBuilder();
        strURL.append(BASE_URL + '/')
                .append(Integer.toString(id))
                .append("/videos");
        Request result = new Request.Builder()
                .url(strURL.toString())
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", API_KEY)
                .build();
        return result;
    }

    public static JSONObject loadJsonForReviews(int id) {
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(buildRequestReviews(id)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject loadJsonForVideos(int id) {
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(buildRequestVideos(id)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static Request buildRequest(int sortBy, int page) {
        Request result = null;

        String sortMethod = null;
        if (sortBy == POPULARITY) {
            sortMethod = SORT_BY_NUM_VOTE;
        } else {
            sortMethod = SORT_BY_RATING;
        }

        StringBuilder strURL = new StringBuilder();

        strURL.append(BASE_URL)
                .append(OPTION_SORT).append(sortMethod)
                .append(OPTION_TYPE).append(OPTION_TYPE_ALL)
                .append(OPTION_RATING_FROM).append(Integer.toString(0))
                .append(OPTION_RATING_TO).append(Integer.toString(10))
                .append("&yearFrom=1000&yearTo=3000")
                .append(OPTION_PAGE).append(Integer.toString(page));


        result = new Request.Builder()
                .url(strURL.toString())
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", API_KEY)
                .build();

        return result;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public interface OnStartLoadingListener {
            void onStartLoading();
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener != null) {
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null)
                return null;

            String urlString = bundle.getString("url");
            Request request = new Request.Builder()
                    .url(urlString)
                    .addHeader("accept", "application/json")
                    .addHeader("X-API-KEY", API_KEY)
                    .build();

            JSONObject jsonObject = null;
            if (request == null) {
                return null;
            } else {
                //OkHttpClient okHttpClient = new OkHttpClient();

                OkHttpClient okHttpClient; //  = new OkHttpClient();
                OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
                httpBuilder.connectTimeout(15, TimeUnit.SECONDS);
                httpBuilder.readTimeout(15, TimeUnit.SECONDS);
                httpBuilder.writeTimeout(15, TimeUnit.SECONDS);
                okHttpClient = httpBuilder.build();

                try {
                    jsonObject = new JSONObject(okHttpClient.newCall(request).execute().body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }
        }
    }

    private static class JSONLoadTask extends AsyncTask<Request, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Request... requests) {
            JSONObject jsonObject = null;
            if (requests == null || requests.length == 0) {
                return null;
            } else {
                OkHttpClient okHttpClient; //  = new OkHttpClient();
                OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
                httpBuilder.connectTimeout(20, TimeUnit.SECONDS);
                httpBuilder.readTimeout(20, TimeUnit.SECONDS);
                httpBuilder.writeTimeout(20, TimeUnit.SECONDS);
                okHttpClient = httpBuilder.build();
                try {
                    jsonObject = new JSONObject(okHttpClient.newCall(requests[0]).execute().body().string());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
    }

    public static JSONObject loadJsonFromConnection(int sortBy, int page) {
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(buildRequest(sortBy, page)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDescription(int kId) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + Integer.toString(kId))
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", API_KEY)
                .build();
        try {
            return new JSONLoadTask().execute(request).get().getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Отсутствует";
    }

}


















