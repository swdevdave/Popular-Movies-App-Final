package com.swdave.popular_movies_app_final.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;


import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.adapters.MovieRecyclerViewAdapter;
import com.swdave.popular_movies_app_final.api.JsonApi;
import com.swdave.popular_movies_app_final.model.Movies;
import com.swdave.popular_movies_app_final.model.Results;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MainActivity";


    private List<Results> resultsList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView noConnection;
    private TextView errorText;
    private String mUrl;
    private Button retryButton;
    private JsonApi jsonApi;
    private String api_key = "a1a00c2be1584838bc50f724c943db32";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build RetroFit
        buildBaseUrl();
        callPopularMovies();

        // No Network icon
        noConnection = findViewById(R.id.no_network_iv);
        noConnection.setVisibility(View.GONE);
        // Error Text
        errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);
        resultsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        // Retry button for Errors
        retryButton = findViewById(R.id.reset_button);
        retryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // click for retry TODO
            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    private void buildBaseUrl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);
    }

    private void callPopularMovies(){

        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        noConnection.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);

        Call<Movies> call = jsonApi.getPopular(api_key);

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Error: " + (response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<Results> resultsArrayList = response.body().getResults();

                for (int i =0; i<resultsArrayList.size(); i++) {

                    String imgPosterUrl = "https://image.tmdb.org/t/p/w300";

                    Results results = new Results();

                    results.setId(resultsArrayList.get(i).getId());
                    results.setVoteAverage(resultsArrayList.get(i).getVoteAverage());
                    results.setPosterPath(imgPosterUrl + resultsArrayList.get(i).getPosterPath());
                    results.setBackdropPath(imgPosterUrl + resultsArrayList.get(i).getBackdropPath());
                    results.setOverview(resultsArrayList.get(i).getOverview());

                    // Takes yyyy-MM-dd converts to MM/dd/yyyy
                    String baseDate = resultsArrayList.get(i).getReleaseDate();
                    String[] parts = baseDate.split("-");
                    String releaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];
                    results.setReleaseDate(releaseDate);

                    resultsList.add(results);

//                    Log.d(TAG, "onResponse: \n" +
//                            "id: " + resultsArrayList.get(i).getId() + "\n" +
//                            "Vote Average: " + resultsArrayList.get(i).getVoteAverage() + "\n" +
//                            "Title: " + resultsArrayList.get(i).getTitle() + "\n" +
//                            "Poster Path: " + resultsArrayList.get(i).getPosterPath() + "\n" +
//                            "Backdrop Path: " + resultsArrayList.get(i).getBackdropPath() + "\n" +
//                            "Overview: " + resultsArrayList.get(i).getOverview() + "\n" +
//                            "Release Date: " + resultsArrayList.get(i).getReleaseDate() + "\n" +
//                            "-----------------------------------------------------------------\n\n"
//                    );

                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + (t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void jsonParse() {
//
//        Log.d(TAG, "jsonParse: Started");
//        progressBar.setVisibility(View.VISIBLE);
//        errorText.setVisibility(View.GONE);
//        noConnection.setVisibility(View.GONE);
//        retryButton.setVisibility(View.GONE);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("results");
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject results = jsonArray.getJSONObject(i);
//
//                        Movies movies = new Movies();
//
//                        // Extract movie poster Url and create new url
//                        String imgThumbUrl = "https://image.tmdb.org/t/p/w185";
//                        String imgPosterUrl = "https://image.tmdb.org/t/p/w300";
//                        String baseImgUrl = results.getString("poster_path");
//                        // Small img for grid view
//                        movies.setmThumbnailImgUrl(imgThumbUrl + baseImgUrl);
//                        // large img for Details page
//                        movies.setmPosterUrl(imgPosterUrl + baseImgUrl);
//
//                        movies.setmTitle(results.getString("title"));
//                        movies.setmOverview(results.getString("overview"));
//                        movies.setmUserRating(results.getString("vote_average"));
//
//                        // Takes yyyy-MM-dd converts to MM/dd/yyyy
//                        String baseDate = results.getString("release_date");
//                        String[] parts = baseDate.split("-");
//                        String releaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];
//                        movies.setmReleaseDate(releaseDate);
//
//                        movieList.add(movies);
//                        Log.d(TAG, "onResponse: Received the following " + "\n"
//                                + movies.getmTitle() + "\n"
//                                + movies.getmThumbnailImgUrl() + "\n"
//                                + movies.getmPosterUrl() + "\n"
//                                + movies.getmOverview() + "\n"
//                                + movies.getmReleaseDate() + "\n"
//                                + movies.getmUserRating() + "\n");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                progressBar.setVisibility(View.GONE);
//                startRecyclerView(movieList);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//                // Volley error handler
//                errorText.setText("");
//                errorText.setVisibility(View.VISIBLE);
//                retryButton.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//
//                if (volleyError instanceof NetworkError) {
//                    errorText.setText(R.string.network_error);
//                    noConnection.setVisibility(View.VISIBLE);
//                } else if (volleyError instanceof ServerError) {
//                    errorText.setText(R.string.server_error);
//                    noConnection.setVisibility(View.VISIBLE);
//                } else if (volleyError instanceof AuthFailureError) {
//                    errorText.setText(R.string.auth_failure);
//                    noConnection.setVisibility(View.VISIBLE);
//                } else if (volleyError instanceof ParseError) {
//                    errorText.setText(R.string.parse_error);
//                } else if (volleyError instanceof NoConnectionError) {
//                    errorText.setText(R.string.no_connection);
//                    noConnection.setVisibility(View.VISIBLE);
//                } else if (volleyError instanceof TimeoutError) {
//                    errorText.setText(R.string.time_out);
//                    noConnection.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        mQueue = Volley.newRequestQueue(MainActivity.this);
//        mQueue.add(request);
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

    }

//    private void urlBuilder() {
//        // (1) TODO - Enter your API KEY from https://www.themoviedb.org
//        String apiKey = "";
//        if (apiKey.length() > 1) {
//
//            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//            String contentSelection = sharedPrefs.getString(
//                    getString(R.string.settings_order_by_key),
//                    getString(R.string.settings_order_by_default));
//
//            Uri.Builder builder = new Uri.Builder();
//            builder.scheme("https")
//                    .authority("api.themoviedb.org")
//                    .appendPath("3")
//                    .appendPath("movie")
//                    .appendPath(contentSelection)
//                    //.appendPath("top_rated")
//                    .appendQueryParameter("api_key", apiKey)
//                    .appendQueryParameter("language", "en-US");
//
//            mUrl = builder.build().toString();
//            Log.d(TAG, "urlBuilder: " + mUrl);
//            jsonParse();
//
//        } else {
//            progressBar.setVisibility(View.GONE);
//            errorText.setVisibility(View.VISIBLE);
//            errorText.setText(R.string.no_api_key);
//            retryButton.setVisibility(View.GONE);
//
//        }
//    }

    private void startRecyclerView(List<Results> resultsList) {

        MovieRecyclerViewAdapter movieAdapter = new MovieRecyclerViewAdapter(this, resultsList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
