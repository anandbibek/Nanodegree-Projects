package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class GridViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    static String SORT_LOGIC = GlobalConstants.SORT_BY_POPULARITY;

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
        View root = inflater.inflate(R.layout.fragment_gridview, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        moviesAdapter = new MoviesAdapter(getActivity());
        recyclerView.setAdapter(moviesAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //TODO add auto loading next pages
        //new FetchMoviesTask(moviesAdapter).execute(GlobalConstants.SORT_BY_POPULARITY, "2");

        loadMovies("1");
        return root;
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
        loadMovies("1");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        loadMovies("1");
    }

    private void loadMovies(String page){
        new FetchMoviesTask(moviesAdapter, mSwipeRefreshLayout).execute(SORT_LOGIC, page);
    }
}
