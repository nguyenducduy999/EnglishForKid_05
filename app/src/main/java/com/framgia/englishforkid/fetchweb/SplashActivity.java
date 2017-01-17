package com.framgia.englishforkid.fetchweb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.ui.activity.MainActivity;

import rx.subscriptions.CompositeSubscription;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sflash);
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

