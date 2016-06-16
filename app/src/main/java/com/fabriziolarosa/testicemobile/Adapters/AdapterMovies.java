package com.fabriziolarosa.testicemobile.Adapters;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fabriziolarosa.testicemobile.Asyncs.LoadFullMovieData;
import com.fabriziolarosa.testicemobile.Common.ImageViewFromUrl;
import com.fabriziolarosa.testicemobile.Common.Movie;
import com.fabriziolarosa.testicemobile.R;

import java.util.ArrayList;

/**
 * Copyright 2016 {fbrzlarosa@gmail.com}
 * com.fabriziolarosa.testicemobile.Adapters Created by Fabrizio La Rosa on 14/06/16.
 */
public class AdapterMovies extends RecyclerView.Adapter<AdapterMovies.MovieViewHolder> {
    private ArrayList<Movie> listMovies;
    private OnItemClickListener clickListener;
    public static class MovieViewHolder extends RecyclerView.ViewHolder { //This is an holder for cell Recycling
        public TextView titleText;
        public TextView yearText;
        public TextView directorText;
        public ImageViewFromUrl posterImage;
        public ProgressBar progress_loading;
        public MovieViewHolder(View v) {
            super(v);
            this.titleText= (TextView) v.findViewById(R.id.movie_card_title);
            this.directorText= (TextView) v.findViewById(R.id.movie_card_director);
            this.yearText= (TextView) v.findViewById(R.id.movie_card_year);
            this.posterImage= (ImageViewFromUrl) v.findViewById(R.id.movie_card_poster);
            this.progress_loading= (ProgressBar) v.findViewById(R.id.movie_card_progressbar);
        }
        public void bind(final Movie item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item,v);
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener clickListener){ //Setting the click item Listener, isn't in RecyclerView
        this.clickListener=clickListener;
    }
    private OnUpdateList onUpdate;
    public void setOnUpdateItem(OnUpdateList onUpdate){
        this.onUpdate=onUpdate;
    }

    public AdapterMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        for(Movie movie : listMovies){ //foreach Movies
            new LoadFullMovieData(movie, new LoadFullMovieData.OnLoadedMovie() { //Loading Single Movie, see LoadFullMovieData class for more information
                @Override
                public void onFinish(Movie movie) {
                    AdapterMovies.this.notifyDataSetChanged();
                }
                @Override
                public void onError(Exception error) {
                    error.printStackTrace();
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    @Override
    public AdapterMovies.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(inflateView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {

        //Resetting the view Holder to avoid a clone cell
        holder.posterImage.setImageBitmap(null);
        holder.titleText.setText("");
        holder.yearText.setText("");
        holder.directorText.setText("");
        holder.progress_loading.setVisibility(View.VISIBLE);

        if(listMovies.get(position).getTitle()!=null) {
            holder.titleText.setText(listMovies.get(position).getTitle());
            holder.yearText.setText(listMovies.get(position).getYear());
            holder.directorText.setText(listMovies.get(position).getDirector());
            holder.posterImage.setUrl(listMovies.get(position).getPoster());
            holder.posterImage.loadImage();
            holder.progress_loading.setVisibility(View.GONE);
            if(clickListener!=null) {
                holder.bind(listMovies.get(position), clickListener); //Setting the cell click listener
            }
        }
        else{
            new LoadFullMovieData(listMovies.get(position), new LoadFullMovieData.OnLoadedMovie() { //Loading Single Movie, see LoadFullMovieData class for more information
                @Override
                public void onFinish(Movie movie) {
                    onUpdate.update(position); //Update Interface for a callback when an item is loaded
                }
                @Override
                public void onError(Exception error) {
                    error.printStackTrace();
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public interface OnUpdateList{
        void update(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(Movie item,View view);
    }

}

