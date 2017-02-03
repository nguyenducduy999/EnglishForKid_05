package com.framgia.englishforkid.fetchweb;

import com.framgia.englishforkid.data.local.DataRepository;
import com.framgia.englishforkid.data.model.DataObject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 19/01/2017.
 */
public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;
    private DataRepository mDataRepository;
    private CompositeSubscription mSubscriptions;

    public SplashPresenter(SplashContract.View view,
                           DataRepository dataRepository,
                           CompositeSubscription compositeSubscription) {
        mView = view;
        mDataRepository = dataRepository;
        mSubscriptions = compositeSubscription;
    }

    @Override
    public void subcribe() {
        mSubscriptions.clear();
        Observable<DataObject> objectObservable = mDataRepository.getDataObservable();
        if (objectObservable.isEmpty() == null) {
            mDataRepository.deleteData();
        }
        Subscription subscription = objectObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<DataObject>() {
                @Override
                public void onCompleted() {
                    mView.onLoadCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    notifyError();
                    e.printStackTrace();
                }

                @Override
                public void onNext(DataObject dataObject) {
                    mDataRepository.saveData(dataObject);
                }
            });
        mSubscriptions.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscriptions.clear();
    }

    @Override
    public void notifyError() {
        if (mDataRepository.checkAvailableData()) {
            mView.notifyUseDataLocal();
            mView.startMainActivity();
        } else {
            mView.notifyErrorNetwork();
            mView.showTryConnection();
        }
    }
}

