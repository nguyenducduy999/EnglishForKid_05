package com.framgia.englishforkid.main;

import com.framgia.englishforkid.BasePresenter;
import com.framgia.englishforkid.BaseView;
import com.framgia.englishforkid.data.model.DataObject;

import java.util.List;

/**
 * Created by duynguyenduc on 08/02/2017.
 */
public interface VideosContract {
    interface View extends BaseView {
        void onLoadCompleted();
        void notifyErrorNetwork();
        void onUpdate(String keySearch);
        void updateVideo(List<DataObject> datas);
        void switchViewState(int viewType);
        void initView();
    }

    interface Presenter extends BasePresenter {
        void showSearchingData(String keySearch);
        void getData();
        void refreshData();
    }
}
