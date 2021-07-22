
package com.example.weatherprediction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class forecastdata {
    private String micon[]=new String[7],mweather[]=new String[7],date[]=new String[7];
    private int mcondition[]=new int[7];

    public static forecastdata fromJson(JSONObject jsonObject){
        try {
            forecastdata weather = new forecastdata();
            for(int i=0;i<7;i++) {
                weather.mcondition[i] = jsonObject.getJSONArray("daily").getJSONObject(i+1).getJSONArray("weather").getJSONObject(0).getInt("id");
                int d=jsonObject.getJSONArray("daily").getJSONObject(i+1).getInt("dt");
                int sunrise=jsonObject.getJSONArray("daily").getJSONObject(i+1).getInt("sunrise");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                Date dateFormat1 = new java.util.Date(sunrise*1000L);
                String fsunrise = sdf1.format(dateFormat1 );
                int sunset=jsonObject.getJSONArray("daily").getJSONObject(i+1).getInt("sunset");
                Date dateFormat2 = new java.util.Date(sunset*1000L);
                String fsunset = sdf1.format(dateFormat2);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date dateFormat = new java.util.Date(d*1000L);
                String weekday = sdf.format(dateFormat );
                weather.date[i]=weekday;
                int pressure=jsonObject.getJSONArray("daily").getJSONObject(i+1).getInt("pressure");
                int humidity=jsonObject.getJSONArray("daily").getJSONObject(i+1).getInt("humidity");
                double temperature=jsonObject.getJSONArray("daily").getJSONObject(i+1).getJSONObject("temp").getDouble("day")-273.15;
                int rtemperature=(int)Math.rint(temperature);
                double feelslike=jsonObject.getJSONArray("daily").getJSONObject(i+1).getJSONObject("feels_like").getDouble("day")-273.15;
                int rfeelslike=(int)Math.rint(feelslike);
                weather.mweather[i] = jsonObject.getJSONArray("daily").getJSONObject(i+1).getJSONArray("weather").getJSONObject(0).getString("main")+"\nTemperature : "+Integer.toString(rtemperature)+"°C\nFeelslike : "+Integer.toString(rfeelslike)+"°C\nPressure : "+Integer.toString(pressure)+"hPa\nHumidity : "+Integer.toString(humidity)+"%\nSunrise : "+fsunrise+"\nSunset : "+fsunset;
                weather.micon[i] = updateWeatherIcon(weather.mcondition[i]);
            }
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

    public String[] getMicon() {
        return micon;
    }

    public String[] getMweather() {
        return mweather;
    }

    public String[] getDate() {
        return date;
    }
}
