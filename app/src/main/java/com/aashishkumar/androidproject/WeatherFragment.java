package com.aashishkumar.androidproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {


    TextView selectCity, cityField, detailsField, currentTemperatureField, pressure_field, updatedField;
    ImageView imageView;
    ProgressBar loader;
    String location;
    String WEATHER_MAP_API = "58b43eca9e254f02a1f7b75ee9525838";


    double lat, lng;
    String cityName, countryName;


    public WeatherFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        loader = v.findViewById(R.id.loader);
        selectCity = v.findViewById(R.id.selectCity);
        cityField = v.findViewById(R.id.city_field);
        updatedField = v.findViewById(R.id.updated_field);
        detailsField = v.findViewById(R.id.details_field);
        currentTemperatureField = v.findViewById(R.id.current_temperature_field);
        pressure_field = v.findViewById(R.id.pressure_field);
        imageView = v.findViewById(R.id.image1View);




        Geocoder geocoder;
        String bestProvider;
        List<Address> user = null;


        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        @SuppressLint("MissingPermission") Location locationNew = lm.getLastKnownLocation(bestProvider);

        if (locationNew == null){
            Toast.makeText(getActivity(),"Location Not found",Toast.LENGTH_LONG).show();
        }else{
            geocoder = new Geocoder(getActivity());
            try {
                user = geocoder.getFromLocation(locationNew.getLatitude(), locationNew.getLongitude(), 1);
                lat=(double)user.get(0).getLatitude();
                lng=(double)user.get(0).getLongitude();
                System.out.println(" DDD lat: " +lat+",  longitude: "+lng);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        Geocoder geocoder1 = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder1.getFromLocation(lat, lng, 1);
            cityName = addresses.get(0).getLocality();
            countryName = addresses.get(0).getCountryCode();

        } catch (IOException e) {
            e.printStackTrace();
        }





        location = cityName.concat(", "+countryName);



        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("Place", location);
        fragment.setArguments(args);


        WeatherFragment.DownloadWeather task = new WeatherFragment.DownloadWeather();
        task.execute(location);









        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle("Change City");
                final EditText input = new EditText(v.getContext());
                input.setText(location);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Change",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                location = input.getText().toString();
                                WeatherFragment.DownloadWeather task = new WeatherFragment.DownloadWeather();
                                task.execute(location);
                                WeatherFragment fragment = new WeatherFragment();
                                Bundle args = new Bundle();
                                args.putString("Place", location);
                                fragment.setArguments(args);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

        return v;
    }








    class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... args) {
            String xml = Weather_Content.excuteGet("https://api.weatherbit.io/v2.0/current?city=" + args[0] +
                    "&units=imperial&key=" + WEATHER_MAP_API);
            return xml;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if(json != null) {
                    String iconId;
                    JSONObject day = json.getJSONArray("data").getJSONObject(0);
                    JSONObject weather = day.getJSONObject("weather");


                    iconId = weather.getString("icon");
                    updatedField.setText(day.getString("ob_time"));
                    int imageResource = getResources().getIdentifier(("@drawable/"+iconId),null, getActivity().getPackageName());
                    imageView.setImageResource(imageResource);


                    detailsField.setText(weather.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.2f", day.getDouble("temp")) + "°");
                    pressure_field.setText("Pressure: " + day.getDouble("pres") );

                    cityField.setText(day.getString("city_name")+", "+ day.getString("country_code"));



                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }




    }





}
