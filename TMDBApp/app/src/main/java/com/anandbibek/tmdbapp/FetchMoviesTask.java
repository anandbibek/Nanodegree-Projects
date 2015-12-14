package com.anandbibek.tmdbapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private MoviesAdapter mAdapter;

    public FetchMoviesTask(MoviesAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieInfo> movies) {
        if(movies != null) {
            mAdapter.set(movies);
        }
    }

    protected ArrayList<MovieInfo> doInBackground(String... params) {
        if(params.length != 2) {
            Log.d(LOG_TAG, "FetchMoviesTask was called without the sort order or page specified");
        }
        final String sortBy = params[0];
        final String page = params[1];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Connect to The Movie DB
            URL url = new URL(Utility.buildMovieDBUri(sortBy, page).toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Retrieve the results
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            //Empty input
            if(inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line).append("\n");
            }

            if(buffer.length() == 0) {
                Log.d(LOG_TAG, "The response from TMDB was empty");
                return null;
            }

            String retrievedJson = buffer.toString();
            return JsonParser.parse(retrievedJson);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
            return null;
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }
}