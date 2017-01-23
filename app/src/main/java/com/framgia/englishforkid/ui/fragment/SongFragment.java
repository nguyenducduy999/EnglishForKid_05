package com.framgia.englishforkid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.model.VideoModel;
import com.framgia.englishforkid.ui.adapter.ModelAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duynguyenduc on 17/01/2017.
 */
public class SongFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    final static int NUMBER_COLUMS_LIST = 1;
    final static int NUMBER_COLUMS_GRID = 2;
    @BindView(R.id.recycle_song)
    RecyclerView mSongRecycler;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ModelAdapter mModelAdapter;
    private List<VideoModel> mListVideo;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_layout, container, false);
        ButterKnife.bind(this, view);
        mListVideo = new ArrayList<>();
        mContext = getContext();
        mModelAdapter = new ModelAdapter(mListVideo, mContext, ModelAdapter.STATE_LIST);
        mSongRecycler.setLayoutManager(new GridLayoutManager(mContext, NUMBER_COLUMS_LIST));
        mSongRecycler.setAdapter(mModelAdapter);
        fakeData();
        return view;
    }

    public void switchViewState(int viewType) {
        if (mSongRecycler == null) return;
        int numColums = viewType == ModelAdapter.STATE_LIST ?
            NUMBER_COLUMS_LIST : NUMBER_COLUMS_GRID;
        mSongRecycler.setLayoutManager(new GridLayoutManager(mContext, numColums));
        mModelAdapter.setViewType(viewType);
        mModelAdapter.notifyDataSetChanged();
    }

    private void fakeData() {
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello1"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello2"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello3"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello4"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello5"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello6"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello7"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello8"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello9"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello"));
        mListVideo.add(new VideoModel("http://goo.gl/gEgYUd", "Hello"));
        mModelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        //TODO reload data
    }
}

