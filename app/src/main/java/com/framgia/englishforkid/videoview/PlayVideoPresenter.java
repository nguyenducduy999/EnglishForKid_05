package com.framgia.englishforkid.videoview;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;

import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;
import com.framgia.englishforkid.util.Constant;

import java.io.File;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by beepi on 07/02/2017.
 */
public class PlayVideoPresenter implements PlayVideoContract.Presenter {
    private final static String VIDEO_EXTENSION = ".mp4";
    private final static String SUBCRIBE_STATUS_ONLINE = "online";
    private final static String SUBCRIBE_STATUS_DOWNLOAD = "download";
    private static String SUBCRIBE_STATUS = "status";
    private PlayVideoContract.View mView;
    private DataRepository mDataRepository;
    private CompositeSubscription mSubscriptions;
    private DataObject mDataVideo;
    private ConnectivityManager mConnectivityManager;

    public PlayVideoPresenter(PlayVideoContract.View view,
                              DataRepository dataRepository,
                              CompositeSubscription subscription,
                              ConnectivityManager connectivityManager) {
        mView = view;
        mDataRepository = dataRepository;
        mSubscriptions = subscription;
        mConnectivityManager = connectivityManager;
    }

    @Override
    public void notifyError() {
        mView.notifyErrorNetwork();
    }

    @Override
    public void setDataVideo(DataObject data) {
        mDataVideo = data;
    }

    @Override
    public String getUrlVideo() {
        return mDataRepository.getUrlVideoLocal(mDataVideo);
    }

    @Override
    public String createUrlVideo(String idVideo) {
        return String.format(Constant.URL_VIDEO_BASE, idVideo);
    }

    @Override
    public void onPrepareVideo() {
        if (mDataRepository.checkExistFile(getFileName())) {
            mView.setupVideoView(getPathOffline(getFileName()));
        } else if (mDataRepository.getUrlVideoLocal(mDataVideo) != null) {
            mView.setupVideoView(getUrlVideo());
        } else {
            setupStatusSubcribe(SUBCRIBE_STATUS_ONLINE);
            mView.initSubcribe();
        }
    }

    @Override
    public void downLoadVideo() {
        String nameFile = getFileName();
        if (mConnectivityManager.isInternetConnect() && isSDCardPresent()) {
            DownloadManager downLoadManager;
            downLoadManager = (DownloadManager) mConnectivityManager.getContext().getSystemService
                (DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(mDataRepository.getUrlVideoLocal(mDataVideo));
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameFile);
            downLoadManager.enqueue(request);
        } else {
            mView.showErrorDownload();
        }
    }

    @Override
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    @Override
    public byte[] startDownload() {
        if (mDataRepository.checkExistFile(getFileName())) {
            mView.showExistFileDownload();
            return new byte[0];
        }
        if (mView.checkPermission()) {
            setupStatusSubcribe(SUBCRIBE_STATUS_DOWNLOAD);
            mView.initSubcribe();
        }
        return new byte[0];
    }

    @Override
    public void setupStatusSubcribe(String status) {
        SUBCRIBE_STATUS = status;
    }

    @Override
    public String getFileName() {
        String nameFile = mDataVideo.getTitle().trim().replace(" ", "_");
        return nameFile + VIDEO_EXTENSION;
    }

    @Override
    public String getPathOffline(String nameFile) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment
            .DIRECTORY_DOWNLOADS),
            nameFile).getPath();
    }

    @Override
    public List<DataObject> getRandomData() {
        return mDataRepository.getRandomData(mDataVideo.getType(), mDataVideo.getId());
    }

    @Override
    public void subcribe() {
        mSubscriptions.clear();
        Subscription subscription = mDataRepository
            .getIdVideoObservable(mDataVideo)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    switch (SUBCRIBE_STATUS) {
                        case SUBCRIBE_STATUS_ONLINE:
                            mView.onLoadCompleted();
                            break;
                        case SUBCRIBE_STATUS_DOWNLOAD:
                            downLoadVideo();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onError(Throwable e) {
                    mView.notifyErrorNetwork();
                }

                @Override
                public void onNext(String s) {
                    if (s == null) return;
                    mDataVideo.setUrlVideo(createUrlVideo(s));
                    mDataRepository.updateUrlVideo(mDataVideo);
                }
            });
        mSubscriptions.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscriptions.clear();
    }
}
