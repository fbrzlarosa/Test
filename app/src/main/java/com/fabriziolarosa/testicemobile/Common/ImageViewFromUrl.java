package com.fabriziolarosa.testicemobile.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fabriziolarosa.testicemobile.R;

import java.io.InputStream;

/**
 * Copyright 2016 {fbrzlarosa@gmail.com}
 * com.fabriziolarosa.testicemobile.Common Created by Fabrizio La Rosa on 14/06/16.
 */
public class ImageViewFromUrl extends ImageView {

    private String url;
    public ImageViewFromUrl(Context context) {
        super(context);
    }

    public ImageViewFromUrl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewFromUrl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUrl(String url){
        this.url=url;
    }
    public String getUrl(){
        return this.url;
    }
    public void loadImage(){ //Added functionality to retrieve an url image
        try {
            if(this.url!=null) {
                new DownloadImage(this,this.url).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); //See DownloadImage task
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class DownloadImage extends AsyncTask<Void, Void, Bitmap> { //Static implementation for the LruCache static implementation without instance
        ImageView imageView;
        String urlAsync;
        private static LruCache<String, Bitmap> cacheMemory;
        public DownloadImage(ImageView imageView,String urlAsync) {
            this.imageView = imageView;
            this.urlAsync=urlAsync;
            if(cacheMemory==null){
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8; // 1/8 of Max Memory Cache
                cacheMemory = new LruCache<String, Bitmap>(cacheSize) {
                    @Override
                    protected int sizeOf(String key, Bitmap bitmap) {
                        return bitmap.getByteCount() / 1024; //return the size of bitmap
                    }
                };
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap mImageBmp = getBmpFromCache(urlAsync); //Caching Core
            try {
                if(mImageBmp==null) {
                    InputStream in = new java.net.URL(urlAsync).openStream();
                    mImageBmp = BitmapFactory.decodeStream(in);
                    addBmpToMemoryChace(urlAsync,mImageBmp);
                }
                else{
                    return mImageBmp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mImageBmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result!=null) {
                if(imageView.getId()!=R.id.detail_movie_poster) { //Identifying the Image
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                }
                imageView.setImageBitmap(result);
            }
            else{
                imageView.setScaleType(ScaleType.CENTER);
                imageView.setImageResource(R.drawable.ic_report_problem_black_36dp);
            }
        }

        private void addBmpToMemoryChace(String key, Bitmap bitmap) {
            if (getBmpFromCache(key) == null) {
                cacheMemory.put(key, bitmap); //add Bitmap to Cache
            }
        }

        private Bitmap getBmpFromCache(String key) {
            try {
                return cacheMemory.get(key); //retrieve the bitmap to cache
            }
            catch(Exception errorMemoryBmp){
                errorMemoryBmp.printStackTrace();
                return null;
            }
        }


    }



}
