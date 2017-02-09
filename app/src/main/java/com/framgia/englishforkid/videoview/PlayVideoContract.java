package com.framgia.englishforkid.videoview;

import com.framgia.englishforkid.BasePresenter;
import com.framgia.englishforkid.BaseView;
import com.framgia.englishforkid.data.model.DataObject;

import java.util.List;

/**
 * Created by beepi on 07/02/2017.
 */
public interface PlayVideoContract {
    interface View extends BaseView {
        void initPresenter();
        void setupVideoView(String directoryVideo);
        void onSetOrientationPortrait();
        void onSetOrientationLandscape();
        void showConfirmDialog();
        void showErrorDownload();
        void showSuccessfulDownload();
        void showStartDownload();
        void showExistFileDownload();
        boolean checkPermission();
        void getRandomVideo();
        void initView();
    }

    interface Presenter extends BasePresenter {
        void notifyError();
        void setDataVideo(DataObject data);
        String getUrlVideo();
        String createUrlVideo(String idVideo);
        void onPrepareVideo();
        void downLoadVideo();
        boolean isSDCardPresent();
        byte[] startDownload();
        void setupStatusSubcribe(String status);
        String getFileName();
        String getPathOffline(String nameFile);
        List<DataObject> getRandomData();
    }
}
