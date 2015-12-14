package com.anandbibek.tmdbapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Anand on 12-Dec-15 7:58 PM .
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CustomViewHolder> {

    private ArrayList<MovieInfo> movieList = new ArrayList<>();

    public MoviesAdapter(){}

    public void set(ArrayList<MovieInfo> data){
        movieList = data;
        notifyDataSetChanged();
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_list_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        MovieInfo movieInfo = movieList.get(position);
        holder.titleView.setText(movieInfo.title);
        holder.dateView.setText(Utility.getShortDateString(movieInfo.release_date));
        holder.ratingView.setText(movieInfo.rating);
        holder.popularityView.setText(movieInfo.popularity);
        Picasso.with(holder.posterView.getContext())
                .load(GlobalConstants.MOVIE_POSTER_PATH+movieInfo.poster_path)
                .into(holder.posterView);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView titleView, dateView, ratingView, popularityView;
        protected ImageView posterView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView)itemView.findViewById(R.id.grid_item_title);
            dateView = (TextView)itemView.findViewById(R.id.grid_item_date);
            ratingView = (TextView)itemView.findViewById(R.id.grid_item_rating);
            popularityView = (TextView)itemView.findViewById(R.id.grid_item_popularity);
            posterView = (ImageView)itemView.findViewById(R.id.grid_item_poster);
        }
    }
}
