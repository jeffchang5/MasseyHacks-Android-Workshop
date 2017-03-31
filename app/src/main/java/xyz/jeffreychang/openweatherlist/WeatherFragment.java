package xyz.jeffreychang.openweatherlist;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import xyz.jeffreychang.openweatherlist.models.Weather;
import xyz.jeffreychang.openweatherlist.recyclerview.WeatherAdapter;
import xyz.jeffreychang.openweatherlist.util.NetworkSingleton;

public class WeatherFragment extends Fragment {
    RecyclerView mRecyclerView;
    private final String TAG = "WeatherFragment";
    private final int REQUEST_CODE_LOCATION = 1;
    private final static String LAT = "lat";
    private final static String LON = "lon";
    private final static String URL = "url";

    private double latitude;
    private double longitude;
    private String url;

    private LocationManager locationManager;
    private Location location;

    private ActiveListener activeListener = new ActiveListener();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Get location permission


        // Get the location manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new WeatherAdapter());

        return v;

    }
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
    private void requestWeather() {
        if (url != null) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Weather[] weatherArray = createWeatherObject(response);
                            //setUI(weatherArray);
                            for(Weather weather: weatherArray) {
                                Log.d(TAG, weather.toString());// SET UP UI
                            }

                        }
                    },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "Couldn't get a response from server.");
                                }
                            });

            NetworkSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
        }
    }
    public void startLocationUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            try {
                locationManager.requestLocationUpdates(provider, 500, 1, activeListener);
            }
            catch (SecurityException e) {
                Log.d(TAG, e.getMessage());
            }


        }

    }
    private void unregisterListeners() {

        try {
            locationManager.removeUpdates(activeListener);
        }
        catch (Exception e){
            Log.d(TAG, "There could no be updates for Location Manager");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            startLocationUpdates();
        }

    }
    private Weather[] createWeatherObject(JSONObject response) {
        Weather [] weatherArray = new Weather[5];
        try {
            String city = response.getJSONObject("city").getString("name");
            JSONArray weatherList = response.getJSONArray("list");
            //Weather weather = new Weather(city);
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
                weatherArray[i] = new Weather(timestamp, description, min, max);
            }
        }
        catch(JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return weatherArray;
    }

    private void updateLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        url = NetworkSingleton.getInstance(getActivity()).urlBuilder(latitude, longitude);

        // DEBUG
        Log.d(TAG, location.getProvider());
        Log.d(TAG, String.valueOf(location.getAccuracy()));
        Log.d(TAG, String.valueOf(latitude));
        Log.d(TAG, String.valueOf(longitude));
        Log.d(TAG, url);

        // Save into preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(LAT, String.valueOf(latitude));
        edit.putString(LON, String.valueOf(longitude));
        edit.putString(URL, url);
        edit.apply();

        requestWeather();
    }

    public void getLocation() {



        // Create a Criteria object
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // Criteria is a class that provides the parameters in the best strategy to get a location.


        if (gpsStatus || networkStatus) {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // This is where the if block starts
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);

                startLocationUpdates();



            }

        }
        else {
            Log.d(TAG, "Provider is null");
        }

    }
    public AlertDialog buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.help_message)
                .setTitle(R.string.help_title)
                .setPositiveButton(R.string.ok, null);
        return builder.create();
    }

    @Override
    public void onPause() {
        unregisterListeners();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (url == null) {
//            getLocation();
//        } else {
//            requestWeather();
//        }
    }


    public void setUI() {

    }


    public void d(String s) {
        Log.d(TAG, s);
    }
}


