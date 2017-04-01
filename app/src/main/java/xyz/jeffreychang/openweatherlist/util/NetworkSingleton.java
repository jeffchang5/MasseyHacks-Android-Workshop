package xyz.jeffreychang.openweatherlist.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.jeffreychang.openweatherlist.WeatherFragment;
import xyz.jeffreychang.openweatherlist.models.DailyWeather;
import xyz.jeffreychang.openweatherlist.models.HourlyWeather;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class NetworkSingleton {
    private String TAG = "NetworkSingleton";

    private static final int FORECAST_DAYS = 5;
    private static final String API_KEY = "577b001467139c35c3e90b3d2dcd4456";
    private static final String SIXTEEN_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
    private static final String FIVEDAY_URL = "http://api.openweathermap.org/data/2.5/forecast";

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

    private RequestQueue getRequestQueue() {

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
    public DailyWeather[] createDailyWeatherObject(JSONObject response) {

        try {
            String city = response.getJSONObject("city").getString("name");
            JSONArray weatherList = response.getJSONArray("list");

            DailyWeather[] dailyWeatherArray = new DailyWeather[weatherList.length()];


            for (int i = 0; i < weatherList.length(); i++) {
                JSONObject weatherObject = weatherList.getJSONObject(i);
                JSONObject temp = weatherObject.getJSONObject("temp");
                JSONObject descObj = weatherObject.getJSONArray("weather").getJSONObject(0);

                dailyWeatherArray[i] =

                        new DailyWeather(
                                descObj.getString("description"),
                                SimpleDateFormat.getDateInstance().format(Double.valueOf(weatherObject.getString("dt")) * 1000),
                                Math.round(Float.valueOf(temp.getString("min"))),
                                Math.round(Float.valueOf(temp.getString("max")))
                        );
            }
            return dailyWeatherArray;
        }
        catch(JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;

    }

    public HourlyWeather[] createHourlyWeatherArray(JSONObject response) {

        try {
            String city = response.getJSONObject("city").getString("name");
            JSONArray weatherList = response.getJSONArray("list");
            HourlyWeather[] hourlyWeatherArray = new HourlyWeather[weatherList.length()];


            for (int i = 0; i < weatherList.length(); i++) {
                JSONObject weatherObject = weatherList.getJSONObject(i);
                JSONObject main = weatherObject.getJSONObject("main");

                hourlyWeatherArray[i] = new HourlyWeather (
                        new Date(Math.round(Double.valueOf(weatherObject.getString("dt")) * 1000)),
                        main.getInt("temp_min"),
                        main.getInt("temp_max"),
                        main.getInt("pressure"),
                        main.getInt("humidity")
                );
            }
            return hourlyWeatherArray;
        }
        catch(JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;

    }
}
