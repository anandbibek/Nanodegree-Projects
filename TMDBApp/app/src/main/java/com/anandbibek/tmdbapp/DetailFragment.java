package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.anandbibek.tmdbapp.volley.CustomVolley;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class DetailFragment extends Fragment {

    private static final String MOVIE_INFO_PARAM = "movie_info";
    NetworkImageView background, poster;
    TextView bigTitle, smallTitle, dateText, ratingText, popularityText;
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
        smallTitle = (TextView)root.findViewById(R.id.plot_text);
        dateText = (TextView)root.findViewById(R.id.date_text);
        ratingText = (TextView)root.findViewById(R.id.rating_text);
        popularityText = (TextView)root.findViewById(R.id.popularity_text);

        MovieInfo info = getArguments().getParcelable(MOVIE_INFO_PARAM);
        if(info!=null) {
            background.setImageUrl(GlobalConstants.MOVIE_POSTER_PATH_BIG + info.backdrop_path, imageLoader);
            poster.setImageUrl(GlobalConstants.MOVIE_POSTER_PATH_SMALL + info.poster_path, imageLoader);
            bigTitle.setText(info.title);
            smallTitle.setText(info.overview);
            dateText.setText(String.format(getString(R.string.released),Utility.getLongDateString(info.release_date)));
            ratingText.setText(String.format(getString(R.string.rating), info.rating, info.vote_count));
            popularityText.setText(String.format(getString(R.string.popularity), String.format("%.0f", info.popularity)));
        }
        staggeredAnimate();
        return root;
    }

    public void staggeredAnimate(){
        View[] animatedViews = new View[] { bigTitle, dateText, ratingText, popularityText, smallTitle};

        // see here for using the right interpolator is important:
        // http://www.google.com/design/spec/animation/authentic-motion.html#authentic-motion-mass-weight
        // and here for how to use them:
        // http://developer.android.com/guide/topics/graphics/prop-animation.html#interpolators
        Interpolator interpolator = new DecelerateInterpolator();

        for (int i = 0; i < animatedViews.length; ++i) {
            View v = animatedViews[i];

            // let's enable hardware acceleration for better performance
            // http://blog.danlew.net/2015/10/20/using-hardware-layers-to-improve-animation-performance/
            v.setLayerType(View.LAYER_TYPE_HARDWARE, null);

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

}
