package com.kaleidoscope.filmadviser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.kaleidoscope.filmadviser.util.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONObject jsonObject = NetworkUtils.loadJsonFromConnection(0, 1);

        if (jsonObject != null) {
            Log.i("RESULT", jsonObject.toString());
        }
        else
            Log.i("RESULT", "null object");
    }
}