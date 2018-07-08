package com.example.android.newguardianapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FootballFragment();
        }
        else if (position == 1) {
            return new PoliticsFragment();
        }
        else if (position == 2) {
            return new CultureFragment();
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.football_title);
        }
        else if (position == 1){
            return mContext.getString(R.string.politics_title);
        }
        else {
            return mContext.getString(R.string.culture_title);
        }
    }
}
