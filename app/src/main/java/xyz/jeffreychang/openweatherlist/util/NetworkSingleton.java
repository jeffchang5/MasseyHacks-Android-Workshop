package xyz.jeffreychang.openweatherlist.util;
import android.content.Context;

import com.android.volley.*;
/**
 * Created by jeffreychang on 3/7/17.
 */

public class NetworkSingleton {
    private String TAG = "NetworkSingleton";
    RequestQueue mRequestQueue;
    private static NetworkSingleton mNetworkSingleton;
    private Context mContext;

    private NetworkSingleton (Context c) {





    }
    public static synchronized void getInstance(Context c) {
        if (mNetworkSingleton == null) {
            mNetworkSingleton = new NetworkSingleton(c);
        }
        else {
            return
        }

    }

}
