package com.anandbibek.tmdbapp;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailsActivity extends AppCompatActivity {

    public static String PARCELABLE_MOVIE_INFO = "parcelable_movie_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementsUseOverlay(false);
        }

        if(savedInstanceState == null) {

            /**
             * For some reason, Volley loads an already cached image with 150ms delay upon adding fragment first time
             * Pausing shared element transition here so imageView doesn't flicker due to load delay
             * Resume the transition with fail-safe from DetailFragment after image load error/success
             * */
            supportPostponeEnterTransition();

            MovieInfo data = getIntent().getParcelableExtra(PARCELABLE_MOVIE_INFO);
            DetailFragment fragment = DetailFragment.newInstance(data);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().add(R.id.frame_layout_details, fragment).commit();
        }
    }

}
