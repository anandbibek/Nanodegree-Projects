package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.anandbibek.tmdbapp.volley.CustomVolley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class GridViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GridLayoutManager gridLayoutManager;
    View root;
    RequestQueue requestQueue;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    public static String SORT_LOGIC = GlobalConstants.SORT_BY_POPULARITY;
    public static String REQUEST_TAG = "discover";
    public static String MOVIE_BUNDLE = "movie_bundle";

    public GridViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gridview, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getActivity(), getActivity().getResources().getInteger(R.integer.num_columns));
        recyclerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(getActivity());
        recyclerView.setAdapter(moviesAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        requestQueue = CustomVolley.getInstance(getActivity()).getRequestQueue();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadMovies(totalItemCount/20 + 1);
                    loading = true;
                }
            }
        });

        //TODO add auto loading next pages

        if(savedInstanceState==null){
            //load first page if starting up first time
            loadMovies(1);
        } else {
            //load from bundle if recreating
            moviesAdapter.set(savedInstanceState.<MovieInfo>getParcelableArrayList(MOVIE_BUNDLE));
        }
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(moviesAdapter!=null)
            outState.putParcelableArrayList(MOVIE_BUNDLE, moviesAdapter.getMovieList());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        //make sure response handlers are not called if
        if(requestQueue!=null)
            requestQueue.cancelAll(REQUEST_TAG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_gridview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_popular : SORT_LOGIC = GlobalConstants.SORT_BY_POPULARITY;
                break;
            case R.id.action_sort_rating : SORT_LOGIC = GlobalConstants.SORT_BY_RATING;
                break;
        }
        loadMovies(1);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        loadMovies(1);
    }

    private void loadMovies(int page){
        String url = Utility.buildMovieDBUri(SORT_LOGIC, String.valueOf(page)).toString();
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        moviesAdapter.add(JsonParser.parse(response));
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(root, "Something went wrong", Snackbar.LENGTH_SHORT);
                    }
                });
        request.setTag(REQUEST_TAG);
        requestQueue.add(request);
        mSwipeRefreshLayout.setRefreshing(true);
    }
}
