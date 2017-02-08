package com.framgia.englishforkid.videoview;

import com.framgia.englishforkid.BasePresenter;
import com.framgia.englishforkid.BaseView;
import com.framgia.englishforkid.data.model.DataObject;

/**
 * Created by beepi on 07/02/2017.
 */
public interface VideoContract {
    interface View extends BaseView {
        void initPresenter();
        void setupVideoView(String directoryVideo);
        void onSetOrientationPortrait();
        void onSetOrientationLandscape();
    }

    interface Presenter extends BasePresenter {
        void notifyError();
        void setDataVideo(DataObject data);
        String getUrlVideo();
        String createUrlVideo(String idVideo);
        void onPrepareVideo();
    }
}
