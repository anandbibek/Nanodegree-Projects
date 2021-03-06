package com.anandbibek.tmdbapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anandbibek.tmdbapp.volley.CustomVolley;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Anand on 12-Dec-15 7:58 PM .
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CustomViewHolder> {

    private ArrayList<MovieInfo> movieList = new ArrayList<>();
    private Context mContext;
    private ImageLoader mImageLoader;

    public interface AdapterCallback{
        void onAdapterItemClick(MovieInfo data, View poster, View card);
    }

    public MoviesAdapter(Context context){
        mContext = context;
        mImageLoader = CustomVolley.getInstance(mContext).getImageLoader();
    }

    public void set(ArrayList<MovieInfo> data){
        movieList = data;
        notifyDataSetChanged();
    }

    public void add(ArrayList<MovieInfo> data){
        for(MovieInfo info : data)
            movieList.add(info);
        notifyDataSetChanged();
    }

    public void add(MovieInfo data){
        movieList.add(data);
        notifyDataSetChanged();
    }

    public void removeAll(){
        movieList.clear();
        notifyDataSetChanged();
    }

    public ArrayList<MovieInfo> getMovieList(){
        return movieList;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_list_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        MovieInfo movieInfo = movieList.get(position);
        holder.titleView.setText(movieInfo.title);
        holder.ratingView.setText(String.format("%.1f", movieInfo.rating));
        holder.popularityView.setText(String.format("%.0f", movieInfo.popularity));
        holder.posterView.setImageUrl(GlobalConstants.MOVIE_POSTER_PATH_SMALL + movieInfo.poster_path, mImageLoader);
//        Picasso.with(mContext).load(GlobalConstants.MOVIE_POSTER_PATH_SMALL + movieInfo.poster_path)
//                .into(holder.posterView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView titleView, ratingView, popularityView;
        protected NetworkImageView posterView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView)itemView.findViewById(R.id.grid_item_title);
            ratingView = (TextView)itemView.findViewById(R.id.grid_item_rating);
            popularityView = (TextView)itemView.findViewById(R.id.grid_item_popularity);
            posterView = (NetworkImageView)itemView.findViewById(R.id.grid_item_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieInfo data = movieList.get(getLayoutPosition());
            ((AdapterCallback)mContext).onAdapterItemClick(data, posterView, v);
        }
    }
}
