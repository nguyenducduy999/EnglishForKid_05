package com.framgia.englishforkid.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.ui.adapter.VideoAdapter;
import com.framgia.englishforkid.ui.adapter.VideoPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.englishforkid.ui.adapter.VideoAdapter.STATE_GRID;
import static com.framgia.englishforkid.ui.adapter.VideoAdapter.STATE_LIST;

public class MainActivity extends AppCompatActivity
    implements android.widget.SearchView.OnQueryTextListener {
    @BindView(R.id.tab_layout)
    TabLayout mSongTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.imgeview_swich_state)
    FloatingActionButton mFloatingSwitchListGrid;
    private int mViewState = STATE_GRID;
    private VideoPagerAdapter mSongPagerAdapter;
    @BindView(R.id.searchview)
    SearchView mSearchView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mSongTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mSongPagerAdapter = new VideoPagerAdapter(getSupportFragmentManager(),
            MainActivity.this);
        mViewPager.setAdapter(mSongPagerAdapter);
        mSongTabLayout.setupWithViewPager(mViewPager);
        mSearchView.setOnQueryTextListener(this);
    }

    @OnClick(R.id.imgeview_swich_state)
    public void onClick() {
        mViewState = mViewState == STATE_LIST ? VideoAdapter.STATE_GRID : VideoAdapter
            .STATE_LIST;
        mSongPagerAdapter.switchState(mViewState);
        setImageState(mViewState);
    }

    public void setImageState(int state) {
        mFloatingSwitchListGrid.setImageResource(state == STATE_LIST ?
            R.drawable.ic_list : R.drawable.ic_grid);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSongPagerAdapter.setKeySearch(newText);
        mViewPager.getAdapter().notifyDataSetChanged();
        return true;
    }
}


