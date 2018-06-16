package com.example.android.newguardianapp;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootballFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsStories>> {


    public FootballFragment() {
        // Required empty public constructor
    }



    private static final int NEWS_STORIES_LOADER_ID = 1;

    // Create the URL for the query
    private static final String USGS_REQUEST_URL =
            "http://content.guardianapis.com/search?show-tags=contributor&section=football&order-by=newest&api-key=4d140bcc-14e5-4324-9d3e-2ed236257e30";

    // Adapter for the news stories
    private NewsStoriesAdapter mAdapter;

    // TextView for the empty list of news stories
    private TextView mEmptyStateTextView;

    // TextView for the loading spinner
    private ProgressBar progressSpinner;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.list_item, container, false);



        // Get the SwipeContainerLayout
        final SwipeRefreshLayout swipeLayout = rootView.findViewById(R.id.swipe_container);

        // Add a listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Check for Internet connection
                ConnectivityManager reloadConnectivityManager = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo reloadNetworkInfo = reloadConnectivityManager.getActiveNetworkInfo();
                if (reloadNetworkInfo != null && reloadNetworkInfo.isConnected()) {
                    // Refresh the loader
                    LoaderManager refreshLoaderManager = getLoaderManager();
                    refreshLoaderManager.initLoader(NEWS_STORIES_LOADER_ID, null, FootballFragment.this);
                } else {
                    // Otherwise, display error
                    //First, hide the the progress spinner so the error message will be visible
                    View progressSpinner = rootView.findViewById(R.id.progress_spinner);
                    progressSpinner.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    mEmptyStateTextView.setText("No internet connection");
                }

                // Toast message to say that it is refreshing
                Toast.makeText(getActivity().getApplicationContext(), "Refreshing",
                        Toast.LENGTH_SHORT).show();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000); // Delay in millis
            }
        });



        // Find the listView and assign it a variable name
        ListView newsStoriesListView = (ListView) rootView.findViewById(R.id.list_view);

        // Set the empty list to the no news view
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.no_news);
        // newsStoriesListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of news stories as input
        mAdapter = new NewsStoriesAdapter(getActivity(), new ArrayList<NewsStories>());

        // Set the adapter on the LsitView so that it can be populated
        newsStoriesListView.setAdapter(mAdapter);


        newsStoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news story that was clicked on
                NewsStories currentNewsStory = mAdapter.getItem(position);

                // Convert the string URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentNewsStory.getUrl());

                // Create a new intent to view the news story URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Use the intent to start a new activity
                startActivity(websiteIntent);
            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if(networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialise the loader
            loaderManager.initLoader(NEWS_STORIES_LOADER_ID, null, FootballFragment.this);
        } else {
            // Otherwise, display error
            //First, hide the the progress spinner so the error message will be visible
            View progressSpinner = rootView.findViewById(R.id.progress_spinner);
            progressSpinner.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No internet connection");
        }



        return rootView;


    }

    @Override
    public Loader<List<NewsStories>> onCreateLoader(int i, Bundle args) {
        Log.e("onCreateLoader", "working");
        // Create a new Loader for the given URL
        return new NewsStoryLoader(getContext(), USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStories>> loader, List<NewsStories> newsStories) {
        // Log if it is working
        Log.e("onLoadFinished", "working");
        // Hide progress spinner because the data has been loaded
        View progressSpinner = getActivity().findViewById(R.id.progress_spinner);
        progressSpinner.setVisibility(View.GONE);
        // Clear the adapter of the previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@Link NewsStories}s, then add them to the adapter's
        // data set. This will trigger the ListView to update
        if (newsStories != null && !newsStories.isEmpty()) {
            mAdapter.addAll(newsStories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsStories>> loader) {
        Log.e("onLoaderReset", "working");
        // Clear the adapter
        mAdapter.clear();
    }
}
