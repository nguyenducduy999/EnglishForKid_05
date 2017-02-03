package com.framgia.englishforkid.fetchweb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.ui.activity.MainActivity;

import rx.subscriptions.CompositeSubscription;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private SplashPresenter mPresenter;
    private ProgressBar mProgressBar;
    private TextView mTextViewStartLoad;
    private Button mButtonTryLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTextViewStartLoad = (TextView)findViewById(R.id.textview_startload);
        mButtonTryLoad = (Button)findViewById(R.id.buton_load_error);
        initPresenter();
        initSubcribe();
    }

    @Override
    public void onLoadCompleted() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLoadError() {
        mProgressBar.setVisibility(View.GONE);
        mTextViewStartLoad.setVisibility(View.GONE);
        mButtonTryLoad.setVisibility(View.VISIBLE);
    }

    @Override
    public void initSubcribe() {
        mPresenter.subcribe();
    }

    @Override
    public void initPresenter() {
        mPresenter =
            new SplashPresenter(this, new DataRepository(this), new CompositeSubscription());
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
        mPresenter.subcribe();
    }
}

