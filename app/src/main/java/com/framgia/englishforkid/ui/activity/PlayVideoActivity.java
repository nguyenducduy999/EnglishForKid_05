package com.framgia.englishforkid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;
import com.framgia.englishforkid.ui.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayVideoActivity extends AppCompatActivity
    implements VideoAdapter.OnItemClickListenner {
    @BindView(R.id.recycle_random_video)
    public RecyclerView mRecyclerRandomVideo;
    private VideoAdapter mVideoAdapter;
    private List<DataObject> mListRandomVideo;
    private DataObject mVideo;
    final static String EXTRA_VIDEO = "EXTRA_VIDEO";

    public static Intent getPlayVideoIntent(Context context, DataObject video) {
        Intent intent = new Intent(context, PlayVideoActivity.class);
        intent.putExtra(EXTRA_VIDEO, video);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_controls_bar);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void onClick(DataObject videoModel) {
        //TODO play this video
    }

    private List<DataObject> addData() {
        mVideo = (DataObject) getIntent().getExtras().getSerializable(EXTRA_VIDEO);
        List data =
            new DataRepository(this).getRandomData(mVideo.getType(), mVideo.getId());
        mListRandomVideo.clear();
        mListRandomVideo.addAll(data);
        mVideoAdapter.notifyDataSetChanged();
        return data;
    }

    public void initViews() {
        mListRandomVideo = new ArrayList<>();
        mVideoAdapter = new VideoAdapter(this, mListRandomVideo, VideoAdapter.STATE_LIST, this);
        mRecyclerRandomVideo.setAdapter(mVideoAdapter);
        mRecyclerRandomVideo
            .setLayoutManager(new LinearLayoutManager(getBaseContext()));
        addData();
    }
}