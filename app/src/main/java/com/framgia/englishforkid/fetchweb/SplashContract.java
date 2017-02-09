package com.framgia.englishforkid.fetchweb;

import com.framgia.englishforkid.BasePresenter;
import com.framgia.englishforkid.BaseView;

/**
 * Created by beepi on 19/01/2017.
 */
public interface SplashContract {
    interface View extends BaseView {
        void notifyUseDataLocal();
        void initSubcribe();
        void startMainActivity();
        void showTryConnection();
    }

    interface Presenter extends BasePresenter {
        void notifyError();
    }
}

