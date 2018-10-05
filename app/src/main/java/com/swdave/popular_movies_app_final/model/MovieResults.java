package com.swdave.popular_movies_app_final.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieResults implements Parcelable {

    private String id;

    @SerializedName("vote_average")
    private String voteAverage;

    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;


    protected MovieResults(Parcel in) {
        id = in.readString();
        voteAverage = in.readString();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<MovieResults> CREATOR = new Creator<MovieResults>() {
        @Override
        public MovieResults createFromParcel(Parcel in) {
            return new MovieResults(in);
        }

        @Override
        public MovieResults[] newArray(int size) {
            return new MovieResults[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(voteAverage);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
