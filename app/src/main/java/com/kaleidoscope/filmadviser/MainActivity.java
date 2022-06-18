package com.kaleidoscope.filmadviser;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kaleidoscope.filmadviser.adapters.MovieAdapter;
import com.kaleidoscope.filmadviser.data.FavoriteMovie;
import com.kaleidoscope.filmadviser.data.MainViewModel;
import com.kaleidoscope.filmadviser.data.Movie;
import com.kaleidoscope.filmadviser.util.JsonUtils;
import com.kaleidoscope.filmadviser.util.NetworkUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewTopRated;
    private TextView textViewPopularity;
    private MainViewModel viewModel;
    private ProgressBar progressBarLoading;

    private static final int LOADER_ID = 33;
    private LoaderManager loaderManager;

    private static int page = 1;
    private static int methodOfSort;
    private static boolean isLoading = false;


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

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return (width / 185 > 2) ? width / 185 : 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        loaderManager = LoaderManager.getInstance(this);

        progressBarLoading = findViewById(R.id.progressBarLoading);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);
        switchSort = findViewById(R.id.switchSort);
        switchSort.setChecked(true);

        setUpItemTouchHelper();

        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                page = 1;
                setMethodOfSort(b);
                downloadData(methodOfSort, page);
            }
        });

        switchSort.setChecked(false);

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
                if (!isLoading) {
                    downloadData(methodOfSort, page);
                }
            }
        });

        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
               if (page == 1) {
                   movieAdapter.setMovies(movies);
               }
            }
        });
    }

    private void setMethodOfSort(boolean isTopRated) {
        if (isTopRated) {
            textViewTopRated.setTextColor(getResources().getColor(R.color.red_200));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            methodOfSort = NetworkUtils.TOP_RATED;
        } else {
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
            textViewPopularity.setTextColor(getResources().getColor(R.color.red_200));
            methodOfSort = NetworkUtils.POPULARITY;
        }
    }

    private void downloadData(int methodOfSort, int page) {
        Request request = NetworkUtils.buildRequest(methodOfSort, page);
        Bundle bundle = new Bundle();
        bundle.putString("url", request.url().toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    public void onClickSetPopularity(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    public void onClickSetTopRated(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JsonUtils.getMoviesFromJson(data);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                viewModel.deleteAllMovies();
                movieAdapter.clear();
            }
            for (Movie m : movies) {
                viewModel.insertMovie(m);
            }
            movieAdapter.addMovies(movies);
            ++page;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPos = viewHolder.getAdapterPosition();
                MovieAdapter movAdapter = (MovieAdapter)recyclerViewPosters.getAdapter();
                if (direction == ItemTouchHelper.LEFT)
                    movAdapter.remove(swipedPos);
                if (direction == ItemTouchHelper.RIGHT) {
                    Toast.makeText(MainActivity.this, R.string.add_to_favorite, LENGTH_SHORT).show();
                    // TODO: check if already in favourite
                    Movie movie = movAdapter.getMovieByPos(swipedPos);
                    viewModel.insertFavoriteMovie(new FavoriteMovie(movie));

                }

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerViewPosters);
    }
}


































