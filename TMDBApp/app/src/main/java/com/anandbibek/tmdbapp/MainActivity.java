package com.anandbibek.tmdbapp;

import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.AdapterCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new GridViewFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAdapterItemClick(MovieInfo data, View transitionView) {

        Intent i = new Intent(MainActivity.this, DetailsActivity.class);
        i.putExtra(DetailsActivity.PARCELABLE_MOVIE_INFO, data);

        //shared element transition will only work on Lollipop or above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String transitionName = getString(R.string.poster_transition);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, transitionView, transitionName);
            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }
}
