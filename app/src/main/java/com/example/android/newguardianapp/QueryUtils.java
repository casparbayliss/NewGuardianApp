package com.example.android.newguardianapp;


import android.app.DownloadManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caspar on 06-Apr-18.
 */

public final class QueryUtils {

    // Tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){
    }

    /** Query the USGS dataset and return a list of {@Link NewsStories} objects
     * @param requestUrl
     * @return
     */

    public static List<NewsStories> fetchNewsStoriesData(String requestUrl) {
        // Create a URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@Link Event} object
        List<NewsStories> newsStories = extractFeatureFromJson(jsonResponse);

        return newsStories;
    }

    // Return new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news stories JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsStories> extractFeatureFromJson(String newsStoriesJSON) {

        ArrayList<NewsStories> newsStories = new ArrayList<>();
        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(newsStoriesJSON)) {
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsStoriesJSON);
            // Declare a JSON Array for the news stories
            JSONArray newsStoriesArray;
            // Check that the baseJsonResponse has results in it
            if (baseJsonResponse.getJSONObject("response").has("results")) {
                newsStoriesArray =
                        baseJsonResponse.getJSONObject("response").getJSONArray("results");
            } else {
                return null;
            }

            // For each news story in the newsStories Array, create a {@Link NewsStories} object
            for(int i=0; i<newsStoriesArray.length(); i++) {
                // Get a single news story at position i within the list of news stories
                JSONObject currentNewsStory = newsStoriesArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String headline = currentNewsStory.getString("webTitle");
                // Extract the value for the key called "webPublicationDate"
                String date = currentNewsStory.getString("webPublicationDate");
                // Declare a value for the author name String
                String author = " ";
                // Extract the value for the key called "webUrl"
                String url = currentNewsStory.getString("webUrl");

                // Extract the JSONArray associated with the key "tags"
                JSONArray tagsArray = currentNewsStory.getJSONArray("tags");
                if(tagsArray.length() > 0) {
                    for (int j = 0; j < 1; j++) {
                        // extract the value for the key 'webTitle' (author)
                        JSONObject authorObj = tagsArray.getJSONObject(j);
                        try {
                            author = authorObj.getString("webTitle");
                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Missing one or more authors name JSONObject");
                        }
                    }
                }

                // Create a new {@Link NewsStory} object with the headline, date and
                // author from the JSON response
                NewsStories newsStory = new NewsStories(headline, date, author, url);

                newsStories.add(newsStory);

            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try"
            // block, catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news story JSON results", e);
        }

        // Return the list of news stories
        return newsStories;
    }

}


