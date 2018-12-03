package com.aashishkumar.androidproject;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aashishkumar.androidproject.model.Credentials;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if (findViewById(R.id.frame_main) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void onLoginSuccess(Credentials mCredentials, String username, String id) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("email", mCredentials.getEmail());
        intent.putExtra("username", username);
        intent.putExtra("id", id);
        MainActivity.this.startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterClicked() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, new RegisterFragment())
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onRegisterSuccess(Credentials mCredentials) {

    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }
}
