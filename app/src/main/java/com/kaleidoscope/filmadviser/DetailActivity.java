package com.kaleidoscope.filmadviser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaleidoscope.filmadviser.data.MainViewModel;
import com.kaleidoscope.filmadviser.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseYear;
    private TextView textViewOverview;

    private  int id;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseYear = findViewById(R.id.textViewReleaseYear);
        textViewOverview = findViewById(R.id.textViewOverview);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        } else {
            finish();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Movie movie = viewModel.getMovieById(id);

        Picasso.get().load(movie.getPosterPath()).into(imageViewBigPoster);
        textViewTitle.setText(movie.getRuTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText( String.format("kinopoisk: %s, IMDB: %s", movie.getKinoRating(), movie.getImdbRating()));
        textViewReleaseYear.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
    }
}








