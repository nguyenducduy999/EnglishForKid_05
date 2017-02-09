package com.framgia.englishforkid.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.framgia.englishforkid.data.model.DataObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

import static com.framgia.englishforkid.data.local.DataHelper.FIELD_ID;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_TITLE;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_TYPE;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_URL_IMG;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_URL_PAGE_VIDEO;
import static com.framgia.englishforkid.data.local.DataHelper.FIELD_URL_VIDEO;
import static com.framgia.englishforkid.data.local.DataHelper.TYPE_SONG;
import static com.framgia.englishforkid.data.local.DataHelper.TYPE_STORY;
import static com.framgia.englishforkid.data.local.EnglishForKidProvider.CONTENT_URI;
import static com.framgia.englishforkid.util.Constant.COUNT_RANDOM_VIDEO;
import static com.framgia.englishforkid.util.Constant.URL_BASE;

/**
 * Created by beepi on 20/01/2017.
 */
public class DataRepository implements RepositoryContract {
    public final static String URL_SONGS = URL_BASE + "/en/songs";
    public final static String URL_STORIES = URL_BASE + "/en/short-stories";
    public final static String ELEMENT_CLASS = "div.field-content";
    public final static String ELEMENT_VIDEO_PAGE_TAG = "a";
    public final static String ELEMENT_IMAGE_TAG = "img";
    public final static String ATTR_LINK_VIDEO_PAGE = "href";
    public final static String ELEMENT_VIDEO_TAG = ".viddler-auto-embed";
    public final static String ATTR_VIDEO_ID = "data-video-id";
    public final static String ATTR_LINK_IMAGE = "src";
    public final static String ATTR_TITLE = "title";
    public final static String START_TITLE_SONG = "Song:";
    public final static String START_TITLE_SONGS = "Songs:";
    public final static String START_TITLE_STORIY = "Short stories:";
    public final static String START_TITLE_STORIES = "Story:";
    private Context mContext;
    private ContentResolver mContentResolver;

