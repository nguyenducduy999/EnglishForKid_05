package com.framgia.englishforkid.data.local;

import android.net.Uri;

import com.framgia.englishforkid.data.model.DataObject;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 20/01/2017.
 */
public interface RepositoryContract {
    public List<DataObject> getDataObject(String urlWeb);
    public Observable<DataObject> getDataObservable();
    public Uri saveData(DataObject dataObject);
    public int deleteData();
}

