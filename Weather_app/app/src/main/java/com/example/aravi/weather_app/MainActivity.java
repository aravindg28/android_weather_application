package com.example.aravi.weather_app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*************************************************************************************************************************************************************
Program: MainActivity class for weather application
Author: Aravind
Date of creation: 25-Oct-2018
*************************************************************************************************************************************************************/

public class MainActivity extends AppCompatActivity {

    private TextView city_input;
    private TextView city_output;
    private TextView temperature;
    private TextView min_max;
    private TextView weather;
    private TextView weather_desc;
    private TextView humidity_value;
    private TextView cloud_value;
    private Button getWeather;
    private Weather weather_obj;
    private Runnable runnable;
    private Button refreshBtn;
    private LinearLayout linearLayout;
    private TextView day_night_icon;
    private AlphaAnimation buttonClickAnimation; //for blinking the button when it is clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();

    }

    //Initializes the UI components

    private void initializeComponents() {

        city_input = findViewById(R.id.city_name_input);
        city_output = findViewById(R.id.city_name_output);
        temperature = findViewById(R.id.temperature);
        min_max = findViewById(R.id.min_max);
        weather = findViewById(R.id.main_weather);
        weather_desc = findViewById(R.id.weather_desc);
        humidity_value = findViewById(R.id.humidity_output);
        cloud_value = findViewById(R.id.clouds_output);
        getWeather = findViewById(R.id.weatherBtn);
        refreshBtn = findViewById(R.id.refreshBtn);
        linearLayout = findViewById(R.id.linearLayoutId);
        day_night_icon = findViewById(R.id.day_night_icon);


        weather_obj = new Weather();

        buttonClickAnimation = new AlphaAnimation(1F, 0.8F);


        //Fetching default weather details - have used Halifax

        weather_obj.setCityInput("Halifax");
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeatherData(weather_obj.getCityInput());

            }
        };

        //retrieve data on separate thread
        Thread thread = new Thread(null, runnable, "background");
        thread.start();


        //Fetching the weather details when the search button is clicked
        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                weather_obj.setCityInput(city_input.getText().toString().toUpperCase());
                city_input.setText(null);

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        getWeatherData(weather_obj.getCityInput());
                    }
                };

                //retrieve data on separate thread
                Thread thread = new Thread(null, runnable, "background");
                thread.start();

                //close the soft keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });

        //Fetching the weather details of the already requested city when the refresh button is clicked
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                if(weather_obj.getCityOutput() != null){
                    Toast.makeText(getApplicationContext(), "Data refreshed successfully for " +weather_obj.getCityOutput(), Toast.LENGTH_SHORT).show();

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            getWeatherData(weather_obj.getCityOutput());
                        }
                    };

                    //retrieve data on separate thread
                    Thread thread = new Thread(null, runnable, "background");
                    thread.start();

                    //close the soft keyboard
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    //Method for making HTTP request and getting the weather data

    private void getWeatherData(String city) {
        if(city != null && !city.trim().equals("")){
            String url1 = "http://api.openweathermap.org/data/2.5/weather?";
            String url2 = "q="+city;
            String url3 = "&appid=aa7644eddf5db15ddb88d5ae7677fb0a&units=metric";
            String url = url1 + url2 + url3;

            //build the request

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        //fetching value from json
                        weather_obj.setCityOutput(response.getString("name"));

                        JSONObject main_object = response.getJSONObject("main");
                        weather_obj.setTemperatureValue(String.valueOf(main_object.getDouble("temp")));
                        weather_obj.setHumidityValue(String.valueOf(main_object.getInt("humidity")));
                        weather_obj.setMinTemperature(String.valueOf(main_object.getDouble("temp_min")));
                        weather_obj.setMaxTemperature(String.valueOf(main_object.getDouble("temp_max")));
                        weather_obj.setHumidityText("Humidity");

                        JSONArray weather_array = response.getJSONArray("weather");
                        JSONObject weather_array_object = weather_array.getJSONObject(0);
                        weather_obj.setWeatherValue(weather_array_object.getString("main"));
                        weather_obj.setWeatherDescValue(weather_array_object.getString("description").toUpperCase());
                        String dayNightVal = weather_array_object.getString("icon");
                        /*NOTE: The JSON value of day and night returned from OpenWeatherAPI sometimes is out of sync.
                         The value of day or night may not be refreshed for some cities.*/
                        if(dayNightVal.contains("d")){
                           weather_obj.setDay(true);
                           weather_obj.setNight(false);
                        }
                        else{
                            weather_obj.setDay(false);
                            weather_obj.setNight(true);
                        }

                        JSONObject clouds_object = response.getJSONObject("clouds");
                        weather_obj.setCloudText("Clouds");
                        weather_obj.setCloudValue(String.valueOf(clouds_object.getInt("all")));

                        //setting fetched value to the UI
                        city_output.setText(weather_obj.getCityOutput());
                        temperature.setText(weather_obj.getTemperatureValue() + "°C");
                        min_max.setText("Min. " +weather_obj.getMinTemperature() + "°C" + " Max. " +weather_obj.getMaxTemperature() + "°C");
                        weather.setText(weather_obj.getWeatherValue().toUpperCase());
                        weather_desc.setText(weather_obj.getWeatherDescValue().toUpperCase());
                        humidity_value.setText(weather_obj.getHumidityValue() + "%" + " Humidity");
                        cloud_value.setText(weather_obj.getCloudValue() + "%" + " Clouds");

                        if(weather_obj.isDay()){
                            day_night_icon.setText("Day");
                            day_night_icon.setCompoundDrawablesWithIntrinsicBounds( R.drawable.sunrise,0 , 0, 0);
                        }

                        else{
                            day_night_icon.setText("Night");
                            day_night_icon.setCompoundDrawablesWithIntrinsicBounds( R.drawable.moon,0 , 0, 0);

                        }

                        //setting background image based on the weather
                        setBackground(linearLayout,weather_obj.getWeatherValue());
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    NetworkResponse response = error.networkResponse;
                    Log.e("err value",""+response.statusCode);
                    clear();
                    if(response != null){
                        if("404".equals(String.valueOf(response.statusCode))){   //The weather API returns an error code 404 for an invalid city name
                            temperature.setText("Please enter a valid city");
                            min_max.setText("For example: Toronto");
                        }
                    }

                }
            });

            //add the request to queue
            RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

        }



    }

    //Sets the background dynamically according to the weather condition

    private void setBackground(LinearLayout linearLayout, String weatherValue) {

        switch(weatherValue){
            case("Rain"): linearLayout.setBackgroundResource(R.drawable.rain);
            break;
            case("Clouds"):linearLayout.setBackgroundResource(R.drawable.clouds);
            break;
            case("Mist"):linearLayout.setBackgroundResource(R.drawable.mist);
            break;
            case("Smoke"):linearLayout.setBackgroundResource(R.drawable.smoke);
            break;
            case("Drizzle"):linearLayout.setBackgroundResource(R.drawable.drizzle);
            break;
            case("Clear"):linearLayout.setBackgroundResource(R.drawable.clear);
            break;
            case("Thunderstorm"):linearLayout.setBackgroundResource(R.drawable.thunderstorm);
            break;
            case("Haze"):linearLayout.setBackgroundResource(R.drawable.haze);
            break;
            case("Fog"):linearLayout.setBackgroundResource(R.drawable.fog);
            break;
            case("Snow"):linearLayout.setBackgroundResource(R.drawable.snow);
            break;
            default:
                break;


        }


    }

    //Clears the UI components

    private void clear() {
        city_output.setText(null);
        temperature.setText(null);
        min_max.setText(null);
        weather.setText(null);
        weather_desc.setText(null);
        humidity_value.setText(null);
        cloud_value.setText(null);
        day_night_icon.setText(null);
        day_night_icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        weather_obj.setCityOutput(null);
    }


}
