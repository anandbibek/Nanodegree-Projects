package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {

    private static final String MOVIE_INFO_PARAM = "movie_info";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        final ImageView background = (ImageView)root.findViewById(R.id.detail_background_image);
        final ImageView poster = (ImageView)root.findViewById(R.id.detail_poster_image);
        final TextView bigTitle = (TextView)root.findViewById(R.id.big_header_text);
        final TextView smallTitle = (TextView)root.findViewById(R.id.small_header_text);
        final TextView dateText = (TextView)root.findViewById(R.id.date_text);
        final TextView ratingText = (TextView)root.findViewById(R.id.rating_text);
        final TextView popularityText = (TextView)root.findViewById(R.id.popularity_text);

        MovieInfo info = getArguments().getParcelable(MOVIE_INFO_PARAM);
        if(info!=null) {
            Picasso.with(getActivity())
                    .load(GlobalConstants.MOVIE_POSTER_PATH_BIG + info.backdrop_path)
                    .into(background, new Callback() {
                        @Override
                        public void onSuccess() {
                            background.animate().alpha(1);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            Picasso.with(getActivity())
                    .load(GlobalConstants.MOVIE_POSTER_PATH_SMALL + info.poster_path)
                    .into(poster);
            bigTitle.setText(info.title);
            smallTitle.setText(info.overview);
            dateText.setText(info.release_date);
            ratingText.setText(info.rating);
            popularityText.setText(info.popularity);
        }
        return root;
    }


}
