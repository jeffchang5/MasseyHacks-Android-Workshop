package xyz.jeffreychang.openweatherlist.util;
import android.content.Context;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class NetworkSingleton {
    private String TAG = "NetworkSingleton";
    private RequestQueue mRequestQueue;
    private NetworkSingleton mNetworkSingleton;
    private Context mContext;

    public NetworkSingleton (Context c) {
        this.mContext = c;
    }

    public synchronized NetworkSingleton getInstance(Context c) {
        if (mNetworkSingleton == null) {
            mNetworkSingleton = new NetworkSingleton(c);
        }
            return mNetworkSingleton;
    }
    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;

    }

    public String urlBuilder(double latitude, double longitude) {
        int days = 5;
        String api_key = "577b001467139c35c3e90b3d2dcd4456";
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily";
        return String.format("%s?lat=%s&lon=%s&cnt=%s&appid=%s", url, latitude, longitude, days, api_key);

    }
}
