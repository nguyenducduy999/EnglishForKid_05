package com.framgia.englishforkid.data.model;

import java.io.Serializable;

/**
 * Created by beepi on 19/01/2017.
 */
public class DataObject implements Serializable {
    private int mId;
    private String mTitle;
    private String mUrlImage;
    private String mUrlVideoPage;
    private String mUrlVideo;
    private int mType;

    public DataObject(int id, String title, String urlImage, String urlVideoPage, int type) {
        mTitle = title;
        mUrlImage = urlImage;
        mUrlVideoPage = urlVideoPage;
        mType = type;
        mId = id;
    }

    public DataObject(String title, String urlImage, String urlVideoPage, int type) {
        mTitle = title;
        mUrlImage = urlImage;
        mUrlVideoPage = urlVideoPage;
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrlImage() {
        return mUrlImage;
    }

    public void setUrlImage(String urlImage) {
        mUrlImage = urlImage;
    }

    public String getUrlVideoPage() {
        return mUrlVideoPage;
    }

    public void setUrlVideoPage(String urlVideoPage) {
        mUrlVideoPage = urlVideoPage;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUrlVideo() {
        return mUrlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        mUrlVideo = urlVideo;
    }
}
