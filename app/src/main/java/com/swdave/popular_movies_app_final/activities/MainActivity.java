package com.swdave.popular_movies_app_final.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.adapters.MovieRecyclerViewAdapter;
import com.swdave.popular_movies_app_final.api.JsonApi;
import com.swdave.popular_movies_app_final.model.JSONResponse;
import com.swdave.popular_movies_app_final.model.Results;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<Results> results;
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter adapter;

    private ProgressBar progressBar;
    private ImageView noConnection;
    private TextView errorText;
    private Button retryButton;
    private JsonApi jsonApi;
    private static final String API_KEY = "a1a00c2be1584838bc50f724c943db32";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build RetroFit





        // No Network icon
        noConnection = findViewById(R.id.no_network_iv);
        noConnection.setVisibility(View.GONE);
        // Error Text
        errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


        recyclerView = findViewById(R.id.recycler_view);
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

    private void checkApi(){
        if (API_KEY.length() > 1){
            initViews();
        }else {
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
    }

    private void callPopularMovies() {
        Log.d(TAG, "callPopularMovies: Started");


        Call<JSONResponse> call = jsonApi.getPopular(API_KEY);

        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                results = new ArrayList<>(Arrays.asList(jsonResponse.getResults()));
                adapter = new MovieRecyclerViewAdapter(MainActivity.this, results);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                errorText.setText("");
                errorText.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                errorText.setText("Error" + t.getMessage());


            }
        });

    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        // spaces out grid evenly
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
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

        buildBaseUrl();

        callPopularMovies();


    }
}
