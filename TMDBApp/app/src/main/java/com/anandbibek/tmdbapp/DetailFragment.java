package com.anandbibek.tmdbapp;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anandbibek.tmdbapp.volley.CustomVolley;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class DetailFragment extends Fragment {

    private static final String MOVIE_INFO_PARAM = "movie_info";
    private static final int MAX_TRANSITION_WAIT = 300;
    NetworkImageView background; ImageView poster;
    TextView bigTitle, plotOverview, dateText, ratingText, votesText, popularityText;
    LinearLayout ratingContainer;
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
        poster = (ImageView)root.findViewById(R.id.poster_image);
        bigTitle = (TextView)root.findViewById(R.id.big_header_text);
        plotOverview = (TextView)root.findViewById(R.id.plot_text);
        dateText = (TextView)root.findViewById(R.id.date_text);
        ratingText = (TextView)root.findViewById(R.id.rating_text);
        votesText = (TextView)root.findViewById(R.id.votes_text);
        popularityText = (TextView)root.findViewById(R.id.popularity_text);
        ratingContainer = (LinearLayout)root.findViewById(R.id.rating_container);

        MovieInfo info = getArguments().getParcelable(MOVIE_INFO_PARAM);
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
        }
        staggeredAnimate();
        return root;
    }

    private void resumeSharedTransition(boolean immediate){
        if(immediate)
            ((AppCompatActivity) getActivity()).supportStartPostponedEnterTransition();
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(getActivity()!=null)
                        ((AppCompatActivity) getActivity()).supportStartPostponedEnterTransition();
                }
            },MAX_TRANSITION_WAIT);
        }
    }

    private void staggeredAnimate(){
        View[] animatedViews = new View[] { bigTitle, dateText, ratingContainer, popularityText, plotOverview};
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

}
