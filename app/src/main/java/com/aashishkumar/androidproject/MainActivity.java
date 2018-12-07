package com.aashishkumar.androidproject;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aashishkumar.androidproject.model.Credentials;


/**
 * Main activity - handle login and register
 *
 * @author Aayush Shah
 * @author Robert Bohlman
 * @author Hien Doan
 */
public class MainActivity extends AppCompatActivity
        implements LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        Verification.OnVerificationFragmentInteractionListener {

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

    /* Once login successfully, start HomeActivity
     *
     * @param mCredentials is the credential from login
     * @param id is the member id get from login
     */
    @Override
    public void onLoginSuccess(Credentials mCredentials, String id) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("email", mCredentials.getEmail());
        intent.putExtra("id", id);
        MainActivity.this.startActivity(intent);
        finish();
    }


    /* Once login successfully, start HomeActivity
     *
     * @param mCredentials is the credential from login
     * @param username is the username get from login
     * @param id is the member id get from login
     */
    @Override
    public void onLoginSuccess(Credentials mCredentials, String username, String id) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("email", mCredentials.getEmail());
        intent.putExtra("username", username);
        intent.putExtra("id", id);
        MainActivity.this.startActivity(intent);
        finish();
    }

    /**
     * Once register button is clicked in LoginFragment,
     * go to the RegisterFragment.
     */
    @Override
    public void onRegisterClicked() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main, new RegisterFragment())
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * Successfully registered,
     * go to the verification frag.
     */
    @Override
    public void onRegisterSuccess(Credentials mCredentials,String email) {
        Bundle bundle=new Bundle();
        bundle.putString("email",email);
        Verification mverification = new Verification();
        mverification.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main,mverification)
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * Load and display the wait fragment
     */
    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Hide the wait fragment
     */
    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }



    @Override
    public void verifiedUserSendToSuccess(String email) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("email", email);
     //   intent.putExtra("id", id);
        MainActivity.this.startActivity(intent);
        finish();
    }


}
