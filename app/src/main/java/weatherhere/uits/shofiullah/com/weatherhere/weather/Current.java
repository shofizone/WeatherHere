package weatherhere.uits.shofiullah.com.weatherhere.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by shofi on 05-Nov-17.
 */

public class Current {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;
    private String mCity;

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night

    public int getIconId() {


        return Forcast.getIconId(mIcon);
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getFormatedTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h: mm a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date datTime = new Date(getTime() * 1000);
        String timeString = simpleDateFormat.format(datTime);


        return timeString;
    }


    public double getTemperature() {
        return Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = (temperature - 32) * (0.5556);
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public String getPrecipChance() {

        if (mPrecipChance == 0) {
            return "NO";
        } else {
            return "Yes";
        }


    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
