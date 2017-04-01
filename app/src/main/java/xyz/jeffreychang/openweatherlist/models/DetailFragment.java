package xyz.jeffreychang.openweatherlist.models;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.jeffreychang.openweatherlist.R;
import xyz.jeffreychang.openweatherlist.util.NetworkSingleton;


public class DetailFragment extends Fragment {

    private final String TAG = "DetailFragment";
    private NetworkSingleton mNetworkSingleton;
    private double latitude;
    private double longitude;
    private static final String EXTRA_LATITUDE = "lat";
    private static final String EXTRA_LONGITUDE = "long";
    private HourlyWeather mCurrentHourlyWeather;

    public DetailFragment() {

    }
    public static DetailFragment getInstance(Date date, double latitude, double longitude) {

        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putDouble(EXTRA_LATITUDE, latitude);
        args.putDouble(EXTRA_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;

    }
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.latitude = savedInstanceState.getDouble(EXTRA_LATITUDE);
            this.longitude = savedInstanceState.getDouble(EXTRA_LONGITUDE);
        }
        mNetworkSingleton = NetworkSingleton.getInstance(getActivity());

    }

    public void getNetwork() {
        String fiveDayUrl = mNetworkSingleton.urlBuilder(NetworkSingleton.API.FIVE_DAY, latitude, longitude);
        final JsonObjectRequest hourlyWeatherRequest = new JsonObjectRequest
                (Request.Method.GET, fiveDayUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mCurrentHourlyWeather = createWeatherObject(response)[0];
                        Log.d(TAG, mCurrentHourlyWeather.toString());
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Couldn't get a response from server.");
                            }
                        });
        mNetworkSingleton.addToRequestQueue(hourlyWeatherRequest);
    }

    private HourlyWeather[] createWeatherObject(JSONObject response) {
        HourlyWeather[] hourlyWeatherArray = new HourlyWeather[5];
        try {
            String city = response.getJSONObject("city").getString("name");
            JSONArray weatherList = response.getJSONArray("list");

            Log.d(TAG, "5 Day Forecast for " + city);
            Log.d(TAG, "5 Day Forecast for " + response.toString());



            for (int i = 0; i < weatherList.length(); i++) {
                JSONObject jweather = weatherList.getJSONObject(i);
                JSONObject temp = jweather.getJSONObject("temp");
                String timestamp = SimpleDateFormat.getDateInstance().format(Double.valueOf(jweather.getString("dt")) * 1000);
                JSONObject descObj = jweather.getJSONArray("weather").getJSONObject(0);
                String description = descObj.getString("description");
                int min = Math.round(Float.valueOf(temp.getString("min")));
                int max = Math.round(Float.valueOf(temp.getString("max")));
                hourlyWeatherArray[i] = new HourlyWeather(timestamp, description, min, max);
            }
        }
        catch(JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return hourlyWeatherArray;
    }





}
