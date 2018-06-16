package com.example.android.newguardianapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by Caspar on 06-Apr-18.
 */

// Load a list of news stories by using an AsyncTask to perform the
// network request for the given URL
public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStories>> {

    // Tag for log messages
    private static final String LOG_TAG = NewsStoryLoader.class.getName();

    // Query URL
    private String mUrl;

    /**
     * Constructs a new {@Link NewsStoryLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public NewsStoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e("onStartLoading", "Working");
    }

    // This is on a background thread

    @Override
    public List<NewsStories> loadInBackground() {
        Log.e("loadInBackground", "Working");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news stories
        List<NewsStories> newsStories = QueryUtils.fetchNewsStoriesData(mUrl);
        return newsStories;
    }
}
