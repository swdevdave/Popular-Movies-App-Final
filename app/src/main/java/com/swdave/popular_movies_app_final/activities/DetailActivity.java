package com.swdave.popular_movies_app_final.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.adapters.ReviewAdapter;
import com.swdave.popular_movies_app_final.adapters.TrailerAdapter;
import com.swdave.popular_movies_app_final.api.JsonApi;
import com.swdave.popular_movies_app_final.database.FavoritesDatabase;
import com.swdave.popular_movies_app_final.executer.AppExecutors;
import com.swdave.popular_movies_app_final.model.MovieResults;
import com.swdave.popular_movies_app_final.model.ReviewResponse;
import com.swdave.popular_movies_app_final.model.ReviewResults;
import com.swdave.popular_movies_app_final.model.TrailerResponse;
import com.swdave.popular_movies_app_final.model.TrailerResults;
import com.swdave.popular_movies_app_final.viewModel.FavoritesViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailActivity extends AppCompatActivity {

    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    // UI
    private FloatingActionButton fab;

    // Data
    private JsonApi jsonApi;
    private MovieResults mMovieResults;
    private int mMovieId;
    private String mTitle;
    private FavoritesDatabase mDatabase;
    private FavoritesViewModel mFavoritesViewModel;


    // Trailers
    private int mTrailerSize = 0;
    private TextView mTrailersFound;
    private ArrayList<TrailerResults> mTrailerResults;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRecyclerView;

    // Reviews
    private int mReviewSize = 0;
    private TextView mReviewsFound;
    private ArrayList<ReviewResults> mReviewResults;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mReviewRecyclerView;

    // Favs
    private Boolean mCheckResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getIncomingIntent();
        setTitle(mTitle);

        mDatabase = FavoritesDatabase.getInstance(getApplicationContext());

        mFavoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        fab = findViewById(R.id.fab_favorites);

        checkIfExists task = new checkIfExists();
        task.execute();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabButtonClicked();
            }
        });
    }

    private void getIncomingIntent() {

        Intent intent = getIntent();
        mMovieResults = intent.getParcelableExtra("Movie");

        mMovieId = mMovieResults.getId();
        String mVoteAverage = mMovieResults.getVoteAverage();
        mTitle = mMovieResults.getTitle();
        String mBackdropPath = mMovieResults.getBackdropPath();
        String mOverview = mMovieResults.getOverview();
        String mReleaseDate = mMovieResults.getReleaseDate();

        TextView movieTitle = findViewById(R.id.movie_title);
        movieTitle.setText(mTitle);

        TextView movieOverview = findViewById(R.id.synopsis);
        movieOverview.setText(mOverview);

        TextView movieUserRating = findViewById(R.id.user_rating_data);
        movieUserRating.setText(mVoteAverage);

        TextView movieReleaseDate = findViewById(R.id.release_date_data);

        // Takes yyyy-MM-dd converts to MM/dd/yyyy
        String[] parts = mReleaseDate.split("-");
        String updatedReleaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];
        movieReleaseDate.setText(updatedReleaseDate);

        ImageView image = findViewById(R.id.movie_backdrop);
        Glide.with(this)
                .load(BASE_IMG_URL + mBackdropPath)
                .into(image);


        buildBaseUrl();
        callTrailer();
        callReviews();

    }


    private void buildBaseUrl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);
        initTrailerRecyclerView();
        initReviewRecyclerView();
    }


    private void callReviews() {

        Call<ReviewResponse> call = jsonApi.getReviews(mMovieId, MainActivity.API_KEY);

        call.enqueue(new Callback<ReviewResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {

                ReviewResponse reviewResponse = response.body();

                assert reviewResponse != null;
                mReviewResults = new ArrayList<>(Arrays.asList(reviewResponse.getReviewResults()));
                mReviewAdapter = new ReviewAdapter(mReviewResults);
                mReviewRecyclerView.setAdapter(mReviewAdapter);

                mReviewSize = mReviewResults.size();
                mReviewsFound = findViewById(R.id.reviews_found);

                if (mReviewSize > 0) {
                    mReviewsFound.setText("Found " + mReviewSize + " Movie Reviews");
                } else {
                    mReviewsFound.setText(R.string.no_reviews_found);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, Throwable t) {
                mReviewsFound.setText(R.string.error_parsing_reviews);
            }
        });

    }

    private void callTrailer() {

        Call<TrailerResponse> call = jsonApi.getTrailers(mMovieId, MainActivity.API_KEY);

        call.enqueue(new Callback<TrailerResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {

                TrailerResponse trailerResponse = response.body();

                assert trailerResponse != null;
                mTrailerResults = new ArrayList<>(Arrays.asList(trailerResponse.getResults()));
                mTrailerAdapter = new TrailerAdapter(mTrailerResults, getApplicationContext());
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);

                mTrailerSize = mTrailerResults.size();
                mTrailersFound = findViewById(R.id.trailers_found);

                if (mTrailerSize > 0) {
                    mTrailersFound.setText("Found " + mTrailerSize + " Movie Trailers");
                } else {
                    mTrailersFound.setText(R.string.no_trailers_found);
                }

            }

            @Override
            public void onFailure(@NonNull Call<TrailerResponse> call, Throwable t) {
                mTrailersFound.setText(R.string.trailer_parse_error);
            }
        });

    }

    private void initTrailerRecyclerView() {

        mTrailerRecyclerView = findViewById(R.id.trailer_rv);
        mTrailerRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);

    }

    private void initReviewRecyclerView() {
        mReviewRecyclerView = findViewById(R.id.reviews_rv);
        mReviewRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(layoutManager);

    }

    public void fabButtonClicked() {

        if (mCheckResult) {

            AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.favoritesDao().deleteThisMovie(mMovieId);

                }
            });
            fab.setImageResource(R.drawable.ic_star_empty);
            mCheckResult = false;
            Toast.makeText(this, "Deleted " + mTitle + " from Favorites", Toast.LENGTH_SHORT).show();
        } else {
            mFavoritesViewModel.insert(mMovieResults);
            fab.setImageResource(R.drawable.ic_star_yellow);
            mCheckResult = true;
            Toast.makeText(this, "Added " + mTitle + " to Favorites", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("StaticFieldLeak")
    protected class checkIfExists extends AsyncTask<Integer, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            Boolean checkResult;
            Integer checkMovieId = mDatabase.favoritesDao().ifExists(mMovieResults.getId());
            checkResult = checkMovieId > 0;
            return checkResult;
        }


        @Override
        protected void onPostExecute(Boolean checkResult) {
            mCheckResult = checkResult;
            if (mCheckResult) {
                fab.setImageResource(R.drawable.ic_star_yellow);
            } else {
                fab.setImageResource(R.drawable.ic_star_empty);
            }
        }
    }
}
