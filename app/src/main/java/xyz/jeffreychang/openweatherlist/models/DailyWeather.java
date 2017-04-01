package xyz.jeffreychang.openweatherlist.models;

import java.net.URL;
import java.util.Date;

/**
 * Created by jeffreychang on 3/7/17.
 */

public class DailyWeather {
    private String date;
    private String description;
    private int lowTemp;
    private int highTemp;
    private String icon;

    public DailyWeather(String date, String description, int lowTemp, int highTemp, String icon) {
        this.date = date;
        this.description = description;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.icon = icon;
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

    public String getIcon(){ return icon;}

    public String toString() {
        return "DailyWeather{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", lowTemp=" + lowTemp +
                ", highTemp=" + highTemp +
                ", highTemp=" + icon +
                '}';
    }

    public int getHighTemp() {
        return highTemp;
    }
}
