package com.kaleidoscope.filmadviser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.kaleidoscope.filmadviser.util.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = NetworkUtils.buildUrl(NetworkUtils.POPULARITY, 1).toString();

        Log.i("RESULT", url);

    }
}