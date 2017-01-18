package com.framgia.englishforkid.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.ui.adapter.SongPagerAdapter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager songViewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(songViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapter pagerAdapter = new SongPagerAdapter(getSupportFragmentManager(),
            MainActivity.this);
        songViewPager.setAdapter(pagerAdapter);
    }
}

