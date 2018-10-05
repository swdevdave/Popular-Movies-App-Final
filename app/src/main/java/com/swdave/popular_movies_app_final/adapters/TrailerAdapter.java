package com.swdave.popular_movies_app_final.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swdave.popular_movies_app_final.R;

import com.swdave.popular_movies_app_final.model.TrailerResults;

import java.util.ArrayList;



public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = "TrailerRecyclerView";
    private ArrayList<TrailerResults> mTrailerResults;
    private Context context;
    private final static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private final static String BASE_YOUTUBE_IMG_URL = "http://img.youtube.com/vi/";

    public TrailerAdapter(Context context, ArrayList<TrailerResults> trailerResults) {

        this.context = context;
        this.mTrailerResults = trailerResults;

    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);

        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder viewHolder, final int position) {

        String name = mTrailerResults.get(position).getName();
        final String key = mTrailerResults.get(position).getKey();

        viewHolder.tv_trailer_adapter.setText(name);
        Log.d(TAG, "onBindViewHolder: " + name +" " + key);

        Glide.with(context)
                .load(BASE_YOUTUBE_IMG_URL + key + "/0.jpg")
                .into(viewHolder.iv_trailer_image);

        final String urlVideo = BASE_YOUTUBE_URL + key;

        Log.d(TAG, "onBindViewHolder: " + urlVideo);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo));
                context.startActivity(webIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mTrailerResults.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        TextView tv_trailer_adapter;
        ImageView iv_trailer_image;
        RelativeLayout parentLayout;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_trailer_adapter = itemView.findViewById(R.id.tv_trailer_adapter);
            iv_trailer_image = itemView.findViewById(R.id.iv_trailer_image);
            parentLayout = itemView.findViewById(R.id.trailer_parent_layout);
        }
    }
}
