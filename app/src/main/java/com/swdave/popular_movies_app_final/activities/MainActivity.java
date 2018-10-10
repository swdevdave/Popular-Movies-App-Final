package com.swdave.popular_movies_app_final.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.adapters.MovieAdapter;
import com.swdave.popular_movies_app_final.api.JsonApi;
import com.swdave.popular_movies_app_final.model.MovieResponse;
import com.swdave.popular_movies_app_final.model.MovieResults;
import com.swdave.popular_movies_app_final.viewModel.FavoritesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private List<MovieResults> results;


    private RecyclerView movieRecyclerView;

    private MovieAdapter mMovieAdapter;



    private ProgressBar progressBar;
    private ImageView noConnection;
    private TextView errorText;
    private Button retryButton;
    private JsonApi jsonApi;

    public static final String API_KEY = "a1a00c2be1584838bc50f724c943db32";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private int movieFlag = 1;

    private FavoritesViewModel favoritesViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // No Network icon
        noConnection = findViewById(R.id.no_network_iv);
        noConnection.setVisibility(View.GONE);
        // Error Text
        errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


        movieRecyclerView = findViewById(R.id.main_recycler_view);

        // Retry button for Errors
        retryButton = findViewById(R.id.reset_button);
        retryButton.setVisibility(View.GONE);
        retryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // click for retry TODO
            }
        });

        checkApi();


    }

    private void checkApi() {
        if (API_KEY.length() > 1) {
            buildBaseUrl();

        } else {
            errorText.setText("");
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.no_api_key);

            retryButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noConnection.setVisibility(View.GONE);


        }
    }

    private void buildBaseUrl() {
        Log.d(TAG, "buildBaseUrl: Started");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);
        initViews();
    }


    private void callMovies() {

        Call<MovieResponse> call = jsonApi.getPopular(API_KEY);

        if (movieFlag == 1) {
            call = jsonApi.getPopular(API_KEY);
        } if (movieFlag == 2) {
            call = jsonApi.getTopRated(API_KEY);
        }

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                MovieResponse jsonMovieResponse = response.body();
                results = new ArrayList<>(Arrays.asList(jsonMovieResponse.getResults()));
                mMovieAdapter = new MovieAdapter(MainActivity.this, results);
                movieRecyclerView.setAdapter(mMovieAdapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                errorText.setText("");
                errorText.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                errorText.setText("Error" + t.getMessage());


            }
        });

    }


    private void initViews() {
        movieRecyclerView = findViewById(R.id.main_recycler_view);
        movieRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieRecyclerView.setLayoutManager(layoutManager);

        // spaces out grid evenly
        movieRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 2;
                int spacing = 10;//spacing between views in grid

                if (position >= 0) {
                    int column = position % spanCount; // item column

                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });

        checkFlag();

    }

    private void checkFlag(){
        if (movieFlag == 3) {
            callFavs();
        } else {
            callMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular:
                movieFlag = 1;
                checkFlag();
                return true;
            case R.id.sort_by_top_rated:
                movieFlag = 2;
                checkFlag();
                return true;
            case R.id.sort_by_favorites:
                movieFlag = 3;
                checkFlag();
                return true;
            case R.id.delete_favs:
                favoritesViewModel.deleteAllFavorites();
                checkFlag();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callFavs(){

        mMovieAdapter = new MovieAdapter(MainActivity.this, results);
        movieRecyclerView.setAdapter(mMovieAdapter);

        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        favoritesViewModel.getAllFavorites().observe(this, new Observer<List<MovieResults>>() {
            @Override
            public void onChanged(@Nullable List<MovieResults> movieResults) {
                mMovieAdapter.setFavs(movieResults);
            }
        });
    }

//    private void checkForDeleteAll(){
//        if
//
//    }
}

