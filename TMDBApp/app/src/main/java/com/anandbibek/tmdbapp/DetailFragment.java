package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anandbibek.tmdbapp.data.ProviderContract;
import com.anandbibek.tmdbapp.data.ProviderContract.MovieInfoTable;
import com.anandbibek.tmdbapp.volley.CustomVolley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MOVIE_INFO_PARAM = "movie_info";
    private static final int MAX_TRANSITION_WAIT = 300;
    NetworkImageView background; ImageView poster, playIcon;
    TextView bigTitle, plotOverview, dateText, ratingText, votesText, popularityText, reviewText;
    View plotCard, root;
    RequestQueue requestQueue;
    LinearLayout ratingContainer;
    ImageLoader imageLoader;
    FloatingActionButton fab;
    MovieInfo info;
    String trailerKey = "";

    private boolean IS_FAV = false;
    private static final int FAV_CHECKER = 1;

    public static DetailFragment newInstance(MovieInfo data) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_INFO_PARAM, data);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_detail, container, false);
        info = getArguments().getParcelable(MOVIE_INFO_PARAM);
        imageLoader = CustomVolley.getInstance(getActivity()).getImageLoader();
        background = (NetworkImageView)root.findViewById(R.id.detail_background_image);
        poster = (ImageView)root.findViewById(R.id.poster_image);
        playIcon = (ImageView)root.findViewById(R.id.play_icon_view);
        bigTitle = (TextView)root.findViewById(R.id.big_header_text);
        plotOverview = (TextView)root.findViewById(R.id.plot_text);
        dateText = (TextView)root.findViewById(R.id.date_text);
        ratingText = (TextView)root.findViewById(R.id.rating_text);
        votesText = (TextView)root.findViewById(R.id.votes_text);
        reviewText = (TextView)root.findViewById(R.id.review_text);
        popularityText = (TextView)root.findViewById(R.id.popularity_text);
        ratingContainer = (LinearLayout)root.findViewById(R.id.rating_container);
        plotCard = root.findViewById(R.id.plot_card);
        requestQueue = CustomVolley.getInstance(getActivity()).getRequestQueue();
        fab = (FloatingActionButton)root.findViewById(R.id.favorite_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(IS_FAV) {
                    removeFavorite();
                }
                else {
                    saveFavorite();
                }
                fab.animate().rotationXBy(360);
            }
        });
        if(info!=null) {

            //If loading is excessively delayed, UI will be stuck.
            // Don't trust the network, activate fail-safe.
            resumeSharedTransition(false);

            imageLoader.get(GlobalConstants.MOVIE_POSTER_PATH_SMALL + info.poster_path,
                    new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            //resume transition immediately if valid bitmap is found
                            if(response.getBitmap()!=null){
                                poster.setImageBitmap(response.getBitmap());
                                resumeSharedTransition(true);
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //this is called when volley knows there is no net connection at all
                            resumeSharedTransition(true);
                        }
                    });


            //poster.setImageUrl(GlobalConstants.MOVIE_POSTER_PATH_SMALL + info.poster_path, imageLoader);
            background.setImageUrl(GlobalConstants.MOVIE_POSTER_PATH_BIG + info.backdrop_path, imageLoader);
            bigTitle.setText(info.title);
            plotOverview.setText(info.overview);
            dateText.setText(String.format(getString(R.string.released),Utility.getLongDateString(info.release_date)));
            ratingText.setText(String.valueOf(info.rating));
            votesText.setText(info.vote_count);
            popularityText.setText(String.format(getString(R.string.popularity), String.format("%.0f", info.popularity)));

            loadTrailers(info.movie_id);
            loadReviews(info.movie_id);
            getLoaderManager().restartLoader(FAV_CHECKER, null, this);
        }
        staggeredAnimate();
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //FIXME share menu persists on gridView fragment upon swapping two-pane layout
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detailview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share : {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        Utility.buildYoutubeUri(trailerKey).toString());
                startActivity(sharingIntent);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resumeSharedTransition(boolean immediate){
        if(immediate)
            if(getActivity()!=null) {
                ((AppCompatActivity) getActivity()).supportStartPostponedEnterTransition();
                //staggeredAnimate();
            }
            else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() != null) {
                            ((AppCompatActivity) getActivity()).supportStartPostponedEnterTransition();
                        }
                    }
                }, MAX_TRANSITION_WAIT);
            }
    }

    private void staggeredAnimate(){
        View[] animatedViews = new View[] { bigTitle, dateText, ratingContainer, popularityText, plotCard};
        Interpolator interpolator = new DecelerateInterpolator();

        background.setAlpha(0f);
        background.setTranslationY(-75);
        background.animate()
                .setInterpolator(interpolator)
                .alpha(1.0f)
                .translationY(0)
                .setStartDelay(300)
                .start();

        for (int i = 0; i < animatedViews.length; ++i) {
            View v = animatedViews[i];

            // initial state: hide the view and move it down slightly
            v.setAlpha(0f);
            v.setTranslationY(75);

            v.animate()
                    .setInterpolator(interpolator)
                    .alpha(1.0f)
                    .translationY(0)
                            // this little calculation here produces the staggered effect we
                            // saw, so each animation starts a bit after the previous one
                    .setStartDelay(300 + 75 * i)
                    .start();
        }
    }

    private void loadTrailers(String id){
        String url = Utility.buildTrailerUri(id).toString();
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        trailerKey = JsonParser.parseTrailer(response);
                        playIcon.setImageResource(R.drawable.ic_play_circle_filled_white_36dp);
                        playIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Utility.buildYoutubeUri(trailerKey)));
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(root, "Could not load trailers", Snackbar.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(request);
    }

    private void loadReviews(String id){
        String url = Utility.buildReviewsUri(id).toString();
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final String data = JsonParser.parseReviews(response);
                        reviewText.setText(data);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(root, "Could not load reviews", Snackbar.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(request);
    }

    private void saveFavorite(){

        ContentValues values = new ContentValues();
        values.put(MovieInfoTable.COLUMN_MOVIE_ID, info.movie_id);
        values.put(MovieInfoTable.COLUMN_TITLE, info.title);
        values.put(MovieInfoTable.COLUMN_OVERVIEW, info.overview);
        values.put(MovieInfoTable.COLUMN_BACKDROP, info.backdrop_path);
        values.put(MovieInfoTable.COLUMN_POSTER, info.poster_path);
        values.put(MovieInfoTable.COLUMN_POPULARITY, info.popularity);
        values.put(MovieInfoTable.COLUMN_RELEASE, info.release_date);
        values.put(MovieInfoTable.COLUMN_RATING, info.rating);
        values.put(MovieInfoTable.COLUMN_VOTES, info.vote_count);

        Uri returnedUri = getActivity().getContentResolver().insert(MovieInfoTable.CONTENT_URI, values);

        if(returnedUri!=null) {
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            IS_FAV = true;
        }
    }

    private void removeFavorite(){
        int rowsDeleted = getActivity().getContentResolver().delete(
                MovieInfoTable.CONTENT_URI,
                MovieInfoTable.COLUMN_MOVIE_ID + "=?",
                new String[]{info.movie_id});
        if(rowsDeleted>0) {
            IS_FAV = false;
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = MovieInfoTable.COLUMN_MOVIE_ID + "=?";
        return new CursorLoader(getActivity(),
                ProviderContract.MovieInfoTable.CONTENT_URI,
                new String[]{MovieInfoTable.COLUMN_MOVIE_ID},
                selection,
                new String[]{info.movie_id},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToNext()){
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            IS_FAV = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
