package com.anandbibek.tmdbapp;

import com.anandbibek.tmdbapp.data.ProviderContract;

/**
 * Created by Anand on 12-Dec-15 7:32 PM .
 */
public class GlobalConstants {

    public static final String MOVIE_POSTER_PATH_SMALL = "https://image.tmdb.org/t/p/w342/";
    public static final String MOVIE_POSTER_PATH_BIG = "https://image.tmdb.org/t/p/w780/";
    public static final String MIN_VOTES = "100";
    public static final String FETCH_MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    public static final String MOVIE_TRAILER_URL ="http://api.themoviedb.org/3/movie/";
    public static final String MOVIE_TRAILER_PATH ="videos";
    public static final String MOVIE_REVIEWS_PATH ="reviews";


    public static final String SORT_PARAM = "sort_by";

    public static final String API_KEY_PARAM = "api_key";

    public static final String PAGE_PARAM = "page";

    public static final String SORT_BY_POPULARITY = "popularity.desc";

    public static final String SORT_BY_RATING = "vote_average.desc";

    public static final String MIN_VOTES_PARAM ="vote_count.gte";

    public static final String TMDB_DATE_FORMAT= "yyyy-MM-dd";

    public static final String SHORT_DATE_FORMAT= "MMM yyyy";

    public static final String LONG_DATE_FORMAT= "d MMMM yyyy";


    public static final String[] MOVIE_COLUMNS = {

            ProviderContract.MovieInfoTable._ID,
            ProviderContract.MovieInfoTable.COLUMN_MOVIE_ID,
            ProviderContract.MovieInfoTable.COLUMN_TITLE,
            ProviderContract.MovieInfoTable.COLUMN_OVERVIEW,
            ProviderContract.MovieInfoTable.COLUMN_POSTER,
            ProviderContract.MovieInfoTable.COLUMN_BACKDROP,
            ProviderContract.MovieInfoTable.COLUMN_RELEASE,
            ProviderContract.MovieInfoTable.COLUMN_RATING,
            ProviderContract.MovieInfoTable.COLUMN_POPULARITY,
            ProviderContract.MovieInfoTable.COLUMN_VOTES
    };

    static final int COL_DB_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_OVERVIEW = 3;
    static final int COL_POSTER = 4;
    static final int COL_BACKDROP = 5;
    static final int COL_RELEASE = 6;
    static final int COL_RATING = 7;
    static final int COL_POPULARITY = 8;
    static final int COL_VOTES = 9;
}
