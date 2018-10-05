package com.swdave.popular_movies_app_final.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swdave.popular_movies_app_final.R;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: Started");
        getIncomingIntent();
        setTitle(mTitle);

    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: Checking for Intents");

        if (getIntent().hasExtra("title")
                && getIntent().hasExtra("overview")
                && getIntent().hasExtra("releaseDate")
                && getIntent().hasExtra("userRating")
                && getIntent().hasExtra("backDrop")) {
            Log.d(TAG, "getIncomingIntent: Found Extras");

            String title = getIntent().getStringExtra("title");
            String overview = getIntent().getStringExtra("overview");
            String releaseDate = getIntent().getStringExtra("releaseDate");
            String userReview = getIntent().getStringExtra("userRating");
            String posterUrl = getIntent().getStringExtra("backDrop");

            setDetail(title, overview, releaseDate, userReview, posterUrl);
            Log.d(TAG, "getIncomingIntent: Intents completed");
        }
    }

    private void setDetail(String title, String overview, String releaseDate, String userRating, String posterUrl) {

        TextView movieTitle = findViewById(R.id.movie_title);
        movieTitle.setText(title);
        mTitle = title;

        TextView movieOverview = findViewById(R.id.synopsis);
        movieOverview.setText(overview);

        TextView movieUserRating = findViewById(R.id.user_rating_data);
        movieUserRating.setText(userRating);

        TextView movieReleaseDate = findViewById(R.id.release_date_data);
        movieReleaseDate.setText(releaseDate);

        ImageView image = findViewById(R.id.movie_backdrop);
        Glide.with(this)
                .load(posterUrl)
                .into(image);

    }
}
