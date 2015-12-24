package com.anandbibek.tmdbapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anand on 12-Dec-15 8:01 PM .
 */
public class MovieInfo implements Parcelable {

    public String movie_id;
    public String title;
    public String overview;
    public String poster_path;
    public String backdrop_path;
    public String release_date;
    public float rating;
    public float popularity;
    public String vote_count;

    public MovieInfo(){}

    protected MovieInfo(Parcel in) {
        movie_id = in.readString();
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        rating = in.readFloat();
        popularity = in.readFloat();
        vote_count = in.readString();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movie_id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(release_date);
        dest.writeFloat(rating);
        dest.writeFloat(popularity);
        dest.writeString(vote_count);
    }
}
