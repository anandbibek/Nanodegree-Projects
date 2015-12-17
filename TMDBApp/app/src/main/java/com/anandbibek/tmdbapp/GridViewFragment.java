package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class GridViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

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
        moviesAdapter = new MoviesAdapter();
        recyclerView.setAdapter(moviesAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //TODO add auto loading next pages
        //new FetchMoviesTask(moviesAdapter).execute(GlobalConstants.SORT_BY_POPULARITY, "2");

        new FetchMoviesTask(moviesAdapter, mSwipeRefreshLayout).execute(GlobalConstants.SORT_BY_POPULARITY, "1");
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //menu.clear();
        //inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public void onRefresh() {
        new FetchMoviesTask(moviesAdapter, mSwipeRefreshLayout).execute(GlobalConstants.SORT_BY_POPULARITY, "1");
    }
}