    public DataRepository(Context context) {
        this.mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

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
            Elements videoElements = classElements.select(ELEMENT_VIDEO_PAGE_TAG);
            if (videoElements == null) return null;
            dataObjList = new ArrayList();
            for (Element element : videoElements) {
                urlVideo = element.attr(ATTR_LINK_VIDEO_PAGE);
                Elements imageElement = element.select(ELEMENT_IMAGE_TAG);
                urlImge = imageElement.attr(ATTR_LINK_IMAGE);
                title = imageElement.attr(ATTR_TITLE);
                if (title == null || title.equals("")) continue;
                switch (urlWeb) {
                    case URL_SONGS:
                        if (title.startsWith(START_TITLE_SONG)) {
                            title = title.substring(START_TITLE_SONG.length(), title.length());
                        } else {
                            title = title.substring(START_TITLE_SONGS.length(), title.length());
                        }
                        dataObject = new DataObject(title, urlImge, urlVideo, TYPE_SONG);
                        break;
                    case URL_STORIES:
                        if (title.startsWith(START_TITLE_STORIY)) {
                            title = title.substring(START_TITLE_STORIY.length(), title.length());
                        } else {
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
        } catch (IOException e) {
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
        values.put(FIELD_URL_PAGE_VIDEO, dataObject.getUrlVideoPage());
        values.put(FIELD_TYPE, dataObject.getType());
        return mContentResolver.insert(CONTENT_URI, values);
    }

    @Override
    public int deleteData() {
        return mContentResolver.delete(CONTENT_URI, null, null);
    }

    public List<DataObject> getDataFromProvider(int types) {
        List<DataObject> listData = null;
        String title, urlVideo, urlImg, id;
        Cursor cursor = mContext.getContentResolver()
            .query(EnglishForKidProvider
                .CONTENT_URI, null, FIELD_TYPE + "=" + types, null, null);
        return convertCursorToList(cursor, types);
    }

    @Override
    public Cursor getDataFromLocal() {
        return mContentResolver.query(CONTENT_URI, null, null, null, null);
    }

    /**
     * get cursor which belongs suggest letter from local data
     *
     * @param querry: key text to request in {@link EnglishForKidProvider}
     * @param type:   type of fragment in viewpager
     * @return list of data
     */
    @Override
    public List<DataObject> getSearchingData(String querry, int type) {
        StringBuilder selection = new StringBuilder()
            .append(FIELD_TYPE)
            .append("=")
            .append(type)
            .append(" and ")
            .append(FIELD_TITLE)
            .append(" like ")
            .append("'%")
            .append(querry)
            .append("%'");
        Cursor cursor = mContentResolver.query(CONTENT_URI, null, selection.toString(),
            null,
            null);
        return convertCursorToList(cursor, type);
    }

    @Override
    public List<DataObject> convertCursorToList(Cursor cursor, int type) {
        if (cursor == null) return null;
        List<DataObject> lists = new ArrayList();
        String title, urlVideo, urlImg;
        int id;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
                title = cursor.getString(cursor.getColumnIndex(FIELD_TITLE));
                urlVideo = cursor.getString(cursor.getColumnIndex(FIELD_URL_PAGE_VIDEO));
                urlImg = cursor.getString(cursor.getColumnIndex(FIELD_URL_IMG));
                lists.add(new DataObject(id, title, urlImg, urlVideo, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lists;
    }

    /**
     * check data is available in local or not
     *
     * @return the number of rows found in local data
     */
    @Override
    public boolean checkAvailableData() {
        Cursor cursor = getDataFromLocal();
        if (cursor == null || cursor.getCount() == 0) return false;
        return true;
    }

    @Override
    public List<DataObject> getRandomData(int types, int videoId) {
        List<DataObject> listData = null;
        String title, urlVideo, urlImg;
        int id;
        Cursor cursor = mContext.getContentResolver()
            .query(EnglishForKidProvider.CONTENT_URI,
                null,
                FIELD_TYPE + "=" + types + " and " + FIELD_ID + "!=" + videoId,
                null,
                "RANDOM() LIMIT " + COUNT_RANDOM_VIDEO);
        return convertCursorToList(cursor, types);
    }

    /**
     * insert urlVideo into object
     *
     * @param video: object need updating
     * @return object updated
     */
    @Override
    public String getIdVideo(DataObject video) {
        if (video == null) return null;
        Document document = null;
        try {
            document =
                Jsoup.connect(URL_BASE.concat(video.getUrlVideoPage())).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return document.select(ELEMENT_VIDEO_TAG).first().attr(ATTR_VIDEO_ID);
    }

    @Override
    public Observable<String> getIdVideoObservable(final DataObject data) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(getIdVideo(data));
            }
        });
    }

    @Override
    public int updateUrlVideo(DataObject video) {
        if (video == null) return 0;
        Uri uriUpdate = CONTENT_URI.buildUpon().appendEncodedPath(
            String.valueOf(video.getId())).build();
        return mContentResolver.update(uriUpdate, convertObject(video), FIELD_TITLE + "=?", new
            String[]{video.getTitle()});
    }

    @Override
    public ContentValues convertObject(DataObject dataObject) {
        ContentValues contentValues = new ContentValues();
        if (dataObject == null) return null;
        contentValues.put(FIELD_TITLE, dataObject.getTitle());
        contentValues.put(FIELD_TYPE, dataObject.getType());
        contentValues.put(FIELD_URL_IMG, dataObject.getUrlImage());
        contentValues.put(FIELD_URL_PAGE_VIDEO, dataObject.getUrlVideoPage());
        contentValues.put(FIELD_URL_VIDEO, dataObject.getUrlVideo());
        contentValues.put(FIELD_ID, dataObject.getId());
        return contentValues;
    }

    @Override
    public String getUrlVideoLocal(DataObject video) {
        Uri uriRequest = CONTENT_URI.buildUpon()
            .appendEncodedPath(String.valueOf(video.getId()))
            .build();
        Cursor cursor = mContentResolver.query(uriRequest, null, FIELD_TITLE + "=?", new
                String[]{video.getTitle()},
            null);
        String url = null;
        if (cursor != null && cursor.moveToFirst()) {
            url = cursor.getString(cursor.getColumnIndex(FIELD_URL_VIDEO));
        }
        return url;
    }

    @Override
    public boolean checkExistFile(String fileName) {
        if (fileName != null) {
            File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName);
            return file.isFile();
        }
        return false;
    }
}
