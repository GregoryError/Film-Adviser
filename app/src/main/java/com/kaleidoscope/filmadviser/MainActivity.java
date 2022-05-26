package com.kaleidoscope.filmadviser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.kaleidoscope.filmadviser.adapters.MovieAdapter;
import com.kaleidoscope.filmadviser.data.MainViewModel;
import com.kaleidoscope.filmadviser.data.Movie;
import com.kaleidoscope.filmadviser.util.JsonUtils;
import com.kaleidoscope.filmadviser.util.NetworkUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewTopRated;
    private TextView textViewPopularity;
    private MainViewModel viewModel;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);

        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);
        switchSort = findViewById(R.id.switchSort);
        switchSort.setChecked(true);
        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {

            @Override
            public void onPosterClick(int pos) {
                Movie movie = movieAdapter.getMovies().get(pos);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }
        });

        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
               // Toast.makeText(MainActivity.this, "Конец списка!", Toast.LENGTH_SHORT).show();
            }
        });

        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setMethodOfSort(b);
            }
        });

        switchSort.setChecked(false);

        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMovies(movies);
            }
        });
    }

    public void onClickSetPopularity(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    public void onClickSetTopRated(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }


    private void setMethodOfSort(boolean isTopRated) {
        int methodOfSort;
        if (isTopRated) {
            textViewTopRated.setTextColor(getResources().getColor(R.color.red_200));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            methodOfSort = NetworkUtils.TOP_RATED;
        } else {
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
            textViewPopularity.setTextColor(getResources().getColor(R.color.red_200));
            methodOfSort = NetworkUtils.POPULARITY;
        }

        downloadData(methodOfSort, 1);

    }

    private void downloadData(int methodOfSort, int page) {
        JSONObject jsonObject = NetworkUtils.loadJsonFromConnection(methodOfSort, 1);
        ArrayList<Movie> movies = JsonUtils.getMoviesFromJson(jsonObject);
        if (movies != null && !movies.isEmpty()) {
            viewModel.deleteAllMovies();
            for (Movie m : movies) {
                viewModel.insertMovie(m);
            }
        }

    }
}


































