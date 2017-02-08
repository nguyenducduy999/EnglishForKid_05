package com.framgia.englishforkid.fetchweb;

import com.framgia.englishforkid.BasePresenter;
import com.framgia.englishforkid.BaseView;

/**
 * Created by beepi on 19/01/2017.
 */
public interface SplashContract {
    public interface View extends BaseView {
        void startMainActivity();
        void showTryConnection();
        void notifyUseDataLocal();
    }

    public interface Presenter extends BasePresenter {

    }
}

