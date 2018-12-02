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
import android.widget.ImageView;
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


    TextView selectCity, cityField, detailsField1, maxTemp1, minTemp1, updateDate1,
            detailsField2, maxTemp2, minTemp2, updateDate2,
            detailsField3, maxTemp3, minTemp3, updateDate3,
            detailsField4, maxTemp4, minTemp4, updateDate4,
            detailsField5, maxTemp5, minTemp5, updateDate5,
            detailsField6, maxTemp6, minTemp6, updateDate6,
            detailsField7, maxTemp7, minTemp7, updateDate7,
            detailsField8, maxTemp8, minTemp8, updateDate8,
            detailsField9, maxTemp9, minTemp9, updateDate9,
            detailsField10, maxTemp10, minTemp10, updateDate10;


    ImageView imageView1, imageView2, imageView3, imageView4, imageView5,
            imageView6, imageView7, imageView8, imageView9, imageView10;

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
        cityField = v.findViewById(R.id.city_field1);




        cityField.setText(location);

        updateDate1 = v.findViewById(R.id.updated_field1);
        detailsField1 = v.findViewById(R.id.details_field1);
        maxTemp1 = v.findViewById(R.id.maxTemp1);
        minTemp1 = v.findViewById(R.id.minTemp1);
        imageView1 = v.findViewById(R.id.imageView2);


        updateDate2 = v.findViewById(R.id.updated_field2);
        detailsField2 = v.findViewById(R.id.details_field2);
        maxTemp2 = v.findViewById(R.id.maxTemp2);
        minTemp2 = v.findViewById(R.id.minTemp2);
        imageView2 = v.findViewById(R.id.imageView3);

        updateDate3 = v.findViewById(R.id.updated_field3);
        detailsField3 = v.findViewById(R.id.details_field3);
        maxTemp3= v.findViewById(R.id.maxTemp3);
        minTemp3 = v.findViewById(R.id.minTemp3);
        imageView3 = v.findViewById(R.id.imageView4);

        updateDate4 = v.findViewById(R.id.updated_field4);
        detailsField4 = v.findViewById(R.id.details_field4);
        maxTemp4 = v.findViewById(R.id.maxTemp4);
        minTemp4 = v.findViewById(R.id.minTemp4);
        imageView4 = v.findViewById(R.id.imageView5);

        updateDate5 = v.findViewById(R.id.updated_field5);
        detailsField5 = v.findViewById(R.id.details_field5);
        maxTemp5 = v.findViewById(R.id.maxTemp5);
        minTemp5 = v.findViewById(R.id.minTemp5);
        imageView5 = v.findViewById(R.id.imageView6);

        updateDate6 = v.findViewById(R.id.updated_field6);
        detailsField6 = v.findViewById(R.id.details_field6);
        maxTemp6 = v.findViewById(R.id.maxTemp6);
        minTemp6 = v.findViewById(R.id.minTemp6);
        imageView6 = v.findViewById(R.id.imageView7);

        updateDate7 = v.findViewById(R.id.updated_field7);
        detailsField7 = v.findViewById(R.id.details_field7);
        maxTemp7 = v.findViewById(R.id.maxTemp7);
        minTemp7 = v.findViewById(R.id.minTemp7);
        imageView7 = v.findViewById(R.id.imageView8);

        updateDate8 = v.findViewById(R.id.updated_field8);
        detailsField8 = v.findViewById(R.id.details_field8);
        maxTemp8 = v.findViewById(R.id.maxTemp8);
        minTemp8 = v.findViewById(R.id.minTemp8);
        imageView8 = v.findViewById(R.id.imageView9);

        updateDate9 = v.findViewById(R.id.updated_field9);
        detailsField9 = v.findViewById(R.id.details_field9);
        maxTemp9 = v.findViewById(R.id.maxTemp9);
        minTemp9 = v.findViewById(R.id.minTemp9);
        imageView9 = v.findViewById(R.id.imageView10);

        updateDate10 = v.findViewById(R.id.updated_field10);
        detailsField10 = v.findViewById(R.id.details_field10);
        maxTemp10 = v.findViewById(R.id.maxTemp10);
        minTemp10 = v.findViewById(R.id.minTemp10);
        imageView10 = v.findViewById(R.id.imageView11);






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
            String xml = Weather_Content.excuteGet("https://api.weatherbit.io/v2.0/forecast/daily?city=Tacoma&units=imperial&key=" + WEATHER_MAP_API);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if(json != null) {
                    String iconId1, iconId2, iconId3, iconId4, iconId5, iconId6,
                            iconId7, iconId8, iconId9, iconId10;
                    int imageResource1, imageResource2, imageResource3, imageResource4, imageResource5,
                            imageResource6, imageResource7, imageResource8, imageResource9, imageResource10;
                    JSONObject day1 = json.getJSONArray("data").getJSONObject(0);
                    JSONObject weather1 = day1.getJSONObject("weather");

                    iconId1 = weather1.getString("icon");
                    updateDate1.setText(day1.getString("datetime"));
                    imageResource1 = getResources().getIdentifier(("@drawable/"+iconId1),null, getActivity().getPackageName());
                    imageView1.setImageResource(imageResource1);
                    maxTemp1.setText("Max "+String.format("%.2f", day1.getDouble("max_temp")) + "°");
                    minTemp1.setText("Min" +String.format("%.2f", day1.getDouble("min_temp")) + "°");
                    detailsField1.setText(weather1.getString("description").toUpperCase(Locale.US));


                    JSONObject day2 = json.getJSONArray("data").getJSONObject(1);
                    JSONObject weather2 = day2.getJSONObject("weather");

                    iconId2 = weather2.getString("icon");
                    updateDate2.setText(day2.getString("datetime"));
                    imageResource2 = getResources().getIdentifier(("@drawable/"+iconId2),null, getActivity().getPackageName());
                    imageView2.setImageResource(imageResource2);
                    maxTemp2.setText("Max "+String.format("%.2f", day2.getDouble("max_temp")) + "°");
                    minTemp2.setText("Min" +String.format("%.2f", day2.getDouble("min_temp")) + "°");
                    detailsField2.setText(weather2.getString("description").toUpperCase(Locale.US));

                    JSONObject day3 = json.getJSONArray("data").getJSONObject(2);
                    JSONObject weather3 = day3.getJSONObject("weather");

                    iconId3 = weather3.getString("icon");
                    updateDate3.setText(day3.getString("datetime"));
                    imageResource3 = getResources().getIdentifier(("@drawable/"+iconId3),null, getActivity().getPackageName());
                    imageView3.setImageResource(imageResource3);
                    maxTemp3.setText("Max "+String.format("%.2f", day3.getDouble("max_temp")) + "°");
                    minTemp3.setText("Min "+String.format("%.2f", day3.getDouble("min_temp")) + "°");
                    detailsField3.setText(weather3.getString("description").toUpperCase(Locale.US));

                    JSONObject day4 = json.getJSONArray("data").getJSONObject(3);
                    JSONObject weather4 = day4.getJSONObject("weather");

                    iconId4 = weather4.getString("icon");
                    updateDate4.setText(day4.getString("datetime"));
                    imageResource4 = getResources().getIdentifier(("@drawable/"+iconId4),null, getActivity().getPackageName());
                    imageView4.setImageResource(imageResource4);
                    maxTemp4.setText("Max "+String.format("%.2f", day4.getDouble("max_temp")) + "°");
                    minTemp4.setText("Min" +String.format("%.2f", day4.getDouble("min_temp")) + "°");
                    detailsField4.setText(weather4.getString("description").toUpperCase(Locale.US));

                    JSONObject day5 = json.getJSONArray("data").getJSONObject(4);
                    JSONObject weather5 = day5.getJSONObject("weather");

                    iconId5 = weather5.getString("icon");
                    updateDate5.setText(day5.getString("datetime"));
                    imageResource5 = getResources().getIdentifier(("@drawable/"+iconId5),null, getActivity().getPackageName());
                    imageView5.setImageResource(imageResource5);
                    maxTemp5.setText("Max "+String.format("%.2f", day5.getDouble("max_temp")) + "°");
                    minTemp5.setText("Min "+String.format("%.2f", day5.getDouble("min_temp")) + "°");
                    detailsField5.setText(weather5.getString("description").toUpperCase(Locale.US));

                    JSONObject day6 = json.getJSONArray("data").getJSONObject(5);
                    JSONObject weather6 = day6.getJSONObject("weather");

                    iconId6 = weather6.getString("icon");
                    updateDate6.setText(day6.getString("datetime"));
                    imageResource6 = getResources().getIdentifier(("@drawable/"+iconId6),null, getActivity().getPackageName());
                    imageView6.setImageResource(imageResource6);
                    maxTemp6.setText("Max "+String.format("%.2f", day6.getDouble("max_temp")) + "°");
                    minTemp6.setText("Min "+String.format("%.2f", day6.getDouble("min_temp")) + "°");
                    detailsField6.setText(weather6.getString("description").toUpperCase(Locale.US));

                    JSONObject day7 = json.getJSONArray("data").getJSONObject(6);
                    JSONObject weather7 = day7.getJSONObject("weather");

                    iconId7 = weather7.getString("icon");
                    updateDate7.setText(day7.getString("datetime"));
                    imageResource7 = getResources().getIdentifier(("@drawable/"+iconId7),null, getActivity().getPackageName());
                    imageView7.setImageResource(imageResource7);
                    maxTemp7.setText("Max "+String.format("%.2f", day7.getDouble("max_temp")) + "°");
                    minTemp7.setText("Min "+String.format("%.2f", day7.getDouble("min_temp")) + "°");
                    detailsField7.setText(weather7.getString("description").toUpperCase(Locale.US));

                    JSONObject day8 = json.getJSONArray("data").getJSONObject(7);
                    JSONObject weather8 = day8.getJSONObject("weather");

                    iconId8 = weather8.getString("icon");
                    updateDate8.setText(day8.getString("datetime"));
                    imageResource8 = getResources().getIdentifier(("@drawable/"+iconId8),null, getActivity().getPackageName());
                    imageView8.setImageResource(imageResource8);
                    maxTemp8.setText("Max "+String.format("%.2f", day8.getDouble("max_temp")) + "°");
                    minTemp8.setText("Min "+String.format("%.2f", day8.getDouble("min_temp")) + "°");
                    detailsField8.setText(weather8.getString("description").toUpperCase(Locale.US));

                    JSONObject day9 = json.getJSONArray("data").getJSONObject(8);
                    JSONObject weather9 = day9.getJSONObject("weather");

                    iconId9 = weather9.getString("icon");
                    updateDate9.setText(day9.getString("datetime"));
                    imageResource9 = getResources().getIdentifier(("@drawable/"+iconId9),null, getActivity().getPackageName());
                    imageView9.setImageResource(imageResource9);
                    maxTemp9.setText("Max "+String.format("%.2f", day9.getDouble("max_temp")) + "°");
                    minTemp9.setText("Min "+String.format("%.2f", day9.getDouble("min_temp")) + "°");
                    detailsField9.setText(weather9.getString("description").toUpperCase(Locale.US));

                    JSONObject day10 = json.getJSONArray("data").getJSONObject(9);
                    JSONObject weather10 = day10.getJSONObject("weather");

                    iconId10 = weather10.getString("icon");
                    updateDate10.setText(day10.getString("datetime"));
                    imageResource10 = getResources().getIdentifier(("@drawable/"+iconId10),null, getActivity().getPackageName());
                    imageView10.setImageResource(imageResource10);
                    maxTemp10.setText("Max "+String.format("%.2f", day10.getDouble("max_temp")) + "°");
                    minTemp10.setText("Min "+String.format("%.2f", day10.getDouble("min_temp")) + "°");
                    detailsField10.setText(weather10.getString("description").toUpperCase(Locale.US));


                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }


    }

}