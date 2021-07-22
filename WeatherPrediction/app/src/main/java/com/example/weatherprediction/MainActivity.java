package com.example.weatherprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    final String APP_ID = "d4af27f957c8ee7c20fb6eb24246b7db";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    final String WEATHER_URL1="https://api.openweathermap.org/data/2.5/onecall";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;
    TextView NameOfCity, weatherState, Temperature,weatherdata1,weatherdata2,weatherdata3,weatherdata4,weatherdata5,weatherdata6,weatherdata7;
    TextView day1,day2,day3,day4,day5,day6,day7;
    ImageView weatherIcon,icon1,icon2,icon3,icon4,icon5,icon6,icon7;
    RelativeLayout mCityFinder;
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        weatherState = findViewById(R.id.weatherdata);
        Temperature = findViewById(R.id.temparature);
        weatherIcon = findViewById(R.id.weathericon);
        mCityFinder = findViewById(R.id.city);
        NameOfCity = findViewById(R.id.cityname);
        weatherdata1=findViewById(R.id.weatherdata1);
        weatherdata2=findViewById(R.id.weatherdata2);
        weatherdata3=findViewById(R.id.weatherdata3);
        weatherdata4=findViewById(R.id.weatherdata4);
        weatherdata5=findViewById(R.id.weatherdata5);
        weatherdata6=findViewById(R.id.weatherdata6);
        weatherdata7=findViewById(R.id.weatherdata7);
        icon1=findViewById(R.id.icon1);
        icon2=findViewById(R.id.icon2);
        icon3=findViewById(R.id.icon3);
        icon4=findViewById(R.id.icon4);
        icon5=findViewById(R.id.icon5);
        icon6=findViewById(R.id.icon6);
        icon7=findViewById(R.id.icon7);
        day1=findViewById(R.id.day1);
        day2=findViewById(R.id.day2);
        day3=findViewById(R.id.day3);
        day4=findViewById(R.id.day4);
        day5=findViewById(R.id.day5);
        day6=findViewById(R.id.day6);
        day7=findViewById(R.id.day7);


        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });


    }

   /* @Override
    protected void onResume() {
        super.onResume();
        getWeatherforCurrentLocation();
    } */

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city=mIntent.getStringExtra("City");
        if(city!=null){
            getWeatherForNewCity(city);
        }
        else{
            getWeatherforCurrentLocation();
        }
    }
    private void getWeatherForNewCity(String city){
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        letsdoSomeNetworking(params);
    }

    private void getWeatherforCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params=new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                letsdoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                //Location disabled;
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"Location obtained successfully",Toast.LENGTH_SHORT).show();
                getWeatherforCurrentLocation();
            }
            else{
                Toast.makeText(MainActivity.this,"Please provide location",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void letsdoSomeNetworking(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // super.onSuccess(statusCode, headers, response);
                Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();
                weatherdata weather = weatherdata.fromJson(response);
                updateUI(weather);
                double lat=weather.getLatitude();
                double lon=weather.getLongitude();
                RequestParams params1=new RequestParams();
                params1.put("lat",lat);
                params1.put("lon",lon);
                params1.put("appid",APP_ID);
                letsdoSomeNetworking1(params1);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
        private void letsdoSomeNetworking1(RequestParams params){
        AsyncHttpClient client1=new AsyncHttpClient();
        client1.get(WEATHER_URL1,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Toast.makeText(MainActivity.this,"ForecastData Get Success",Toast.LENGTH_SHORT).show();
                forecastdata forecast=forecastdata.fromJson(response);
                updateUI(forecast);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(weatherdata weather){
        Temperature.setText(weather.getmTemperature());
        NameOfCity.setText(weather.getmCity());
        weatherState.setText(weather.getmWeather());
        int resourceID=getResources().getIdentifier(weather.getmIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceID);
    }
    private void updateUI(forecastdata forecast){
        String weather[]=forecast.getMweather(),icon[]=forecast.getMicon(),day[]=forecast.getDate();
        int resourceID1,resourceID2,resourceID3,resourceID4,resourceID5,resourceID6,resourceID7;
        weatherdata1.setText(weather[0]);
        weatherdata2.setText(weather[1]);
        weatherdata3.setText(weather[2]);
        weatherdata4.setText(weather[3]);
        weatherdata5.setText(weather[4]);
        weatherdata6.setText(weather[5]);
        weatherdata7.setText(weather[6]);
        resourceID1=getResources().getIdentifier(icon[0],"drawable",getPackageName());
        resourceID2=getResources().getIdentifier(icon[1],"drawable",getPackageName());
        resourceID3=getResources().getIdentifier(icon[2],"drawable",getPackageName());
        resourceID4=getResources().getIdentifier(icon[3],"drawable",getPackageName());
        resourceID5=getResources().getIdentifier(icon[4],"drawable",getPackageName());
        resourceID6=getResources().getIdentifier(icon[5],"drawable",getPackageName());
        resourceID7=getResources().getIdentifier(icon[6],"drawable",getPackageName());
        icon1.setImageResource(resourceID1);
        icon2.setImageResource(resourceID2);
        icon3.setImageResource(resourceID3);
        icon4.setImageResource(resourceID4);
        icon5.setImageResource(resourceID5);
        icon6.setImageResource(resourceID6);
        icon7.setImageResource(resourceID7);
        day1.setText(day[0]);
        day2.setText(day[1]);
        day3.setText(day[2]);
        day4.setText(day[3]);
        day5.setText(day[4]);
        day6.setText(day[5]);
        day7.setText(day[6]);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener);
        }

    }
}