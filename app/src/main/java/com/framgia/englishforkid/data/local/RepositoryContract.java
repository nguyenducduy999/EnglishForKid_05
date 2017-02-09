package com.framgia.englishforkid.data.local;

import android.content.ContentValues;
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
    List<DataObject> getSearchingData(String querry, int type);
    List<DataObject> convertCursorToList(Cursor cursor, int type);
    boolean checkAvailableData();
    List<DataObject> getRandomData(int types, int videoId);
    String getIdVideo(DataObject video);
    Observable<String> getIdVideoObservable(DataObject data);
    int updateUrlVideo(DataObject dataObject);
    ContentValues convertObject(DataObject video);
    String getUrlVideoLocal(DataObject video);
    boolean checkExistFile(String directory);
}
