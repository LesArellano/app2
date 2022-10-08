package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONObject;

public class MainActivity<TextView> extends AppCompatActivity {

    final String APP_ID="f7a749426c5a242384d76b1dd389e292";
    final String WEATHER_URL="https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME=5000;
    final float MIN_DISTANCE=1000;
    final int REQUEST_CODE=101;


    String location_Provider= LocationManager.GPS_PROVIDER;

    TextView NameOfCity,weatherState,Temperature;
    ImageView mweatherIcon;

    RelativeLayout mCityFinder;

    LocationManager mLocationManager;
    LocationListener mLocationListener;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState=findViewById(R.id.weatherCondition);
        Temperature=findViewById(R.id.temperature);
        mweatherIcon=findViewById(R.id.weatherIcon);
        mCityFinder=findViewById(R.id.cityFinder);
        NameOfCity=findViewById(R.id.cityName);




        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,cityFinder.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getWeatherforCurrentLocation();
    }

       private void getWeatherforCurrentLocation() {
           mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
           mLocationListener = new LocationListener() {
               @Override
               public void onLocationChanged(Location location) {

                   String Latitude = String.valueOf(location.getLatitude());
                   String Longitude = String.valueOf(location.getLongitude());


                   RequestParams params=new RequestParams();
                   params.put("lat" ,Latitude);
                   params.put("lon" ,Longitude);
                   params.put("appid" ,APP_ID);
                   letsdoSomeNetworking(params);


               }

               @Override
               public void onStatusChanged(String provider, int status, Bundle extras) {

               }

               @Override
               public void onProviderEnabled(String provider) {


               }

               @Override
               public void onProviderDisabled(String provider) {
                   //not able to get Location
               }
           };


           mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);
       }




       @Override
       public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
            super.onRequestPermissionsResult(requestCode, permission, grantResult);




            if (requestCode==REQUEST_CODE)
            {
                if(grantResult.length>0 && grantResult[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(MainActivity.this, "Locationonget Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //user denied the permission
                }
            }

    }

    private void letsdoSomeNetworking(RequestParams params) {
        AsyncHttpClient client= new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
           @Override
           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

               Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();


               weatherData weatherD=weatherData.fromJson(response);
               updateUI(weatherD);



               //super.onSuccesss(stastusCode, header, response);
           }

            @Override
            public void onFailure(int statusCode Header[] headers, Throwable, throwable, JSONObject errrorResponse) {
               //super.onFailure(statusCode, headers, throwable, errorResponse);
            }



        });
    }

    private  void updateUI(weatherData){


        Temperature.setText(weather.getmTemperature());
        NameOfCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable", getPackage());
        mweatherIcon.setImageResource(resourceID);
    }

@Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null)
        {
            mLocationManager.removeUpdates(mLocationListener);
        }
}
}