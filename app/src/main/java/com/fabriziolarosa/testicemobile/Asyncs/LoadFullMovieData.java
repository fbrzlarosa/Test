package com.fabriziolarosa.testicemobile.Asyncs;

import android.os.AsyncTask;

import com.fabriziolarosa.testicemobile.Common.Movie;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright 2016 {fbrzlarosa@gmail.com}
 * com.fabriziolarosa.testicemobile.Asyncs Created by Fabrizio La Rosa on 14/06/16.
 */
public class LoadFullMovieData extends AsyncTask<Void, Void, Void> {
    private Movie movie;
    private OnLoadedMovie onLoadedMoviecalback;

    public LoadFullMovieData(Movie movie, OnLoadedMovie onLoadedMoviecalback){
        this.movie=movie;
        this.onLoadedMoviecalback=onLoadedMoviecalback; //The callback Interface, when Movie is loaded
    }

    @Override
    protected synchronized Void doInBackground(Void... params) {
        HttpURLConnection connection = null;
        try{
            connection= (HttpURLConnection) new URL("http://www.omdbapi.com/?r=json&i="+movie.getImdbID()).openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();
            switch (connection.getResponseCode()){
                case 200:

                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder strBuild = new StringBuilder();
                    String line;
                    while ((line = buffReader.readLine()) != null) {
                        strBuild.append(line+"\n");
                    }
                    buffReader.close();
                    JSONObject jsonMovie=new JSONObject(strBuild.toString()); //Converting string response to JsonObject
                    movie.setVariablesFromJson(jsonMovie);
                    onLoadedMoviecalback.onFinish(movie);
                    break;
                default:
                    onLoadedMoviecalback.onError(new Exception("Error connection "+connection.getResponseCode())); //Callback Error
                    break;

            }


        }
        catch(Exception e){
            onLoadedMoviecalback.onError(e);
        }
        finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception error) {
                    onLoadedMoviecalback.onError(error);
                }
            }
        }
        return null;
    }


    public interface OnLoadedMovie{
        public void onFinish(Movie movie);
        public void onError(Exception error);

    }

}
