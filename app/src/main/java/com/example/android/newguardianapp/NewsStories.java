package com.example.android.newguardianapp;

/**
 * Created by Caspar on 02-Apr-18.
 */


public class NewsStories {

    private String mHeadline;

    private String mDate;

    private String mAuthor;

    private String mUrl;

    public NewsStories(String vHeadline, String vDate, String vAuthor, String vUrl) {
        mHeadline = vHeadline;
        mDate = vDate;
        mAuthor = vAuthor;
        mUrl = vUrl;
    }

    public String getHeadline(){return mHeadline;}

    public String getDate(){return mDate;}

    public String getAuthor(){return mAuthor;}

    public String getUrl(){return mUrl;}

}