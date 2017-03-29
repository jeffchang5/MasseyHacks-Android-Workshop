package xyz.jeffreychang.openweatherlist.models;

import java.net.URL;
import java.util.Date;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class Weather {
    private Date date;
    private String description;
    private int lowTemp;
    private int highTemp;

    public Weather(int date, String description, int lowTemp, int highTemp) {
        this.date = new Date(date * 1000);
        this.description = description;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    @Override
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
