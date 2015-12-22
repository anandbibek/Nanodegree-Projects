package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anandbibek.tmdbapp.volley.CustomVolley;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class DetailFragment extends Fragment {

    private static final String MOVIE_INFO_PARAM = "movie_info";
    NetworkImageView background, poster;
    TextView bigTitle, smallTitle, TextView, dateText, ratingText, popularityText;
    ImageLoader imageLoader;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        imageLoader = CustomVolley.getInstance(getActivity()).getImageLoader();
        background = (NetworkImageView)root.findViewById(R.id.detail_background_image);
        poster = (NetworkImageView)root.findViewById(R.id.detail_poster_image);
        bigTitle = (TextView)root.findViewById(R.id.big_header_text);
        smallTitle = (TextView)root.findViewById(R.id.small_header_text);
        dateText = (TextView)root.findViewById(R.id.date_text);
        ratingText = (TextView)root.findViewById(R.id.rating_text);
        popularityText = (TextView)root.findViewById(R.id.popularity_text);

        MovieInfo info = getArguments().getParcelable(MOVIE_INFO_PARAM);
        if(info!=null) {
            background.setImageUrl(GlobalConstants.MOVIE_POSTER_PATH_BIG + info.backdrop_path, imageLoader);
            poster.setImageUrl(GlobalConstants.MOVIE_POSTER_PATH_SMALL + info.poster_path, imageLoader);
            bigTitle.setText(info.title);
            smallTitle.setText(info.overview);
            dateText.setText(info.release_date);
            ratingText.setText(info.rating);
            popularityText.setText(info.popularity);
        }
        return root;
    }


}
