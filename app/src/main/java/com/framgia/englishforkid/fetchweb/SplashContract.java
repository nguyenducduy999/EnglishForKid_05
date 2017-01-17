package com.framgia.englishforkid.fetchweb;

import android.content.Context;

import com.framgia.englishforkid.BasePresenter;
import com.framgia.englishforkid.BaseView;

/**
 * Created by beepi on 19/01/2017.
 */
public interface SplashContract {
    public interface View extends BaseView {
        public void onLoadCompleted();
        public Context getContext();
        public void onLoadError();
        public void initSubcribe();
    }

    public interface Presenter extends BasePresenter {
    }
}

