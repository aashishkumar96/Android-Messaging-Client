package com.aashishkumar.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is currently to load the home activity for testing purposes.
        //Remove once login is implemented.
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
