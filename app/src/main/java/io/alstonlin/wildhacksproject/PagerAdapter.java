package io.alstonlin.wildhacksproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 2;
    private AppActivity activity;


    public PagerAdapter(FragmentManager fm, AppActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CameraFragment cam = CameraFragment.newInstance(activity);
                return cam;
            case 1:
                FeedFragment feed = FeedFragment.newInstance(activity);
                return feed;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}