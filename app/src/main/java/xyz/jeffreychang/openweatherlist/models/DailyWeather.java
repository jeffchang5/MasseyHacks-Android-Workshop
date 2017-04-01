package xyz.jeffreychang.openweatherlist.models;

import java.net.URL;
import java.util.Date;
import xyz.jeffreychang.openweatherlist.R;

public class DailyWeather {
    private String date;
    private String description;
    private int lowTemp;
    private int highTemp;
    private int icon;

    public DailyWeather(String date, String description, int lowTemp, int highTemp, String icon) {
        this.date = date;
        this.description = description;
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
        this.icon = getResouceIcon(icon);
    }

    public String getDate() { return date; }

    public String getDescription() { return description; }

    public int getLowTemp() { return lowTemp; }

    public int getIcon(){ return icon;}

    public int getHighTemp() { return highTemp; }

    public String toString() {
        return "DailyWeather{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", lowTemp=" + lowTemp +
                ", highTemp=" + highTemp +
                ", highTemp=" + icon +
                '}';
    }

    private int getResouceIcon(String value){
        switch(value){
            case("01d"):
                return R.drawable.i01d;
            case("01n"):
                return R.drawable.i01n;
            case("02d"):
                return R.drawable.i02d;
            case("02n"):
                return R.drawable.i02n;
            case("03d"):
                return R.drawable.i03d;
            case("03n"):
                return R.drawable.i03n;
            case("04d"):
                return R.drawable.i04d;
            case("04n"):
                return R.drawable.i04n;
            case("09d"):
                return R.drawable.i09d;
            case("09n"):
                return R.drawable.i09n;
            case("10d"):
                return R.drawable.i10d;
            case("10n"):
                return R.drawable.i10n;
            case("11d"):
                return R.drawable.i11d;
            case("11n"):
                return R.drawable.i11n;
            case("13d"):
                return R.drawable.i13d;
            case("13n"):
                return R.drawable.i13n;
            case("50d"):
                return R.drawable.i50d;
            case("50n"):
                return R.drawable.i50n;
            default:
                return 0;
        }
    }
}
