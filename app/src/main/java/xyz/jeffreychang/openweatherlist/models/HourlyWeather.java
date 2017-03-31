package xyz.jeffreychang.openweatherlist.models;

import java.util.Date;

/**
 * Created by jeffreychang on 3/31/17.
 */

public class HourlyWeather {
    private final Date date;
    private final int minTemp;
    private final int maxTemp;
    private final int pressure;
    private final int humidity;

    public HourlyWeather(Date date, int minTemp, int maxTemp, int pressure, int humidity) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Date getDate() {
        return date;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }
}
