package xyz.jeffreychang.openweatherlist;

import android.Manifest;
import android.content.Context;

import xyz.jeffreychang.openweatherlist.models.DailyWeather;
import xyz.jeffreychang.openweatherlist.util.NetworkSingleton.API;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;


import xyz.jeffreychang.openweatherlist.recyclerview.WeatherAdapter;
import xyz.jeffreychang.openweatherlist.util.NetworkSingleton;

public class WeatherFragment extends Fragment {
    // member variables
    RecyclerView mRecyclerView;
    private LocationManager mLocationManager;
    private ActiveListener activeListener = new ActiveListener();
    private ArrayList<DailyWeather> mDailyWeatherList;
    private NetworkSingleton mNetworkSingleton;

    // some constants
    private final String TAG = "WeatherFragment";
    private final int REQUEST_CODE_LOCATION = 1;

    public static final int CURRENT_WEATHER = 0;
    public static final int WEATHER_FORECAST = 1;

    private WeatherAdapter mAdapter;
    private double latitude;
    private double longitude;
    boolean valid = false;



    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mNetworkSingleton = NetworkSingleton.getInstance(getActivity());
        requestWeather();
    }


    /** onCreateView
     * This function is ran when the view is first created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.hasFixedSize();
        mAdapter = new WeatherAdapter(mDailyWeatherList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    /** onResume
     * This function is called whenever the application/fragment resumes
     * from any interruption. It is also called right after onCreate()
     */
    @Override
    public void onResume() {
        super.onResume();
        if (valid){
            requestWeather();
        } else {
            getLocation();
        }
    }

    /** onPause
     * This function is called when there is any interruption
     * (e.g. home button pressed, alert dialog, notification bar is pulled down, etc.)
     */
    @Override
    public void onPause() {
        unregisterListeners();
        super.onPause();
    }


    /**
     * Handles getting location listener started
     */
    public void getLocation() {
        boolean gpsStatus = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkStatus = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (gpsStatus || networkStatus) {
            // criteria for provider
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(false);

            // Location permission handling
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // request location permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                // get best provider based on criteria
                String provider = mLocationManager.getBestProvider(criteria, true);
                // start location listener
                mLocationManager.requestLocationUpdates(provider, 500, 1, activeListener);
                mLocationManager.getLastKnownLocation(provider);
            }
        } else {
            Toast.makeText(getActivity(), "Location is not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles permission request callback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            Toast.makeText(getActivity(), "Location permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function handles when location is acquired
     */
    private void updateLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        valid = true;
        // DEBUG
        Log.d(TAG, location.getProvider());
        Log.d(TAG, String.valueOf(location.getAccuracy()));
        Log.d(TAG, String.valueOf(latitude));
        Log.d(TAG, String.valueOf(longitude));

        requestWeather();
    }

    /**
     * This function handles API call to request weather data
     */
    private void requestWeather() {

        String sixteenDayUrl =  mNetworkSingleton.urlBuilder(API.SIXTEEN_DAY, latitude, longitude);



        JsonObjectRequest forecastRequest = new JsonObjectRequest
                (Request.Method.GET, sixteenDayUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mDailyWeatherList = mNetworkSingleton.createDailyWeatherObject(response);



                        Log.d(TAG, "5 day Forecast");
                        mAdapter.notifyDataSetChanged();
//                        for(DailyWeather weather: mDailyWeatherArray) {
//                            Log.d(TAG, weather.toString());// SET UP UI
//                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Couldn't get a response from server.");
                            }
                        });

        mNetworkSingleton.addToRequestQueue(forecastRequest);

    }

    /** TODO: FIX this function to handle current weather api call response
     * weather object
     * @param response
     * @return
     */



    /**
     * builds the pop up dialog for help
     */
    public AlertDialog buildHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.help_message)
                .setTitle(R.string.help_title)
                .setPositiveButton(R.string.ok, null);
        return builder.create();
    }

    /**
     * Unregisters the location listener
     */
    private void unregisterListeners() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            try {
                mLocationManager.removeUpdates(activeListener);
            }
            catch (Exception e){
                Log.d(TAG, e.getLocalizedMessage());
            }
        }
    }

    /**
     * class to support location listener
     */
    private class ActiveListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null && location.getAccuracy() < 500) {
                updateLocation(location);
                unregisterListeners();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public void d(String s) {
        Log.d(TAG, s);
    }

}


