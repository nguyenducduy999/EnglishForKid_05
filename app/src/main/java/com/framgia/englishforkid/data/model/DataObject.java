package com.framgia.englishforkid.data.model;

/**
 * Created by beepi on 19/01/2017.
 */
public class DataObject {
    private String mTitle;
    private String mUrlImage;
    private String mUrlVideo;
    private int mType;

    public DataObject(String title, String urlImage, String urlVideo, int type) {
        mTitle = title;
        mUrlImage = urlImage;
        mUrlVideo = urlVideo;
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

    public String getUrlVideo() {
        return mUrlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        mUrlVideo = urlVideo;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
