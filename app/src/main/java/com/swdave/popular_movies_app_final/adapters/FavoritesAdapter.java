package com.swdave.popular_movies_app_final.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.activities.DetailActivity;
import com.swdave.popular_movies_app_final.model.MovieResults;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavHolder> {

    private List<MovieResults> favs = new ArrayList<>();
    private Context context;

    private static final String SMALL_POSTER_URL = "https://image.tmdb.org/t/p/w200";

    @NonNull
    @Override
    public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        return new FavHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavHolder favHolder, final int position) {


        // Loading img from Internet with Glide
        Glide.with(context)
                .load(SMALL_POSTER_URL + favs.get(position).getPosterPath())
                .into(favHolder.thumbImg);

        favHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Movie Details", favs.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favs.size();
    }

    public void setFavs(List<MovieResults> favs){
        this.favs = favs;
        notifyDataSetChanged();
    }


    class FavHolder extends RecyclerView.ViewHolder {

        ImageView thumbImg;
        RelativeLayout parentLayout;

        public FavHolder(@NonNull View itemView) {
            super(itemView);

            thumbImg = itemView.findViewById(R.id.posterImg);
            parentLayout = itemView.findViewById(R.id.movie_parent_layout);

        }
    }
}
