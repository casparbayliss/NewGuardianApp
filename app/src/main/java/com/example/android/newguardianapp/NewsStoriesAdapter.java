package com.example.android.newguardianapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.newguardianapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Caspar on 03-Apr-18.
 */

public class NewsStoriesAdapter extends ArrayAdapter<NewsStories> {

    public NewsStoriesAdapter(Activity context, ArrayList<NewsStories> newsStories) {
        super(context, 0, newsStories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the NewsStories object located at positions in the list
        NewsStories currentNewsStory = getItem(position);

        // Get the TextView for the headline
        TextView headlineTextView = (TextView) listItemView.findViewById(R.id.headline_text_view);
        // Store hte article headline as a string
        String articleHeadline = currentNewsStory.getHeadline();
        // Set the text on the headline TextView to the headline for the appropriate News Story
        headlineTextView.setText(articleHeadline);

        // Get the TextView for the date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        // Store the date as a string
        String headlineDate = currentNewsStory.getDate();
        // Set the date on the date TextView to the date for the appropriate News Story
        dateTextView.setText(headlineDate);

        // Get the TextView for the URL
        TextView urlTextView = (TextView) listItemView.findViewById(R.id.url_text_view);
        // Store the URL as a string
        String urlAddress = currentNewsStory.getUrl();
        // Set the URL on the URL TextView to the URl for the appropriate News Story
        urlTextView.setText(urlAddress);

        // Return the whole list item layout: three TextViews.
        return listItemView;
    }
}
