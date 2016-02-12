/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anandbibek.tmdbapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anandbibek.tmdbapp.data.ProviderContract.MovieInfoTable;

/**
 * Manages a local database for weather data.
 */
public class ProviderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public ProviderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TABLE = "CREATE TABLE " + MovieInfoTable.TABLE_NAME + " (" +

                MovieInfoTable._ID + " INTEGER PRIMARY KEY," +

                MovieInfoTable.COLUMN_MOVIE_ID + " TEXT, " +
                MovieInfoTable.COLUMN_TITLE + " TEXT, " +
                MovieInfoTable.COLUMN_POSTER + " TEXT, " +
                MovieInfoTable.COLUMN_OVERVIEW + " TEXT," +

                MovieInfoTable.COLUMN_BACKDROP + " TEXT, " +
                MovieInfoTable.COLUMN_RELEASE + " TEXT, " +

                MovieInfoTable.COLUMN_RATING + " REAL, " +
                MovieInfoTable.COLUMN_POPULARITY + " REAL, " +
                MovieInfoTable.COLUMN_VOTES + " TEXT, " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MovieInfoTable.COLUMN_TITLE + ", " +
                MovieInfoTable.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieInfoTable.TABLE_NAME);
        //nCreate(sqLiteDatabase);
    }
}
