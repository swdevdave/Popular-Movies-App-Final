package com.swdave.popular_movies_app_final.api;

import com.swdave.popular_movies_app_final.model.JSONResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonApi {

    // GET  posts
    @GET("popular")
    Call<JSONResponse> getPopular(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<JSONResponse> getTopRated(@Query("api_key") String apiKey);

}
