package xyz.jeffreychang.openweatherlist.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by han on 4/1/17.
 */

public class CurrentWeather {

    private static final String TAG = "CurrentWeather";

    private String description;
    private String city;
    private float temp;

    public CurrentWeather(JSONObject response) {
        try {
            this.city = response.getString("name");
            this.description = response.getJSONObject("weather").getString("description");
            this.temp = Float.valueOf(response.getJSONObject("main").getString("temp"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public float getTemp() {
        return temp;
    }
}
