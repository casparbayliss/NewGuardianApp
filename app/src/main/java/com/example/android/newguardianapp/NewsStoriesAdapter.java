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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Caspar on 03-Apr-18.
 */

public class NewsStoriesAdapter extends ArrayAdapter<NewsStories> {

    public NewsStoriesAdapter(Activity context, ArrayList<NewsStories> newsStories) {
        super(context, 0, newsStories);
    }

    // Location separators to remove the letters from the date
    private static final String LOCATION_SEPARATOR_1 = "T";
    private static final String LOCATION_SEPARATOR_2 = "Z";

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
        // Store the article headline as a string
        String articleHeadline = currentNewsStory.getHeadline();
        // Set the text on the headline TextView to the headline for the appropriate News Story
        headlineTextView.setText(articleHeadline);

        // Get the unformatted date from the News Story object
        String initialDate = currentNewsStory.getDate();
        // Set the date as a string
        String formattedDate;
        // Set the time half after the Location Separator as a string
        String timeHalf;
        // Set the time as a string
        String formattedTime;

        // Remove the letters from the date
        if (initialDate.contains(LOCATION_SEPARATOR_1)) {
            String[] dateParts = initialDate.split(LOCATION_SEPARATOR_1);
            formattedDate = dateParts[0];
            timeHalf = dateParts[1];
            if (timeHalf.contains(LOCATION_SEPARATOR_2)) {
                String[] timeParts = timeHalf.split(LOCATION_SEPARATOR_2);
                formattedTime = timeParts[0];
            }
            else {
                formattedTime = " ";
            }
        } else {
            formattedDate = initialDate;
            formattedTime = " ";
        }


        // Get the TextView for the date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        // Set the date on the date TextView to the date for the appropriate News Story
        dateTextView.setText(formattedDate + " " + formattedTime);

        // Get the TextView for the Author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        // Store the author name as a string
        String authorName = currentNewsStory.getAuthor();
        // Set the author name on the author TextView to the author appropriate for the story
        // authorTextView.setText(authorName);
        // Set the author TextView to invisible if there is no data
        if (authorName.equals(" ")) {
            authorTextView.setVisibility(View.GONE);
        }
        else {
            authorTextView.setText(authorName);
        }

        // Get the TextView for the URL
        // TextView urlTextView = (TextView) listItemView.findViewById(R.id.url_text_view);
        // Store the URL as a string
        // String urlAddress = currentNewsStory.getUrl();
        // Set the URL on the URL TextView to the URl for the appropriate News Story
        // urlTextView.setText(urlAddress);

        // Return the whole list item layout: three TextViews.
        return listItemView;
    }
}
