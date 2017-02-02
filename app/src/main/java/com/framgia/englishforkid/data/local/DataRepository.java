package com.framgia.englishforkid.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.framgia.englishforkid.data.model.DataObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

import static com.framgia.englishforkid.data.local.DataHelper.FIELD_ID;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_TITLE;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_TYPE;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_URL_IMG;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_URL_VIDEO;
import static com.framgia.englishforkid.data.local.DataHelper.TYPE_SONG;
import static com.framgia.englishforkid.data.local.DataHelper.TYPE_STORY;
import static com.framgia.englishforkid.data.local.EnglishForKidProvider.CONTENT_URI;
import static com.framgia.englishforkid.util.Constant.URL_BASE;

/**
 * Created by beepi on 20/01/2017.
 */
public class DataRepository implements RepositoryContract {
    public final static String TAG = "Jsoup";
    public final static String URL_SONGS = URL_BASE + "songs";
    public final static String URL_STORIES = URL_BASE + "short-stories";
    public final static String ELEMENT_CLASS = "div.field-content";
    public final static String ELEMENT_VIDEO_TAG = "a";
    public final static String ELEMENT_IMAGE_TAG = "img";
    public final static String ATTR_LINK_VIDEO = "href";
    public final static String ATTR_LINK_IMAGE = "src";
    public final static String ATTR_TITLE = "title";
    public final static String START_TITLE_SONG = "Song:";
    public final static String START_TITLE_STORIES = "Short stories:";
    private static Context mContext;
    private ContentResolver mContentResolver;

    public DataRepository(Context context) {
        this.mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    /**
     * get data oject from {@link com.framgia.englishforkid.util.Constant#URL_SONGS}
     * or {@link com.framgia.englishforkid.util.Constant}
     *
     * @param urlWeb: url of web
     * @return list of {@link DataObject}
     */
    @Override
    public List<DataObject> getDataObject(String urlWeb) {
        List dataObjList;
        DataObject dataObject;
        String title, urlImge, urlVideo;
        Document document;
        try {
            document = Jsoup.connect(urlWeb).get();
            Elements classElements = document.select(ELEMENT_CLASS);
            if (classElements == null) return null;
            Elements videoElements = classElements.select(ELEMENT_VIDEO_TAG);
            if (videoElements == null) return null;
            dataObjList = new ArrayList();
            for (Element element : videoElements) {
                urlVideo = element.attr(ATTR_LINK_VIDEO);
                Elements imageElement = element.select(ELEMENT_IMAGE_TAG);
                urlImge = imageElement.attr(ATTR_LINK_IMAGE);
                title = imageElement.attr(ATTR_TITLE);
                if (title == null || title.equals("")) continue;
                switch (urlWeb) {
                    case URL_SONGS:
                        if (title.startsWith(START_TITLE_SONG)) {
                            title = title.substring(START_TITLE_SONG.length(), title.length());
                        }
                        dataObject = new DataObject(title, urlImge, urlVideo, TYPE_SONG);
                        break;
                    case URL_STORIES:
                        if (title.startsWith(START_TITLE_STORIES)) {
                            title = title.substring(START_TITLE_STORIES.length(), title.length());
                        }
                        dataObject = new DataObject(title, urlImge, urlVideo, TYPE_STORY);
                        break;
                    default:
                        dataObject = null;
                        break;
                }
                if (dataObject != null) dataObjList.add(dataObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dataObjList;
    }

    @Override
    public Observable<DataObject> getDataObservable() {
        return Observable.defer(new Func0<Observable<DataObject>>() {
            @Override
            public Observable<DataObject> call() {
                Observable<DataObject> obserSongs = Observable.from(getDataObject(URL_SONGS));
                Observable<DataObject> obserStories = Observable.from(getDataObject(URL_STORIES));
                return Observable.concat(obserSongs, obserStories);
            }
        });
    }

    @Override
    public Uri saveData(DataObject dataObject) {
        ContentValues values = new ContentValues();
        values.put(FIELD_TITLE, dataObject.getTitle());
        values.put(FIELD_URL_IMG, dataObject.getUrlImage());
        values.put(FIELD_URL_VIDEO, dataObject.getUrlVideo());
        values.put(FIELD_TYPE, dataObject.getType());
        return mContentResolver.insert(CONTENT_URI, values);
    }

    @Override
    public int deleteData() {
        return mContentResolver.delete(CONTENT_URI, null, null);
    }

    public static List<DataObject> getDataFromProvider(int types) {
        List<DataObject> listData = null;
        String title, urlVideo, urlImg;
        Cursor cursor = mContext.getContentResolver()
            .query(EnglishForKidProvider
                .CONTENT_URI, null, FIELD_TYPE + "=" + types, null, null);
        if (cursor == null) return null;
        if (cursor.moveToFirst()) {
            //....
            listData = new ArrayList<>();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
                title = cursor.getString(cursor.getColumnIndex(FIELD_TITLE));
                urlVideo = cursor.getString(cursor.getColumnIndex(FIELD_URL_VIDEO));
                urlImg = cursor.getString(cursor.getColumnIndex(FIELD_URL_IMG));
                listData.add(new DataObject(title, urlImg, urlVideo, types));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listData;
    }
}

