package com.fabriziolarosa.testicemobile.Activities;

import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fabriziolarosa.testicemobile.Common.ImageViewFromUrl;
import com.fabriziolarosa.testicemobile.Common.Movie;
import com.fabriziolarosa.testicemobile.R;

/**
 * Copyright 2016 {fbrzlarosa@gmail.com}
 * com.fabriziolarosa.testicemobile.Activities Created by Fabrizio La Rosa on 14/06/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    private ImageViewFromUrl posterImage;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie=getIntent().getExtras().getParcelable("movie");
        if(movie!=null){
            getIntent().getExtras().clear(); //Clearing the Extras of Intent for a clean Relaunch Activity
        }
        else{
            Toast.makeText(this,getResources().getString(R.string.error_movie),Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Setting the Back Button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        scrollView= (ScrollView) findViewById(R.id.detail_movie_scrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                posterImage.setY(scrollView.getScrollY()/2); //Used for a Parallax effect of the Poster Image
            }
        });
        //Find the TextViews for show the Movie Details
        ((TextView)findViewById(R.id.detail_movie_title)).setText(movie.getTitle());
        ((TextView)findViewById(R.id.detail_movie_year)).setText(movie.getYear());
        ((TextView)findViewById(R.id.detail_movie_rated)).setText(movie.getRated());
        ((TextView)findViewById(R.id.detail_movie_released)).setText(movie.getReleased());
        ((TextView)findViewById(R.id.detail_movie_runtime)).setText(movie.getRuntime());
        ((TextView)findViewById(R.id.detail_movie_genre)).setText(movie.getGenre());
        ((TextView)findViewById(R.id.detail_movie_director)).setText(movie.getDirector());
        ((TextView)findViewById(R.id.detail_movie_writer)).setText(movie.getWriter());
        ((TextView)findViewById(R.id.detail_movie_actors)).setText(movie.getActors());
        ((TextView)findViewById(R.id.detail_movie_plot)).setText(movie.getPlot());
        ((TextView)findViewById(R.id.detail_movie_language)).setText(movie.getLanguage());
        ((TextView)findViewById(R.id.detail_movie_country)).setText(movie.getCountry());
        ((TextView)findViewById(R.id.detail_movie_awards)).setText(movie.getAwards());
        ((TextView)findViewById(R.id.detail_movie_metascore)).setText(movie.getMetascore());
        ((TextView)findViewById(R.id.detail_movie_imd_brating)).setText(movie.getImdbRating()+"");
        ((TextView)findViewById(R.id.detail_movie_imdb_votes)).setText(movie.getImdbVotes()+"");
        ((TextView)findViewById(R.id.detail_movie_type)).setText(movie.getType());
        posterImage= (ImageViewFromUrl) findViewById(R.id.detail_movie_poster);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ //Necessary for compatibility of API 16
            posterImage.setTransitionName("movie_image");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,posterImage, "movieImage");
        }
        posterImage.setUrl(movie.getPoster());
        posterImage.loadImage();
    }
}
