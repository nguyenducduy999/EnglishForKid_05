package com.framgia.englishforkid.videoview;

import android.content.Context;
import android.net.NetworkInfo;

/**
 * Created by duynguyenduc on 09/02/2017.
 */
public class ConnectivityManager {
    private static ConnectivityManager sConnectivityManager;
    private Context mContext;

    private ConnectivityManager(Context context) {
        mContext = context;
    }

    public static ConnectivityManager getInstance(Context context) {
        if (sConnectivityManager == null) sConnectivityManager = new ConnectivityManager(context);
        return sConnectivityManager;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isInternetConnect() {
        android.net.ConnectivityManager connectivityManager =
            (android.net.ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
            .getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
