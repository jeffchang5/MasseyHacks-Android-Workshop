package xyz.jeffreychang.openweatherlist.util;
import android.content.Context;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class NetworkSingleton {
    private String TAG = "NetworkSingleton";
    private RequestQueue mRequestQueue;
    private static NetworkSingleton mNetworkSingleton;
    private Context mContext;

    private NetworkSingleton (Context c) {
        mRequestQueue = Volley.newRequestQueue(c);




    }
    public static synchronized NetworkSingleton getInstance(Context c) {
        if (mNetworkSingleton == null) {
            mNetworkSingleton = new NetworkSingleton(c);
        }
            return mNetworkSingleton;


    }
    public static String urlBuilder(String latitude, String longitude) {
        String api_key = "577b001467139c35c3e90b3d2dcd4456";
        String url = "http://samples.openweathermap.org/data/2.5/forecast";
        return String.format("%s?lat=%s&lon=%s&appid=%s", url, "35", "139", api_key);


    }
}
