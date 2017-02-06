package com.framgia.englishforkid.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.local.DataHelper;
import com.framgia.englishforkid.ui.fragment.VideoFragment;

/**
 * Created by duynguyenduc on 17/01/2017.
 */
public class VideoPagerAdapter extends FragmentStatePagerAdapter {
    public static final int POSITON_SONGS = 0;
    public static final int POSITON_SHORT_STORY = 1;
    public VideoFragment mSongFragment;
    public VideoFragment mStoryFragment;
    private String[] mTitle;

    public VideoPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitle = context.getResources().getStringArray(R.array.title_array);
        mSongFragment = VideoFragment.newInstance(DataHelper.TYPE_SONG);
        mStoryFragment = VideoFragment.newInstance(DataHelper.TYPE_STORY);
    }

    public void switchState(int viewType) {
        mSongFragment.switchViewState(viewType);
        mStoryFragment.switchViewState(viewType);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITON_SONGS:
                return mSongFragment;
            case POSITON_SHORT_STORY:
                return mStoryFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTitle != null ? mTitle.length : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
