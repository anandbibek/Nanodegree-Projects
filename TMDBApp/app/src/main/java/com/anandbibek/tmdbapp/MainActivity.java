package com.anandbibek.tmdbapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.AdapterCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementsUseOverlay(false);
        }

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new GridViewFragment()).commit();
        }

    }

    @Override
    public void onAdapterItemClick(MovieInfo data, View poster, View card) {

        if (getResources().getBoolean(R.bool.has_two_panes)) {
            DetailFragment fragment = DetailFragment.newInstance(data);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_details, fragment).commit();
            return;
        }

        Intent i = new Intent(MainActivity.this, DetailsActivity.class);
        i.putExtra(DetailsActivity.PARCELABLE_MOVIE_INFO, data);
        startActivity(i);

        //shared element transition will only work on Lollipop or above
        //TODO Doesn't go well with delayed image loading of Volley
        //NPE if fast scrolled and clicked before views are laid out by adapter
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Pair<View, String> p1 = Pair.create(poster, getString(R.string.poster_transition));
//            Pair<View, String> p2 = Pair.create(card, getString(R.string.card_transition));
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, p1, p2);
//            startActivity(i, options.toBundle());
//        } else {
//            startActivity(i);
//        }
    }
}
