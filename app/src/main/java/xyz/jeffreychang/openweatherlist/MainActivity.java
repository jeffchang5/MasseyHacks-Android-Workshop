package xyz.jeffreychang.openweatherlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import xyz.jeffreychang.openweatherlist.util.NetworkSingleton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkSingleton singleton = NetworkSingleton.getInstance(this);
        Log.d("NetworkSingleton", NetworkSingleton.urlBuilder("35", "139"));
    }
}
