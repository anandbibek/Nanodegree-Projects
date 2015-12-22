package com.anandbibek.tmdbapp;

import android.net.Uri;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static Uri buildMovieDBUri(String sortBy, String page) {
        return Uri.parse(GlobalConstants.FETCH_MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(GlobalConstants.SORT_PARAM, sortBy)
                .appendQueryParameter(GlobalConstants.PAGE_PARAM, page)
                .appendQueryParameter(GlobalConstants.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();
    }

    public static String getShortDateString(String dateString) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(GlobalConstants.TMDB_DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat toFormat = new SimpleDateFormat(GlobalConstants.SHORT_DATE_FORMAT, Locale.getDefault());

        if(dateString == null) return ""; //Year is unknown

        try {
            Date fromDate = fromFormat.parse(dateString);
            return toFormat.format(fromDate);
        } catch (ParseException e) {
            Log.v(Utility.class.getSimpleName(), "Problem parsing date: " + dateString);
            return ""; //Use a blank string instead
        }
    }
}