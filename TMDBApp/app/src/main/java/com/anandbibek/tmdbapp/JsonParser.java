package com.anandbibek.tmdbapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Anand on 13-Dec-15 11:38 AM .
 */
public class JsonParser {

    private static final String TAG_RESULTS = "results";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ID = "id";
    private static final String TAG_RELEASE_DATE = "release_date";
    private static final String TAG_RATING = "vote_average";
    private static final String TAG_POPULARITY = "popularity";
    private static final String TAG_POSTER_PATH = "poster_path";
    private static final String TAG_BACKDROP_PATH = "backdrop_path";
    private static final String TAG_PLOT = "overview";
    private static final String TAG_VOTES = "vote_count";

    public static ArrayList<MovieInfo> parseMovieList(String jsonData) {

        ArrayList<MovieInfo> movieList = new ArrayList<>();

        if(jsonData==null)
            return movieList;

        try {
            JSONArray movieArray = new JSONObject(jsonData).getJSONArray(TAG_RESULTS);
            int length = movieArray.length();

            for(int i=0; i<length; i++){

                MovieInfo movieInfo = new MovieInfo();
                JSONObject movieObject = movieArray.getJSONObject(i);

                movieInfo.title = movieObject.getString(TAG_TITLE);
                movieInfo.movie_id = movieObject.getString(TAG_ID);
                movieInfo.overview = movieObject.getString(TAG_PLOT);
                movieInfo.poster_path = movieObject.getString(TAG_POSTER_PATH);
                movieInfo.backdrop_path = movieObject.getString(TAG_BACKDROP_PATH);
                movieInfo.release_date = movieObject.getString(TAG_RELEASE_DATE);
                movieInfo.popularity = Float.parseFloat(movieObject.getString(TAG_POPULARITY));
                movieInfo.rating = Float.parseFloat(movieObject.getString(TAG_RATING));
                movieInfo.vote_count = movieObject.getString(TAG_VOTES);

                movieList.add(movieInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    //TODO improve implementation
    public static String parseTrailer(String jsonData){
        String trailerList = "";

        if(jsonData!=null) {
            try {
                JSONArray movieArray = new JSONObject(jsonData).getJSONArray(TAG_RESULTS);
                int length = movieArray.length();

                for (int i = 0; i < length; i++) {
                    JSONObject trailerObject = movieArray.getJSONObject(i);
                    trailerList = trailerObject.getString("key");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trailerList;
    }

    public static String parseReviews(String jsonData){
        String reviewList = "";

        if(jsonData!=null) {
            try {
                JSONArray movieArray = new JSONObject(jsonData).getJSONArray(TAG_RESULTS);
                int length = movieArray.length();

                for (int i = 0; i < length; i++) {
                    JSONObject trailerObject = movieArray.getJSONObject(i);
                    reviewList = reviewList.concat(trailerObject.getString("author")+"\n");
                    reviewList = reviewList.concat(trailerObject.getString("content")+"\n\n");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviewList;
    }
}
