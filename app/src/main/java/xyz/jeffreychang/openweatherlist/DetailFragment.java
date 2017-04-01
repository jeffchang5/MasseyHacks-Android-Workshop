package xyz.jeffreychang.openweatherlist;

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

import org.json.JSONObject;

import java.util.Date;

import xyz.jeffreychang.openweatherlist.models.HourlyWeather;
import xyz.jeffreychang.openweatherlist.util.NetworkSingleton;


public class DetailFragment extends Fragment {

    private final String TAG = "DetailFragment";
    private NetworkSingleton mNetworkSingleton;
    private static final String EXTRA_LATITUDE = "lat";
    private static final String EXTRA_LONGITUDE = "long";
    private HourlyWeather[] mHourlyWeatherArray;

    private double latitude;
    private double longitude;

    public DetailFragment() {

    }
    public static DetailFragment getInstance(Date date, double latitude, double longitude, int position) {

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
        Bundle args = getArguments();
        if (args != null) {
            latitude = args.getDouble(EXTRA_LATITUDE);
            longitude = args.getDouble(EXTRA_LONGITUDE);
        }
        mNetworkSingleton = NetworkSingleton.getInstance(getActivity());
        requestWeather();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    public void requestWeather() {
        String fiveDayUrl = mNetworkSingleton.urlBuilder(NetworkSingleton.API.FIVE_DAY, latitude, longitude);
        final JsonObjectRequest hourlyWeatherRequest = new JsonObjectRequest
                (Request.Method.GET, fiveDayUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mHourlyWeatherArray = mNetworkSingleton.createHourlyWeatherArray(response);

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


    public void d(String s) {
        Log.d(TAG, s);
    }





}
