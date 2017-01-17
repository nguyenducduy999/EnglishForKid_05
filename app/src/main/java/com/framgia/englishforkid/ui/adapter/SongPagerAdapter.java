package com.framgia.englishforkid.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.ui.fragment.ShortStoryFragment;
import com.framgia.englishforkid.ui.fragment.SongFragment;

/**
 * Created by duynguyenduc on 17/01/2017.
 */
public class SongPagerAdapter extends FragmentStatePagerAdapter {
    static final int POSITON_SONGS = 0;
    static final int POSITON_SHORT_STORY = 1;
    private String[] mTitle;

    public SongPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitle = context.getResources().getStringArray(R.array.title_array);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITON_SONGS:
                return new SongFragment();
            case POSITON_SHORT_STORY:
                return new ShortStoryFragment();
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

