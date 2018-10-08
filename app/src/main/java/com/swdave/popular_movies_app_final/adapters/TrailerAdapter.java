package com.swdave.popular_movies_app_final.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swdave.popular_movies_app_final.R;
import com.swdave.popular_movies_app_final.model.TrailerResults;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private ArrayList<TrailerResults> mTrailer;
    private Context mContext;


    public TrailerAdapter(ArrayList<TrailerResults> trailers, Context context) {

        this.mTrailer = trailers;
        this.mContext = context;

    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerHolder holder, final int position) {

        holder.trailerName.setText(mTrailer.get(position).getName());

        Glide.with(mContext)
                .load(mTrailer.get(position).getYoutubeImg())
                .into(holder.trailerImage);

        holder.trailerImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTrailer.get(position).getYoutubeUrl()));
                mContext.startActivity(webIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailer.size();
    }


    public class TrailerHolder extends RecyclerView.ViewHolder {

        TextView trailerName;
        ImageView trailerImage;

        public TrailerHolder(View itemView) {
            super(itemView);

            trailerImage = itemView.findViewById(R.id.trailer_sample_image);
            trailerName = itemView.findViewById(R.id.trailer_tv_name);

        }
    }
}
