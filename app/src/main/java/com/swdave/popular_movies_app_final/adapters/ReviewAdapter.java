package com.swdave.popular_movies_app_final.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
