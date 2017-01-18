package com.framgia.englishforkid.data.model;

/**
 * Created by duynguyenduc on 18/01/2017.
 */
public class VideoModel {
    private String mTitle;
    private String mIconUrl;

    public VideoModel(String iconUrl, String title) {
        mIconUrl = iconUrl;
        mTitle = title;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public String getSongTitle() {
        return mTitle;
    }

    public void setSongTitle(String songTitle) {
        this.mTitle = songTitle;
    }
}
