package xyz.jeffreychang.openweatherlist;

import android.content.Context;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import xyz.jeffreychang.openweatherlist.util.NetworkSingleton;

public class MainActivity extends AppCompatActivity {
    final String TAG = "WeatherActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        NetworkSingleton singleton = NetworkSingleton.getInstance(MainActivity.this);
        Log.d("NetworkSingleton", NetworkSingleton.urlBuilder("35", "139"));
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
                Log.d(TAG, "Location selected");

                break;
            case R.id.help:
                Log.d(TAG, "Help selected");
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

        LocationManager locationManager =  (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);


    }
}
