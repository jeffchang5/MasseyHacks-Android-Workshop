package xyz.jeffreychang.openweatherlist.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.jeffreychang.openweatherlist.R;
import xyz.jeffreychang.openweatherlist.models.DailyWeather;
import xyz.jeffreychang.openweatherlist.models.HourlyWeather;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter {
    private final String TAG = "WeatherAdapter";
    private ArrayList <DailyWeather> weatherList;



    public WeatherAdapter(ArrayList <DailyWeather> weatherList) {
        this.weatherList = weatherList;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater instantiates a new View object from a XML file

            View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_weather, parent, false);
            return new WeatherViewHolder(itemLayout);

    }







    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (weatherList == null) {

        }
        else {
            Log.d(TAG, weatherList.get(position).getDate().toString());
        }


    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "HI");
        if (weatherList == null) {
            return 0;
        }
        return weatherList.size();

    }

}
