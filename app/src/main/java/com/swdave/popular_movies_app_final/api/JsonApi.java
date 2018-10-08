package com.swdave.popular_movies_app_final.api;

import com.swdave.popular_movies_app_final.model.MovieResponse;
import com.swdave.popular_movies_app_final.model.MovieResults;
import com.swdave.popular_movies_app_final.model.ReviewResponse;
import com.swdave.popular_movies_app_final.model.TrailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApi {

    // GET  posts
    @GET("popular")
    Call<MovieResponse> getPopular(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<MovieResponse> getTopRated(@Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<TrailerResponse> getTrailers(
            @Path("id") String postId,
            @Query("api_key") String apiKey
    );

    @GET("{id}/reviews")
    Call<ReviewResponse> getReviews(
            @Path("id") String postId,
            @Query("api_key") String apiKey
    );

}
