package xyz.jeffreychang.openweatherlist.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.jeffreychang.openweatherlist.R;
import xyz.jeffreychang.openweatherlist.recyclerview.WeatherViewHolder;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LayoutInflater instantiates a new View object from a XML file

        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        Log.d("TAG", itemLayout.toString());

        return new WeatherViewHolder(itemLayout);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
