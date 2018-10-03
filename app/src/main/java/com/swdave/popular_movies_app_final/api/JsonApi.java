package com.swdave.popular_movies_app_final.api;

import com.swdave.popular_movies_app_final.model.Movies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApi {

    // GET  posts
    @GET("popular")
    Call<Movies> getPopular(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<Movies> getTopRated(@Query("api_key") String apiKey);

}
