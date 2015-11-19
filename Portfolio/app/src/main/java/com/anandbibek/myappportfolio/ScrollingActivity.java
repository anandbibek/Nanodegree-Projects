package com.anandbibek.myappportfolio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackBar(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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

    public void onButtonClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.spotifyAppButton : showSnackBar(v);
                break;
            case R.id.scoresAppButton : showSnackBar(v);
                break;
            case R.id.buildItAppButton : showSnackBar(v);
                break;
            case R.id.readerAppButton : showSnackBar(v);
                break;
            case R.id.libraryAppButton : showSnackBar(v);
                break;
            case R.id.capStoneAppButton : showSnackBar(v);
                break;
            //TODO change when projects are done

        }
    }

    private void showSnackBar(View view){
        Snackbar.make(view, "Placeholder text", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
