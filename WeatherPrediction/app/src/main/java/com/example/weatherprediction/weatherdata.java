
package com.example.weatherprediction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class weatherdata {
    private String mTemperature,mIcon,mCity,mWeather;
    private int mCondition;
    private double latitude,longitude;

    public static weatherdata fromJson(JSONObject jsonObject){
        try {
            weatherdata weather = new weatherdata();
            int sunrise=jsonObject.getJSONObject("sys").getInt("sunrise");
            int date=jsonObject.getInt("dt");
            SimpleDateFormat sdf1=new SimpleDateFormat("EEEE, dd MMM yyyy");
            Date dateFormat1=new java.util.Date(date*1000L);
            String date1=sdf1.format(dateFormat1);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            Date dateFormat = new java.util.Date(sunrise*1000L);
            String fsunrise = sdf.format(dateFormat );
            int sunset=jsonObject.getJSONObject("sys").getInt("sunset");
            Date dateFormat2 = new java.util.Date(sunset*1000L);
            String fsunset = sdf.format(dateFormat2);
            int pressure=jsonObject.getJSONObject("main").getInt("pressure");
            int humidity=jsonObject.getJSONObject("main").getInt("humidity");
            double temp_min=jsonObject.getJSONObject("main").getDouble("temp_min")-273.15;
            int rtemp_min=(int)Math.rint(temp_min);
            double temp_max=jsonObject.getJSONObject("main").getDouble("temp_max")-273.15;
            int rtemp_max=(int)Math.rint(temp_max);
            double feelslike=jsonObject.getJSONObject("main").getDouble("feels_like")-273.15;
            int rfeelslike=(int)Math.rint(feelslike);
            double windspeed=jsonObject.getJSONObject("wind").getDouble("speed");
            weather.mCity = jsonObject.getString("name");
            weather.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weather.mWeather=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")+"\n"+date1+"\nFeels like : "+Integer.toString(rfeelslike)+"°C\nMin Temperature : "+Integer.toString(rtemp_min)+"°C\nMax Temperature : "+Integer.toString(rtemp_max)+"°C\nPressure : "+Integer.toString(pressure)+"hPa\nHumidity : "+Integer.toString(humidity)+"%\nWind speed : "+Double.toString(windspeed)+"m/s"+"\nSunrise : "+fsunrise+"\nSunset : "+fsunset;
            weather.mIcon=updateWeatherIcon(weather.mCondition);
            weather.latitude=jsonObject.getJSONObject("coord").getDouble("lat");
            weather.longitude=jsonObject.getJSONObject("coord").getDouble("lon");
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int rValue=(int)Math.rint(tempResult);
            weather.mTemperature=Integer.toString(rValue);
            return weather;
        }
        catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition) {
        if (condition >= 0 && condition < 300) {
            return "thunder";
        }
        else if (condition >= 300 && condition < 500) {
            return "rain";
        }
        else if (condition >= 500 && condition < 600) {
            return "rain2";
        }
        else if (condition >= 600 && condition < 700) {
            return "snow";
        }
        else if (condition >= 700 && condition < 800) {
            return "atmos";
        }
        else if (condition == 800) {
            return "clear";
        }
        else if (condition == 801 || condition == 802) {
            return "clouds";
        }
        else if (condition == 803 || condition == 804) {
            return "partclouds";
        }
        return "dunno";
    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmWeather() {
        return mWeather;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
