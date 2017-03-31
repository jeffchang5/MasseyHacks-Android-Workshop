package xyz.jeffreychang.openweatherlist.models;

import java.net.URL;
import java.util.Date;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class Weather {
    private String date;
    private String description;
    private int lowTemp;
    private int highTemp;

    public Weather(String date, String description, int lowTemp, int highTemp) {
        this.date = date;
        this.description = description;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public String toString() {
        return "Weather{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", lowTemp=" + lowTemp +
                ", highTemp=" + highTemp +
                '}';
    }

    public int getHighTemp() {
        return highTemp;
    }
}
