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

    private static final int FORECAST_DAYS = 5;
    private static final String API_KEY = "577b001467139c35c3e90b3d2dcd4456";
    private static final String URL = "http://api.openweathermap.org/data/2.5/forecast/daily";

    private RequestQueue mRequestQueue;
    private static NetworkSingleton mNetworkSingleton = null;
    private static Context mContext;

    /**
     * private constructor
     * @param c Context: application context
     */
    private NetworkSingleton (Context c) {
        mContext = c;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkSingleton getInstance(Context c) {
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

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public String urlBuilder(double lat, double lon) {
        return String.format("%s?lat=%s&lon=%s&cnt=%s&appid=%s",
                URL, lat, lon, FORECAST_DAYS, API_KEY);
    }
}
