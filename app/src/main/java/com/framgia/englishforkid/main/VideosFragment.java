package com.framgia.englishforkid.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;
import com.framgia.englishforkid.videoview.PlayVideoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by duynguyenduc on 17/01/2017.
 */
public class VideosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
    VideoAdapter.OnItemClickListenner, VideosContract.View {
    final static int NUMBER_COLUMS_LIST = 1;
    final static int NUMBER_COLUMS_GRID = 2;
    @BindView(R.id.recycle_song)
    RecyclerView mSongRecycler;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private VideoAdapter mModelAdapter;
    private int mType;
    private VideosPresenter mPresenter;
    private View mView;

    public static VideosFragment newInstance(int type) {
        VideosFragment fragment = new VideosFragment();
        fragment.setType(type);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.song_layout, container, false);
        }
        ButterKnife.bind(this, mView);
        initPresenter();
        initView();
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void switchViewState(int viewType) {
        if (mSongRecycler == null) return;
        int numColums = viewType == VideoAdapter.STATE_LIST ?
            NUMBER_COLUMS_LIST : NUMBER_COLUMS_GRID;
        mSongRecycler.setLayoutManager(new GridLayoutManager(getContext(), numColums));
        mModelAdapter.setViewType(viewType);
        mModelAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateVideo(List<DataObject> datas) {
        mModelAdapter.updateData(datas);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }

    @Override
    public void onUpdate(String keySearch) {
        mPresenter.showSearchingData(keySearch);
    }

    @Override
    public void initView() {
        mModelAdapter = new VideoAdapter(getContext(), null, VideoAdapter.STATE_LIST, this);
        mSongRecycler.setLayoutManager(new GridLayoutManager(getContext(), NUMBER_COLUMS_LIST));
        mSongRecycler.setAdapter(mModelAdapter);
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.color_blue,
            R.color.color_grey,
            R.color.color_orange,
            R.color.color_red);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public void onLoadCompleted() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void notifyErrorNetwork() {
        Toast.makeText(getContext(), R.string.error_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    public void initPresenter() {
        mPresenter = new VideosPresenter(this,
            mType,
            new DataRepository(getContext()),
            new CompositeSubscription());
    }

    @Override
    public void initSubcribe() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubcribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubcribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subcribe();
    }

    @Override
    public void onClick(DataObject videoModel) {
        startActivity(PlayVideoActivity.getPlayVideoIntent(getContext(), videoModel));
    }
}
