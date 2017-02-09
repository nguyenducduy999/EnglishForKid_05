package com.framgia.englishforkid.videoview;

import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;
import com.framgia.englishforkid.util.Constant;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.framgia.englishforkid.util.Constant.DOWNLOAD_DIRECTORY;

/**
 * Created by beepi on 07/02/2017.
 */
public class VideoPresenter implements VideoContract.Presenter {
    private VideoContract.View mView;
    private DataRepository mDataRepository;
    private CompositeSubscription mSubscriptions;
    private DataObject mDataVideo;

    public VideoPresenter(VideoContract.View view, DataRepository dataRepository,
                          CompositeSubscription subscription) {
        mView = view;
        mDataRepository = dataRepository;
        mSubscriptions = subscription;
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
        if (mDataRepository
            .checkExistFile(DOWNLOAD_DIRECTORY.concat(mDataVideo.getTitle().trim()))) {
            mView.setupVideoView(DOWNLOAD_DIRECTORY.concat(mDataVideo.getTitle().trim()));
        } else if (mDataRepository.getUrlVideoLocal(mDataVideo) != null) {
            mView.setupVideoView(getUrlVideo());
        } else mView.initSubcribe();
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
                    mView.onLoadCompleted();
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
