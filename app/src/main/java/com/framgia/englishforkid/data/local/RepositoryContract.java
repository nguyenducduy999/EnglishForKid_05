package com.framgia.englishforkid.data.local;

import android.database.Cursor;
import android.net.Uri;

import com.framgia.englishforkid.data.model.DataObject;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 20/01/2017.
 */
public interface RepositoryContract {
    List<DataObject> getDataObject(String urlWeb);
    Uri saveData(DataObject dataObject);
    int deleteData();
    Observable<DataObject> getDataObservable();
    Cursor getDataFromLocal();
    boolean checkAvailableData();
}

