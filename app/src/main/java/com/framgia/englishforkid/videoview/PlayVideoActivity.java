package com.framgia.englishforkid.videoview;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.framgia.englishforkid.main.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

import static com.framgia.englishforkid.util.Constant.REQUEST_WRITE_PERMISION;

public class PlayVideoActivity extends AppCompatActivity
    implements VideoAdapter.OnItemClickListenner, PlayVideoContract.View,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, View.OnClickListener {
    private final static String EXTRA_VIDEO = "EXTRA_VIDEO";
    private final String TAG = this.getClass().getSimpleName();
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
    @BindView(R.id.image_download)
    ImageButton mImageButtonDownload;
    private VideoAdapter mVideoAdapter;
    private List<DataObject> mListRandomVideo;
    private DataObject mVideo;
    private MediaController mMediaController;
    private PlayVideoPresenter mPresenter;
    private int currentPosition = 0;
    private BroadcastReceiver mReceiverDownLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                showSuccessfulDownload();
            }
        }
    };

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
        mVideo = (DataObject) getIntent().getExtras().getSerializable(EXTRA_VIDEO);
        initPresenter();
        initView();
    }

    @Override
    public void onClick(DataObject videoModel) {
        mPresenter.setDataVideo(videoModel);
        mVideoView.stopPlayback();
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.onPrepareVideo();
        getRandomVideo();
    }

    @Override
    public void initView() {
        mListRandomVideo = new ArrayList<>();
        mVideoAdapter = new VideoAdapter(this, mListRandomVideo, VideoAdapter.STATE_LIST, this);
        mRecyclerRandomVideo.setAdapter(mVideoAdapter);
        mRecyclerRandomVideo
            .setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mImageButtonFullScreen.setOnClickListener(this);
        mImageButtonExitFullScreen.setOnClickListener(this);
        mMediaController = new MediaController(this);
        mVideoView.setOnPreparedListener(this);
        getRandomVideo();
        registerReceiver(mReceiverDownLoad, new IntentFilter(
            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @OnClick(R.id.image_download)
    public void onDownload(View view) {
        showStartDownload();
        mPresenter.startDownload();
    }

    @Override
    public void initPresenter() {
        mPresenter =
            new PlayVideoPresenter(this, new DataRepository(this), new CompositeSubscription(),
                com.framgia.englishforkid.videoview.ConnectivityManager.getInstance(this));
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
        mRecyclerRandomVideo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSetOrientationLandscape() {
        mImageButtonFullScreen.setVisibility(View.GONE);
        mImageButtonExitFullScreen.setVisibility(View.VISIBLE);
        mRecyclerRandomVideo.setVisibility(View.GONE);
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
        unregisterReceiver(mReceiverDownLoad);
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

    @Override
    public void getRandomVideo() {
        mPresenter.setDataVideo(mVideo);
        mListRandomVideo = new ArrayList<>();
        mVideoAdapter = new VideoAdapter(this, mListRandomVideo, VideoAdapter.STATE_LIST, this);
        mRecyclerRandomVideo.setAdapter(mVideoAdapter);
        mRecyclerRandomVideo
            .setLayoutManager(new LinearLayoutManager(getBaseContext()));
        List data = mPresenter.getRandomData();
        mListRandomVideo.clear();
        mListRandomVideo.addAll(data);
        mVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showConfirmDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.title_dialog)
            .setMessage(R.string.msg_infor)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(PlayVideoActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_PERMISION);
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .create()
            .show();
    }

    @Override
    public void showErrorDownload() {
        Toast.makeText(PlayVideoActivity.this, R.string.error_fail
            , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessfulDownload() {
        Toast.makeText(PlayVideoActivity.this, R.string.msg_done
            , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStartDownload() {
        Toast.makeText(PlayVideoActivity.this, R.string.msg_start_download
            , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showExistFileDownload() {
        Toast.makeText(PlayVideoActivity.this, R.string.msg_exist_file
            , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
            .WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                PlayVideoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showConfirmDialog();
            } else {
                ActivityCompat.requestPermissions(PlayVideoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISION);
            }
        } else return true;
        return false;
    }
}