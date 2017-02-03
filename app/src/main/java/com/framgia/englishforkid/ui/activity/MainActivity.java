package com.framgia.englishforkid.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.ui.adapter.ModelAdapter;
import com.framgia.englishforkid.ui.adapter.SongPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.englishforkid.ui.adapter.ModelAdapter.STATE_GRID;
import static com.framgia.englishforkid.ui.adapter.ModelAdapter.STATE_LIST;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tab_layout)
    TabLayout mSongTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.imgeview_swich_state)
    FloatingActionButton mFloatingSwitchListGrid;
    private int mViewState = STATE_GRID;
    private SongPagerAdapter mSongPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mSongTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mSongPagerAdapter = new SongPagerAdapter(getSupportFragmentManager(),
            MainActivity.this);
        mViewPager.setAdapter(mSongPagerAdapter);
        mSongTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.imgeview_swich_state)
    public void onClick() {
        mViewState = mViewState == STATE_LIST ? ModelAdapter.STATE_GRID : ModelAdapter
            .STATE_LIST;
        mSongPagerAdapter.switchState(mViewState);
        setImageState(mViewState);
    }

    public void setImageState(int state) {
        mFloatingSwitchListGrid.setImageResource(state == STATE_LIST ?
            R.drawable.ic_list : R.drawable.ic_grid);
    }
}