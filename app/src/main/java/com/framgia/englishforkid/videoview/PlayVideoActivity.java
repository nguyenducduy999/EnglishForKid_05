package com.framgia.englishforkid.videoview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;
import com.framgia.englishforkid.ui.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public class PlayVideoActivity extends AppCompatActivity
    implements VideoAdapter.OnItemClickListenner, VideoContract.View,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, View.OnClickListener {
    final static String EXTRA_VIDEO = "EXTRA_VIDEO";
    @BindView(R.id.recycle_random_video)
    public RecyclerView mRecyclerRandomVideo;
    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.image_fullscreen)
    ImageButton mImageButtonFullScreen;
    @BindView(R.id.image_exit_full_screen)
    ImageButton mImageButtonExitFullScreen;
    private VideoAdapter mVideoAdapter;
    private List<DataObject> mListRandomVideo;
    private DataObject mVideo;
    private MediaController mMediaController;
    private VideoPresenter mPresenter;
    private int currentPosition = 0;

    public static Intent getPlayVideoIntent(Context context, DataObject video) {
        Intent intent = new Intent(context, PlayVideoActivity.class);
        intent.putExtra(EXTRA_VIDEO, video);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        initPresenter();
        initViews();
    }

    @Override
    public void onClick(DataObject videoModel) {
        //TODO play this video
    }

    private List<DataObject> addData() {
        mVideo = (DataObject) getIntent().getExtras().getSerializable(EXTRA_VIDEO);
//        List data =
//            new DataRepository(this).getRandomData(mVideo.getType(), mVideo.getId());
//        mListRandomVideo.clear();
//        mListRandomVideo.addAll(data);
//        mVideoAdapter.notifyDataSetChanged();
        mPresenter.setDataVideo(mVideo);
        return null;
    }

    public void initViews() {
        mListRandomVideo = new ArrayList<>();
        mVideoAdapter = new VideoAdapter(this, mListRandomVideo, VideoAdapter.STATE_LIST, this);
        mRecyclerRandomVideo.setAdapter(mVideoAdapter);
        mRecyclerRandomVideo
            .setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mImageButtonFullScreen.setOnClickListener(this);
        mImageButtonExitFullScreen.setOnClickListener(this);
        mMediaController = new MediaController(this);
        mVideoView.setOnPreparedListener(this);
        addData();
    }

    @Override
    public void initPresenter() {
        mPresenter =
            new VideoPresenter(this, new DataRepository(this), new CompositeSubscription());
    }

    @Override
    public void initSubcribe() {
        mPresenter.subcribe();
    }

    @Override
    public void onLoadCompleted() {
        setupVideoView(mPresenter.getUrlVideo());
    }

    @Override
    public void notifyErrorNetwork() {
        Toast.makeText(this, R.string.msg_video_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setupVideoView(String directoryVideo) {
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(directoryVideo);
        mVideoView.requestFocus();
    }

    @Override
    public void onSetOrientationPortrait() {
        mImageButtonFullScreen.setVisibility(View.VISIBLE);
        mImageButtonExitFullScreen.setVisibility(View.GONE);
    }

    @Override
    public void onSetOrientationLandscape() {
        mImageButtonFullScreen.setVisibility(View.GONE);
        mImageButtonExitFullScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mVideoView.seekTo(currentPosition);
        if (currentPosition == 0) {
            mVideoView.start();
        }
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        mMediaController.setAnchorView(mVideoView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubcribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubcribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onPrepareVideo();
    }

    // change direct phone
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVideoView.seekTo(currentPosition);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_fullscreen:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.image_exit_full_screen:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.image_download:
                // todo download video
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onSetOrientationLandscape();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            onSetOrientationPortrait();
        }
    }
}