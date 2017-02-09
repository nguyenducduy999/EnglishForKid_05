package com.framgia.englishforkid.util;
/**
 * Created by beepi on 17/01/2017.
 */
import android.os.Environment;

/**
 * Usage: determine values which are constant in project
 */
public class Constant {
    public final static String URL_BASE = "https://learnenglishkids.britishcouncil.org";
    public final static int COUNT_RANDOM_VIDEO = 10;
    public final static String URL_VIDEO_BASE = "http://www.viddler.com/file/%s/html5";
    public final static String DOWNLOAD_DIRECTORY = Environment.getExternalStoragePublicDirectory
        (Environment.DIRECTORY_DOWNLOADS).getPath().concat("/");
}

