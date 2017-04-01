package xyz.jeffreychang.openweatherlist.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by jeffreychang on 3/7/17.
 */


public class WeatherViewHolder extends RecyclerView.ViewHolder {
    final String TAG = "WeatherActivity";

    public WeatherViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = getAdapterPosition();
                Log.d(TAG, Integer.toString(position));
                // NEW ACTIVITY

            }
        });
    }
}
