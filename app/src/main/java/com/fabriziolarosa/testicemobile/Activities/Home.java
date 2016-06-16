package com.fabriziolarosa.testicemobile.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fabriziolarosa.testicemobile.Adapters.AdapterMovies;
import com.fabriziolarosa.testicemobile.Common.Movie;
import com.fabriziolarosa.testicemobile.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright 2016 {fbrzlarosa@gmail.com}
 * com.fabriziolarosa.testicemobile.Activities Created by Fabrizio La Rosa on 14/06/16.
 */
public class Home extends AppCompatActivity {

    private LinearLayoutManager layoutManagerRecyclerView;
    private static int numPage=1;
    private static ArrayList<Movie> listMovies=null; //We need static declaration, for switch land/port
    private ProgressBar home_progressbar;
    private RecyclerView list_movies_recycler;
    public TextView pleaseSearch;
    private static String queryString="";
    private boolean loadingMore = true;
    private ProgressBar progress_loading_more;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home_progressbar= (ProgressBar) findViewById(R.id.home_progressbar);
        list_movies_recycler= (RecyclerView) findViewById(R.id.list_movies);
        list_movies_recycler.setHasFixedSize(true);
        layoutManagerRecyclerView = new LinearLayoutManager(this);
        list_movies_recycler.setLayoutManager(layoutManagerRecyclerView);
        list_movies_recycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            int pastVisiblesItems;
            int visibleItemCount;
            int totalItemCount;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0)
                {
                    visibleItemCount = layoutManagerRecyclerView.getChildCount();
                    totalItemCount = layoutManagerRecyclerView.getItemCount();
                    pastVisiblesItems = layoutManagerRecyclerView.findFirstVisibleItemPosition();
                    if (loadingMore)
                    {
                        if((visibleItemCount+pastVisiblesItems)>=totalItemCount)
                        {
                            progress_loading_more.setVisibility(View.VISIBLE);
                            loadingMore = false;
                            numPage++;
                            final int preLengthList=listMovies.size();
                            new LoadListMovies(numPage, queryString, listMovies, new LoadListMovies.OnFinishLoading() { //When User execute a Search, we call the Loading Movie Async (after a clean of the list)
                                @Override
                                public void finish() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            home_progressbar.setVisibility(View.GONE);
                                            searchMenuItem.collapseActionView(); //Closing the SearchView to see the progress of LoadingMore
                                            progress_loading_more.setVisibility(View.GONE);
                                            if(listMovies.size()==0) {
                                                pleaseSearch.setText(getResources().getString(R.string.no_results));
                                                pleaseSearch.setVisibility(View.VISIBLE);
                                            }
                                            else {
                                                list_movies_recycler.setVisibility(View.VISIBLE);
                                                list_movies_recycler.getAdapter().notifyDataSetChanged();
                                            }
                                            if(preLengthList<listMovies.size()) {
                                                loadingMore = true; //No more Movies
                                            }
                                        }
                                    });
                                }
                            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                }
            }
        });


        pleaseSearch= (TextView) findViewById(R.id.please_search);
        if(listMovies==null){
            listMovies=new ArrayList<Movie>();
        }
        else{
            if(listMovies.size()>0){
                pleaseSearch.setVisibility(View.GONE);
            }
        }
        final AdapterMovies adpMovies=new AdapterMovies(listMovies);
        adpMovies.setOnItemClickListener(new AdapterMovies.OnItemClickListener() {
            @Override
            public void onItemClick(Movie item, View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Necessary for compatibility of API 16
                    Intent intent = new Intent(Home.this, MovieDetailActivity.class);
                    view.findViewById(R.id.movie_card_poster).setTransitionName("movie_image");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Home.this, view.findViewById(R.id.movie_card_poster), "movie_image");
                    //In this case, we call the same TransitionName of MovieDetailActivity Image for Material Animation
                    intent.putExtra("movie",item);
                    startActivity(intent, options.toBundle());
                } else {
                    Intent intent = new Intent(Home.this, MovieDetailActivity.class);
                    intent.putExtra("movie",item);
                    startActivity(intent);
                }
            }
        });
        adpMovies.setOnUpdateItem(new AdapterMovies.OnUpdateList() { //Custom Interface, necessary for a clean List loading
            @Override
            public void update(final int position) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list_movies_recycler.getAdapter().notifyItemChanged(position);
                    }
                });
            }
        });
        list_movies_recycler.setAdapter(adpMovies);
        Toolbar toolBarSearch = (Toolbar) findViewById(R.id.home_toolbar); //Replace the Base ActionBar
        setSupportActionBar(toolBarSearch);
        progress_loading_more= (ProgressBar) findViewById(R.id.home_toolbar_progress_bar);
        progress_loading_more.setVisibility(View.GONE);
    }

    private static class LoadListMovies extends AsyncTask<Void,Void,Void>{

        private ArrayList<Movie> arrayListMovies=null;
        private String searchString=null;
        private int page=1;
        private OnFinishLoading listenerFinish;

        public LoadListMovies(int page, String searchString, ArrayList<Movie> arrayListMoviesParam,OnFinishLoading listenerFinish){
            this.arrayListMovies=arrayListMoviesParam;
            this.searchString=searchString;
            this.page=page;
            this.listenerFinish=listenerFinish;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection connection = null;
            try{
                connection= (HttpURLConnection) new URL("http://www.omdbapi.com/?r=json&s="+ Uri.encode(searchString)+"&page="+page).openConnection(); //Set the connection Url
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.connect();
                switch (connection.getResponseCode()){
                    case 200:
                        BufferedReader buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Using a Buffer to Read the content of the response HttpUrlConnection
                        StringBuilder strBuild = new StringBuilder();
                        String line;
                        while ((line = buffReader.readLine()) != null) {
                            strBuild.append(line+"\n");
                        }
                        buffReader.close();
                        JSONObject respJson=new JSONObject(strBuild.toString());
                        JSONArray jsonMovies=respJson.getJSONArray("Search");
                        for(int i=0;i<jsonMovies.length();i++){
                            arrayListMovies.add(new Movie(jsonMovies.getJSONObject(i).getString("imdbID"))); //We need only ID, after we call every singular Movie
                        }
                        break;
                    default:
                        break;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listenerFinish.finish(); //Call the Interface for a Callback
            super.onPostExecute(aVoid);
        }
        public interface OnFinishLoading{ //An Interface to get a callback when the connection finish
            void finish();
        }
    }

    private SearchView searchView;
    private MenuItem searchMenuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu); //Call the inflater for the SearchBar
        searchMenuItem = menu.findItem(R.id.home_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findViewById(R.id.please_search).setVisibility(View.GONE);
                searchView.clearFocus();
                numPage=1;
                listMovies.clear();
                list_movies_recycler.getAdapter().notifyDataSetChanged();
                home_progressbar.setVisibility(View.VISIBLE);
                list_movies_recycler.setVisibility(View.GONE);
                queryString=query;
                new LoadListMovies(numPage, queryString, listMovies, new LoadListMovies.OnFinishLoading() { //When User execute a Search, we call the Loading Movie Async (after a clean of the list)
                    @Override
                    public void finish() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                home_progressbar.setVisibility(View.GONE);
                                loadingMore=true;
                                if(listMovies.size()==0) {
                                    pleaseSearch.setText(getResources().getString(R.string.no_results));
                                    pleaseSearch.setVisibility(View.VISIBLE);

                                    loadingMore=false;
                                }
                                else {
                                    list_movies_recycler.setVisibility(View.VISIBLE);
                                    list_movies_recycler.getAdapter().notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); //Execute in the THREAD_POOL_EXECUTOR for a better Threads performance
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}
