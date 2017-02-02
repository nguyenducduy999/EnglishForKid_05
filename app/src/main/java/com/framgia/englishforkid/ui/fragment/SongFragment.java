package com.framgia.englishforkid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;
import com.framgia.englishforkid.ui.activity.PlayVideoActivity;
import com.framgia.englishforkid.ui.adapter.ModelAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duynguyenduc on 17/01/2017.
 */
public class SongFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
    ModelAdapter.OnItemClickListenner {
    final static int NUMBER_COLUMS_LIST = 1;
    final static int NUMBER_COLUMS_GRID = 2;
    @BindView(R.id.recycle_song)
    RecyclerView mSongRecycler;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ModelAdapter mModelAdapter;
    private List<DataObject> mListVideo;
    private Context mContext;
    private int mType;

    public static SongFragment newInstance(int type) {
        SongFragment fragment = new SongFragment();
        fragment.setType(type);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_layout, container, false);
        ButterKnife.bind(this, view);
        initView();
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

    @Override
    public void onRefresh() {
        //TODO reload data
    }

    @Override
    public void onClick(DataObject videoModel) {
        Intent intent = new Intent(mContext, PlayVideoActivity.class);
        startActivity(intent);
    }

    public void initView() {
        mListVideo = new ArrayList<>();
        mContext = getContext();
        mModelAdapter = new ModelAdapter(mContext, mListVideo, ModelAdapter.STATE_LIST, this);
        mSongRecycler.setLayoutManager(new GridLayoutManager(mContext, NUMBER_COLUMS_LIST));
        mSongRecycler.setAdapter(mModelAdapter);
        setDataForRecycleView(DataRepository.getDataFromProvider(mType));
    }
    private void setDataForRecycleView(List<DataObject> dataObjects) {
        mListVideo.clear();
        mListVideo.addAll(dataObjects);
        mModelAdapter.notifyDataSetChanged();
    }

    public void setType(int type) {
        mType = type;
    }
}
