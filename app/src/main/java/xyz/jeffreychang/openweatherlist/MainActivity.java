package xyz.jeffreychang.openweatherlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "WeatherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        //transaction.replace(R.id.fragmentFrame, DetailFragment.getInstance(new Date(), 42.4806, 83.4755, 1));
        transaction.replace(R.id.fragmentFrame, new WeatherFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }


}