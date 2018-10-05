package com.swdave.popular_movies_app_final.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.activities.DetailActivity;
import com.swdave.popular_movies_app_final.activities.MainActivity;
import com.swdave.popular_movies_app_final.model.Results;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private ArrayList<Results> results;
    private Context context;


    public MovieRecyclerViewAdapter(Context context, ArrayList<Results> results) {
        this.context = context;
        this.results = results;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {

        // Loading img from Internet with Glide

        String smallPoster = "https://image.tmdb.org/t/p/w200" + results.get(position).getPosterPath();


        Glide.with(context)
                .load(smallPoster)
                .into(holder.thumbImg);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", results.get(position).getTitle());
                intent.putExtra("overview", results.get(position).getOverview());
                intent.putExtra("releaseDate", results.get(position).getReleaseDate());
                intent.putExtra("userRating", results.get(position).getVoteAverage());
                intent.putExtra("backDrop", results.get(position).getBackdropPath());


                String itemClicked = results.get(position).getTitle();
                Toast.makeText(context, "You clicked: " + itemClicked, Toast.LENGTH_SHORT).show();

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbImg;
        RelativeLayout parentLayout;

        MovieViewHolder(View itemView) {
            super(itemView);

            thumbImg = itemView.findViewById(R.id.posterImg);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }

}
