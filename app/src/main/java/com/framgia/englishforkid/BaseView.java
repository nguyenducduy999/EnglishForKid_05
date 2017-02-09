package com.framgia.englishforkid;

/**
 * Created by beepi on 20/01/2017.
 */
public interface BaseView {
    void initPresenter();
    void initSubcribe();
    void onLoadCompleted();
    void notifyErrorNetwork();
}
