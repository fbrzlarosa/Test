package com.fabriziolarosa.testicemobile.Common;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Copyright 2016 {fbrzlarosa@gmail.com}
 * com.fabriziolarosa.testicemobile.Adapters Created by Fabrizio La Rosa on 14/06/16.
 */
public class Movie implements Parcelable { //implements Parcelable for the Bundle
    protected String title;
    protected String year;
    protected String rated;
    protected String released;
    protected String runtime;
    protected String genre;
    protected String director;
    protected String writer;
    protected String actors;
    protected String plot;
    protected String language;
    protected String country;
    protected String awards;
    protected String poster;
    protected String metascore;
    protected float imdbRating;
    protected float imdbVotes;
    protected String imdbID;
    protected String type;
    protected boolean response;

    public Movie(String id){
        this.imdbID=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public float getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(float imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public void setVariablesFromJson(JSONObject jsonMovie){ //Converting the Json, to primitive variables
        try {
            if (jsonMovie.has("Title")) {
                title = jsonMovie.getString("Title");
            }
            if (jsonMovie.has("Year")) {
                year = jsonMovie.getString("Year");
            }
            if (jsonMovie.has("Rated")) {
                year = jsonMovie.getString("Rated");
            }
            if (jsonMovie.has("Released")) {
                released = jsonMovie.getString("Released");
            }
            if (jsonMovie.has("Runtime")) {
                runtime = jsonMovie.getString("Runtime");
            }
            if (jsonMovie.has("Genre")) {
                genre = jsonMovie.getString("Genre");
            }
            if (jsonMovie.has("Director")) {
                director = jsonMovie.getString("Director");
            }
            if (jsonMovie.has("Writer")) {
                writer = jsonMovie.getString("Writer");
            }
            if (jsonMovie.has("Actors")) {
                actors = jsonMovie.getString("Actors");
            }
            if (jsonMovie.has("Plot")) {
                plot = jsonMovie.getString("Plot");
            }
            if (jsonMovie.has("Language")) {
                language = jsonMovie.getString("Language");
            }
            if (jsonMovie.has("Country")) {
                country = jsonMovie.getString("Country");
            }
            if (jsonMovie.has("Awards")) {
                awards = jsonMovie.getString("Awards");
            }
            if (jsonMovie.has("Poster")) {
                poster = jsonMovie.getString("Poster");
            }
            if (jsonMovie.has("Metascore")) {
                metascore= jsonMovie.getString("Metascore");
            }
            if (jsonMovie.has("imdbRating")) {
                try {
                    imdbRating = Float.parseFloat(jsonMovie.getString("imdbRating"));
                }
                catch (Exception parsingFloat){
                    imdbRating=0.0f;
                    parsingFloat.printStackTrace();
                }
            }
            if (jsonMovie.has("imdbId")) {
                imdbID = jsonMovie.getString("imdbId");
            }
            if (jsonMovie.has("imdbVotes")) {
                try {
                    imdbVotes = Float.parseFloat(jsonMovie.getString("imdbVotes").replace(",", "."));
                }
                catch(Exception parsingFloat){
                    imdbVotes=0.0f;
                    parsingFloat.printStackTrace();
                }
            }
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.year);
        dest.writeString(this.rated);
        dest.writeString(this.released);
        dest.writeString(this.runtime);
        dest.writeString(this.genre);
        dest.writeString(this.director);
        dest.writeString(this.writer);
        dest.writeString(this.actors);
        dest.writeString(this.plot);
        dest.writeString(this.language);
        dest.writeString(this.country);
        dest.writeString(this.awards);
        dest.writeString(this.poster);
        dest.writeString(this.metascore);
        dest.writeFloat(this.imdbRating);
        dest.writeFloat(this.imdbVotes);
        dest.writeString(this.imdbID);
        dest.writeString(this.type);
        dest.writeByte(this.response ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.year = in.readString();
        this.rated = in.readString();
        this.released = in.readString();
        this.runtime = in.readString();
        this.genre = in.readString();
        this.director = in.readString();
        this.writer = in.readString();
        this.actors = in.readString();
        this.plot = in.readString();
        this.language = in.readString();
        this.country = in.readString();
        this.awards = in.readString();
        this.poster = in.readString();
        this.metascore = in.readString();
        this.imdbRating = in.readFloat();
        this.imdbVotes = in.readFloat();
        this.imdbID = in.readString();
        this.type = in.readString();
        this.response = in.readByte() != 0;
    }

    //Parcelling the object
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
