package com.kaleidoscope.filmadviser;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaleidoscope.filmadviser.adapters.ReviewAdapter;
import com.kaleidoscope.filmadviser.adapters.TrailerAdapter;
import com.kaleidoscope.filmadviser.data.FavoriteMovie;
import com.kaleidoscope.filmadviser.data.MainViewModel;
import com.kaleidoscope.filmadviser.data.Movie;
import com.kaleidoscope.filmadviser.data.Review;
import com.kaleidoscope.filmadviser.data.Trailer;
import com.kaleidoscope.filmadviser.util.JsonUtils;
import com.kaleidoscope.filmadviser.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseYear;
    private TextView textViewOverview;
    private String description;
    private ImageView imageViewFavorite;
    private ScrollView scrollViewInfo;

    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    private int id;
    private Movie movie;
    private FavoriteMovie favoriteMovie;

    private MainViewModel viewModel;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavorite:
                Intent intentToFavorite = new Intent(this, FavoriteActivity.class);
                startActivity(intentToFavorite);
                break;
            case R.id.action_share:
                // TODO: share activity + url to
                Intent intent_share = new Intent();
                intent_share.setAction(Intent.ACTION_SEND);
                intent_share.putExtra(Intent.EXTRA_TEXT, "Я нашел этот фильм на Film Adviser\n" +
                        movie.getPosterPath() +
                        "\n\n Попробуй Film Adviser тут:");
                intent_share.setType("text/plain");
                startActivity(intent_share);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
        imageViewFavorite = findViewById(R.id.imageViewAddToFavorite);
        scrollViewInfo = findViewById(R.id.scrollViewInfo);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        } else {
            finish();
        }

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (!intent.hasExtra("isFavorite")) {
            movie = viewModel.getMovieById(id);
        } else {
            movie = viewModel.getFavoriteMovieById(id);
            Log.i("OTPUT", movie.toString());
        }

        description = NetworkUtils.getDescription(id);

        Picasso.get().load(movie.getPosterPath()).into(imageViewBigPoster);
        textViewTitle.setText(movie.getRuTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(String.format("kinopoisk: %s, IMDB: %s", movie.getKinoRating(), movie.getImdbRating()));
        textViewReleaseYear.setText(movie.getReleaseYear());
        textViewOverview.setText(movie.getOverview());
        textViewOverview.setText(description);
        setFavorite();

        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        trailerAdapter = new TrailerAdapter();
        reviewAdapter = new ReviewAdapter();

        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(String url) {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailer);
            }
        });

        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setAdapter(trailerAdapter);
        JSONObject jsonObjectTrailers = NetworkUtils.loadJsonForVideos(movie.getId());
        JSONObject jsonObjectReviews = NetworkUtils.loadJsonForReviews(movie.getId());
        ArrayList<Trailer> trailers = JsonUtils.getTrailersFromJSON(jsonObjectTrailers);
        ArrayList<Review> reviews = JsonUtils.getReviewsFromJSON(jsonObjectReviews);
        reviewAdapter.setReviews(reviews);
        trailerAdapter.setTrailers(trailers);

      //  scrollViewInfo.scrollTo(0, 0);
    }

    public void onClickChangeFavorite(View view) {
        favoriteMovie = viewModel.getFavoriteMovieById(id);
        if (favoriteMovie == null) {
            viewModel.insertFavoriteMovie(new FavoriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavoriteMovies(favoriteMovie);
            Toast.makeText(this, R.string.delete_from_favorite, Toast.LENGTH_SHORT).show();
        }
        setFavorite();
    }

    private void setFavorite() {
        favoriteMovie = viewModel.getFavoriteMovieById(id);
        if (favoriteMovie == null) {
            imageViewFavorite.setImageResource(R.drawable.heart_empty);
        } else {
            imageViewFavorite.setImageResource(R.drawable.heart_filed);
        }
    }
}

























