package com.kaleidoscope.filmadviser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kaleidoscope.filmadviser.data.Movie;
import com.kaleidoscope.filmadviser.util.JsonUtils;
import com.kaleidoscope.filmadviser.util.NetworkUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONObject jsonObject = NetworkUtils.loadJsonFromConnection(0, 2);


        ArrayList<Movie> movies = JsonUtils.getMoviesFromJson(jsonObject);

        StringBuilder builder = new StringBuilder();
        for (Movie movie : movies) {
            builder.append(movie.getRuTitle()).append("\n");
        }

        Log.i("RESULT", builder.toString());


//        if (jsonObject != null) {
//            Log.i("RESULT", jsonObject.toString());
//        }
//        else
//            Log.i("RESULT", "null object");
    }
}