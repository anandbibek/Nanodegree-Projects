package com.anandbibek.tmdbapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


//        ArrayList<MovieInfo> movieList = new ArrayList<>();
//
//        MovieInfo m = new MovieInfo();
//        m.title="October 2015\n6.5";
//        m.poster_path="/D6e8RJf2qUstnfkTslTXNTUAlT.jpg";
//        movieList.add(m);
//
//        MovieInfo m1 = new MovieInfo();
//        m1.title="December 2015\n8.5";
//        m1.poster_path="/fYzpM9GmpBlIC893fNjoWCwE24H.jpg";
//        movieList.add(m1);
//
//        MovieInfo m2 = new MovieInfo();
//        m2.title="November 2015\n5.5";
//        m2.poster_path="/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg";
//        movieList.add(m2);

        final MoviesAdapter moviesAdapter = new MoviesAdapter();
        recyclerView.setAdapter(moviesAdapter);

        //TODO add auto loading next pages
        new FetchMoviesTask(moviesAdapter).execute(GlobalConstants.SORT_BY_POPULARITY, "1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
