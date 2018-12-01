package com.aashishkumar.androidproject;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherforecastFragment extends Fragment {


    TextView selectCity, cityField, detailsField1, maxTemp1, minTemp1, weatherIcon1, updateDate1,
            detailsField2, maxTemp2, minTemp2, weatherIcon2, updateDate2,
            detailsField3, maxTemp3, minTemp3, weatherIcon3, updateDate3,
            detailsField4, maxTemp4, minTemp4, weatherIcon4, updateDate4,
            detailsField5, maxTemp5, minTemp5, weatherIcon5, updateDate5,
            detailsField6, maxTemp6, minTemp6, weatherIcon6, updateDate6,
            detailsField7, maxTemp7, minTemp7, weatherIcon7, updateDate7,
            detailsField8, maxTemp8, minTemp8, weatherIcon8, updateDate8,
            detailsField9, maxTemp9, minTemp9, weatherIcon9, updateDate9,
            detailsField10, maxTemp10, minTemp10, weatherIcon10, updateDate10;
    Typeface weatherFont;
    String location = "Tacoma";
    //    String city, country;
    String WEATHER_MAP_API = "58b43eca9e254f02a1f7b75ee9525838"; //
    MyLocationsActivity myLocationsActivity;


    public WeatherforecastFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_weatherforecast, container, false);

        selectCity = v.findViewById(R.id.selectCity);
        cityField = v.findViewById(R.id.city_field);


        cityField.setText(location);

        updateDate1 = v.findViewById(R.id.updated_field1);
        detailsField1 = v.findViewById(R.id.details_field1);
        maxTemp1 = v.findViewById(R.id.maxTemp1);
        minTemp1 = v.findViewById(R.id.minTemp1);
        weatherIcon1 = v.findViewById(R.id.weather_icon1);

        updateDate2 = v.findViewById(R.id.updated_field2);
        detailsField2 = v.findViewById(R.id.details_field2);
        maxTemp2 = v.findViewById(R.id.maxTemp2);
        minTemp2 = v.findViewById(R.id.minTemp2);
        weatherIcon2 = v.findViewById(R.id.weather_icon2);

        updateDate3 = v.findViewById(R.id.updated_field3);
        detailsField3 = v.findViewById(R.id.details_field3);
        maxTemp3= v.findViewById(R.id.maxTemp3);
        minTemp3 = v.findViewById(R.id.minTemp3);
        weatherIcon3 = v.findViewById(R.id.weather_icon3);

        updateDate4 = v.findViewById(R.id.updated_field4);
        detailsField4 = v.findViewById(R.id.details_field4);
        maxTemp4 = v.findViewById(R.id.maxTemp4);
        minTemp4 = v.findViewById(R.id.minTemp4);
        weatherIcon4 = v.findViewById(R.id.weather_icon4);

        updateDate5 = v.findViewById(R.id.updated_field5);
        detailsField5 = v.findViewById(R.id.details_field5);
        maxTemp5 = v.findViewById(R.id.maxTemp5);
        minTemp5 = v.findViewById(R.id.minTemp5);
        weatherIcon5 = v.findViewById(R.id.weather_icon5);

        updateDate6 = v.findViewById(R.id.updated_field6);
        detailsField6 = v.findViewById(R.id.details_field6);
        maxTemp6 = v.findViewById(R.id.maxTemp6);
        minTemp6 = v.findViewById(R.id.minTemp6);
        weatherIcon6 = v.findViewById(R.id.weather_icon6);

        updateDate7 = v.findViewById(R.id.updated_field7);
        detailsField7 = v.findViewById(R.id.details_field7);
        maxTemp7 = v.findViewById(R.id.maxTemp7);
        minTemp7 = v.findViewById(R.id.minTemp7);
        weatherIcon7 = v.findViewById(R.id.weather_icon7);

        updateDate8 = v.findViewById(R.id.updated_field8);
        detailsField8 = v.findViewById(R.id.details_field8);
        maxTemp8 = v.findViewById(R.id.maxTemp8);
        minTemp8 = v.findViewById(R.id.minTemp8);
        weatherIcon8 = v.findViewById(R.id.weather_icon8);

        updateDate9 = v.findViewById(R.id.updated_field9);
        detailsField9 = v.findViewById(R.id.details_field9);
        maxTemp9 = v.findViewById(R.id.maxTemp9);
        minTemp9 = v.findViewById(R.id.minTemp9);
        weatherIcon9 = v.findViewById(R.id.weather_icon9);

        updateDate10 = v.findViewById(R.id.updated_field10);
        detailsField10 = v.findViewById(R.id.details_field10);
        maxTemp10 = v.findViewById(R.id.maxTemp10);
        minTemp10 = v.findViewById(R.id.minTemp10);
        weatherIcon10 = v.findViewById(R.id.weather_icon10);




        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon1.setTypeface(weatherFont);
        weatherIcon2.setTypeface(weatherFont);
        weatherIcon3.setTypeface(weatherFont);
        weatherIcon4.setTypeface(weatherFont);
        weatherIcon5.setTypeface(weatherFont);
        weatherIcon6.setTypeface(weatherFont);
        weatherIcon7.setTypeface(weatherFont);
        weatherIcon8.setTypeface(weatherFont);
        weatherIcon9.setTypeface(weatherFont);
        weatherIcon10.setTypeface(weatherFont);


        WeatherforecastFragment.DownloadWeather task = new WeatherforecastFragment.DownloadWeather();
        task.execute(location);


        return v;
    }



    class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String xml = Weather_Content.excuteDailyForecastGet("https://api.weatherbit.io/v2.0/forecast/daily?city=Tacoma&units=imperial&appid=" + WEATHER_MAP_API);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if(json != null) {
                    JSONObject day1 = json.getJSONArray("data").getJSONObject(0);
                   // JSONObject main = json.getJSONObject("weather");



                    //detailsField1.setText(main.getString("description").toUpperCase(Locale.US));
                    maxTemp1.setText(String.format("%.2f", day1.getDouble("max_temp")) + "°");
                    updateDate1.setText(day1.getString("datetime"));
//                    weatherIcon1.setText(Html.fromHtml(Weather_Content.setWeatherIcon(main.getInt("code"),
//                            json.getJSONObject("sys").getLong("sunrise") * 1000,
//                            json.getJSONObject("sys").getLong("sunset") * 1000)));


                     day1 = json.getJSONArray("data").getJSONObject(1);
//                     JSONObject main = json.getJSONObject("weather");



                    //detailsField1.setText(main.getString("description").toUpperCase(Locale.US));
                    maxTemp2.setText(String.format("%.2f", day1.getDouble("max_temp")) + "°");
                    updateDate2.setText(day1.getString("datetime"));
//                    weatherIcon1.setText(Html.fromHtml(Weather_Content.setWeatherIcon(main.getInt("code"),
//                            json.getJSONObject("sys").getLong("sunrise") * 1000,
//                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }


    }

}
