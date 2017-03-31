package xyz.jeffreychang.openweatherlist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jeffreychang.openweatherlist.R;
import xyz.jeffreychang.openweatherlist.recyclerview.WeatherAdapter;

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
        // Inflate the layout for this fragment

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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            startLocationUpdates();
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
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            try {
                locationManager.removeUpdates(activeListener);
            }
            catch (Exception e){
                return;
            }
        }
    }
    public void d(String s) {
        Log.d(TAG, s);
    }
}


