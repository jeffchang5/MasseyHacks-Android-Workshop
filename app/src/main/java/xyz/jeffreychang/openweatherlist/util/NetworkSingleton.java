package xyz.jeffreychang.openweatherlist.util;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import xyz.jeffreychang.openweatherlist.WeatherFragment;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class NetworkSingleton {
    private String TAG = "NetworkSingleton";

    private static final int FORECAST_DAYS = 5;
    private static final String API_KEY = "577b001467139c35c3e90b3d2dcd4456";
    private static final String SIXTEEN_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
    private static final String FIVEDAY_URL = "api.openweathermap.org/data/2.5/forecast";

    public enum API {
        FIVE_DAY,
        SIXTEEN_DAY
    }


    private RequestQueue mRequestQueue;
    private static NetworkSingleton mNetworkSingleton = null;
    private static Context mContext;

    /**
     * private constructor
     *
     * @param c Context: application context
     */
    private NetworkSingleton(Context c) {
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


    public String urlBuilder(API api, double lat, double lon) {
        switch (api) {
            case FIVE_DAY:
                return String.format("%s?lat=%s&lon=%s&cnt=%s&appid=%s&units=metric",
                        FIVEDAY_URL, lat, lon, FORECAST_DAYS, API_KEY);

            case SIXTEEN_DAY:
                return String.format("%s?lat=%s&lon=%s&appid=%s&units=metric",
                        SIXTEEN_URL, lat, lon, API_KEY);
            default:
                return "NOT A VALID ENUM";
        }

    }
}
