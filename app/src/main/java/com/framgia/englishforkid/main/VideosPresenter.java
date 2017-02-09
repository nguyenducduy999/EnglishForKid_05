package com.framgia.englishforkid.main;

import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 19/01/2017.
 */
public class VideosPresenter implements VideosContract.Presenter {
    private VideosContract.View mView;
    private DataRepository mDataRepository;
    private CompositeSubscription mSubscriptions;
    private int mType;

    public VideosPresenter(VideosContract.View view,
                           int type,
                           DataRepository dataRepository,
                           CompositeSubscription compositeSubscription) {
        mView = view;
        mDataRepository = dataRepository;
        mSubscriptions = compositeSubscription;
        mType = type;
    }

    @Override
    public void subcribe() {
        getData();
    }

    @Override
    public void unsubcribe() {
        mSubscriptions.clear();
    }

    @Override
    public void notifyError() {
    }

    @Override
    public void showSearchingData(String keySearch) {
        List<DataObject> datas = mDataRepository.getSearchingData(keySearch, mType);
        mView.updateVideo(datas);
    }

    @Override
    public void getData() {
        List<DataObject> datas = mDataRepository.getDataFromProvider(mType);
        if (datas != null) mView.updateVideo(datas);
    }

    @Override
    public void refreshData() {
        mSubscriptions.clear();
        Observable<DataObject> objectObservable = mDataRepository.getDataObservable();
        if (objectObservable.isEmpty() != null) {
            mDataRepository.deleteData();
        }
        Subscription subscription = objectObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<DataObject>() {
                @Override
                public void onCompleted() {
                    getData();
                    mView.onLoadCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    mView.notifyErrorNetwork();
                    e.printStackTrace();
                }

                @Override
                public void onNext(DataObject dataObject) {
                    mDataRepository.saveData(dataObject);
                }
            });
        mSubscriptions.add(subscription);
    }
}

