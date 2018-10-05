package com.swdave.popular_movies_app_final.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.adapters.TrailerAdapter;
import com.swdave.popular_movies_app_final.api.JsonApi;
import com.swdave.popular_movies_app_final.model.MovieResults;
import com.swdave.popular_movies_app_final.model.ReviewResults;
import com.swdave.popular_movies_app_final.model.TrailerResponse;
import com.swdave.popular_movies_app_final.model.TrailerResults;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private String mTitle;
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    FloatingActionButton fab;
    private JsonApi jsonApi;
    private String mMovieId;

    private ArrayList<TrailerResults> trailerResults;

    private ArrayList<ReviewResults> reviewResults;
    private RecyclerView reviewRecyclerView;

    private TrailerAdapter trailerAdapter;
    private RecyclerView trailerRecyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: Started");
        getIncomingIntent();
        setTitle(mTitle);

//        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
//        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
//        trailerRecyclerView.setHasFixedSize(true);

        fab = findViewById(R.id.fab_favorites);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO- FINISH BUTTON TO ADD TO FAVORITES
                Toast.makeText(DetailActivity.this, "This will add to favorites later", Toast.LENGTH_SHORT).show();
                fab.setImageResource(R.drawable.ic_star_yellow);
            }
        });

    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for Intents");



        Intent intent = getIntent();
        MovieResults movieResults = intent.getParcelableExtra("Movie Details");

        String id = movieResults.getId();
        mMovieId = id;
        String voteAverage = movieResults.getVoteAverage();
        String title = movieResults.getTitle();
        mTitle = title;
        String posterPath = movieResults.getPosterPath();
        String backDropPath = movieResults.getBackdropPath();
        String overview = movieResults.getOverview();
        String releaseDate = movieResults.getReleaseDate();

        TextView movieTitle = findViewById(R.id.movie_title);
        movieTitle.setText(title);

        TextView movieOverview = findViewById(R.id.synopsis);
        movieOverview.setText(overview);

        TextView movieUserRating = findViewById(R.id.user_rating_data);
        movieUserRating.setText(voteAverage);

        TextView movieReleaseDate = findViewById(R.id.release_date_data);

        // Takes yyyy-MM-dd converts to MM/dd/yyyy
        String[] parts = releaseDate.split("-");
        String updatedReleaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];
        movieReleaseDate.setText(updatedReleaseDate);

        ImageView image = findViewById(R.id.movie_backdrop);
        Glide.with(this)
                .load(BASE_IMG_URL + backDropPath)
                .into(image);
        Log.d(TAG, "getIncomingIntent: Intents completed");

        //initTrailerView();
        buildBaseUrl();
        callTrailer();
    }


    private void buildBaseUrl() {
        Log.d(TAG, "buildBaseUrl: Started");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);
    }



    private void callTrailer() {
        Log.d(TAG, "callPopularMovies: Started");

        Call<TrailerResponse> call = jsonApi.getTrailers(mMovieId, MainActivity.API_KEY);

        Log.d(TAG, "callTrailer: " + mMovieId);

        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {


                TrailerResponse trailerResponse = response.body();
                trailerResults = new ArrayList<>(Arrays.asList(trailerResponse.getResults()));

//                trailerAdapter = new TrailerAdapter(DetailActivity.this, trailerResults);
//                trailerRecyclerView.setAdapter(trailerAdapter);

                Log.d(TAG, "onResponse: " + trailerResults.get(0).getName());
                Log.d(TAG, "onResponse: " + trailerResults.get(0).getKey());

                Log.d(TAG, "onResponse: " + trailerResults.size());


            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
