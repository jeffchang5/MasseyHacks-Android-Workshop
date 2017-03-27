package xyz.jeffreychang.openweatherlist;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import xyz.jeffreychang.openweatherlist.util.NetworkSingleton;

public class MainActivity extends AppCompatActivity {

    final String TAG = "WeatherActivity";
    private final int REQUEST_LOCATION = 1;
    private final static String LAT = "lat";
    private final static String LON = "lon";
    private final static String URL = "url";

    private SharedPreferences pref = null;

    double latitude = -1;
    double longitude = -1;
    String url = null;

    LocationManager locationManager = null;
    private ActiveListener activeListener = new ActiveListener();
    private NetworkSingleton singleton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        singleton = new NetworkSingleton(getApplicationContext());

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        latitude = Double.parseDouble(pref.getString(LAT, "-1"));
        longitude = Double.parseDouble(pref.getString(LON, "-1"));
        url = pref.getString(URL ,null);
    }

    @Override
    protected void onPause() {
        unregisterListeners();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.location:
                Toast.makeText(this, "Location selected", Toast.LENGTH_SHORT).show();
                getLocation();
                break;
            case R.id.help:
                Log.d(TAG, "Help selected");
                Toast.makeText(this, "Help selected", Toast.LENGTH_SHORT).show();
                buildAlertDialog().show();
                break;
        }

        return true;
    }

    public AlertDialog buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.help_message)
                .setTitle(R.string.help_title)
                .setPositiveButton(R.string.ok, null);
        return builder.create();
    }

    public void getLocation() {
        locationManager =  (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);

        if (provider != null) {
            // Get location permission
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
            locationManager.requestLocationUpdates(provider, 500, 1, activeListener);
            Location location = locationManager.getLastKnownLocation(provider);
        }

    }

    /**
     * Update UI to display new location
     */
    private void requestWeather() {
        if (url != null) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // SET UP UI
                            Log.d("JSON RESPONSE", response.toString());
                            setUI(response);

                        }
                    },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                }
                            });

            singleton.getRequestQueue().add(jsObjRequest);
        }
    }

    private void savePreferences(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        url = singleton.urlBuilder(latitude, longitude);

        // DEBUG
        Log.d("LOCATION",location.getProvider());
        Log.d("LOCATION",String.valueOf(location.getAccuracy()));
        Log.d("LOCATION",String.valueOf(latitude));
        Log.d("LOCATION",String.valueOf(longitude));
        Log.d("NetworkSingleton", url);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(LAT, String.valueOf(latitude));
        edit.putString(LON, String.valueOf(longitude));
        edit.putString(URL, url);
        edit.apply();

        requestWeather();
    }

    private void setUI(JSONObject response) {
        if (response != null) {
            //TODO: set up UI
            return;
        }
    }


    /**
     * Unregisters the location listener
     */
    private void unregisterListeners() {
        if (ActivityCompat.checkSelfPermission(this,
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


    private class ActiveListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null && location.getAccuracy() < 500) {
                savePreferences(location);
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
}
