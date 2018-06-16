package com.example.android.newguardianapp;

/**
 * Created by Caspar on 02-Apr-18.
 */


public class NewsStories {

    private String mHeadline;

    private String mDate;

    private String mUrl;

    public NewsStories(String vHeadline, String vDate, String vUrl) {
        mHeadline = vHeadline;
        mDate = vDate;
        mUrl = vUrl;
    }

    public String getHeadline(){return mHeadline;}

    public String getDate(){return mDate;}

    public String getUrl(){return mUrl;}

}