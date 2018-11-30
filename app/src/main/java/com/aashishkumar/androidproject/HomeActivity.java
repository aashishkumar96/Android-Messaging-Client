package com.aashishkumar.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aashishkumar.androidproject.connections.Connection;
import com.aashishkumar.androidproject.utils.SendPostAsyncTask;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   WaitFragment.OnFragmentInteractionListener,
                   ConnectionOptionFragment.OnConnectionOptionFragmentInteractionListener,
                   ConnectionFragment.OnConnectionFragmentInteractionListener,
                   NoConnectionFragment.OnNoConnectionFragmentInteractionListener,
                   ConnectionProfileFragment.OnConectionProfileFragmentInteractionListener,
                   ConfirmProfileFragment.OnConfirmProfileFragmentInteractionListener,
                   SearchConnectionFragment.OnSearchConnetionFragmentInteractionListener,
                   SearchResultFragment.OnSearchListFragmentInteractionListener,
                   SearchProfileFragment.OnSearchProfileFragmentInteractionListener {

    private HomeFragment mHomeFragment;
    private int mMemberID;
    private int mFriendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            //Save the home fragment
            mHomeFragment = new HomeFragment();
            // Get the email used to login
            Intent intent = getIntent();
            String emailAdd = intent.getStringExtra("email");
            mMemberID = intent.getIntExtra("id", 0);
            Bundle args = new Bundle();
            args.putString("emailAdd", emailAdd);
            mHomeFragment.setArguments(args);

            if (findViewById(R.id.activity_home) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.activity_home, mHomeFragment)
                        .commit();
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();

        new DeleteTokenAsyncTask().execute();

    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home, frag)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            loadFragment(mHomeFragment);
        } else if (id == R.id.nav_connections) {
            loadFragment(new ConnectionOptionFragment());
        } else if (id == R.id.nav_weather) {
            loadFragment(new WeatherFragment());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSearchClicked() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home, new SearchConnectionFragment())
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onViewFriendsClicked() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_viewfriends))
                .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mMemberID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    private void handleConnectionOnPostExecute(final String result) {

        try {
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("result");

            List<Connection> connections = new ArrayList<>();

            if (data.length() == 0) {
                //Log.e("ERROR", "no connections");
                onWaitFragmentInteractionHide();
                loadFragment(new NoConnectionFragment());
            } else {

                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonConnection = data.getJSONObject(i);
                    connections.add(new Connection.Builder(jsonConnection.getString("username"))
                        .addFirstName(jsonConnection.getString("firstname"))
                        .addLastName(jsonConnection.getString("lastname"))
                        .addVerified(jsonConnection.getInt("verified"))
                        .addID(jsonConnection.getInt("memberid"))
                        .build());
                }

                Connection[] connectionsArray = new Connection[connections.size()];
                connectionsArray = connections.toArray(connectionsArray);

                Bundle args = new Bundle();
                args.putSerializable(ConnectionFragment.ARG_CONNECTION_LIST, connectionsArray);
                Fragment frag = new ConnectionFragment();
                frag.setArguments(args);
                onWaitFragmentInteractionHide();
                loadFragment(frag);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void handleErrorInTask(String result) {
        Log.e("ERROR!", result);
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_home, new WaitFragment(), "WAIT")
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

    @Override
    public void onAddFriendClicked() {
        onSearchClicked();
    }

    @Override
    public void onConnectionListFragmentInteraction(Connection item) {
        Bundle args = new Bundle();
        mFriendID = item.getMemID();
        args.putString("username", item.getUsername());
        args.putString("fname", item.getFirstName());
        args.putString("lname", item.getLastName());
        int isVerified = item.getVerified();
        if (isVerified == 0) {
            ConfirmProfileFragment confirmProfileFragment = new ConfirmProfileFragment();
            confirmProfileFragment.setArguments(args);
            loadFragment(confirmProfileFragment);
        } else {
            ConnectionProfileFragment profileFragment = new ConnectionProfileFragment();
            profileFragment.setArguments(args);
            loadFragment(profileFragment);
        }
    }

    @Override
    public void onChatClicked() {
        //Update later
    }

    @Override
    public void onRemoveClicked() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_removefriend))
                .build();


        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mMemberID);
            msg.put("id_friend", mFriendID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
            onWaitFragmentInteractionHide();
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleRemoveFriendOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    private void handleRemoveFriendOnPostExecute(String result) {
        try {

            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Toast.makeText(this, "This connection is removed", Toast.LENGTH_SHORT).show();
                loadFragment(new ConnectionOptionFragment());
            } else {
                Toast.makeText(this, "Error! This connection can't be removed!", Toast.LENGTH_SHORT).show();
            }
            onWaitFragmentInteractionHide();

        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onSearchListFragmentInteraction(Connection item) {
        // Get the memberid of the connection that you've searched
        // for later use (to add friend).
        mFriendID = item.getMemID();

        Bundle args = new Bundle();
        args.putString("username", item.getUsername());
        args.putString("fname", item.getFirstName());
        args.putString("lname", item.getLastName());
        SearchProfileFragment frag = new SearchProfileFragment();
        frag.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home, frag)
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onSendRequestClicked() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_addfriend))
                .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mMemberID);
            msg.put("id_friend", mFriendID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleSendRequestOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    private void handleSendRequestOnPostExecute(String result) {
        try {

            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Toast.makeText(this, "Successfully send friend request", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error! Can't send friend request!", Toast.LENGTH_SHORT).show();
            }
            onWaitFragmentInteractionHide();

        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    // Deleting the InstanceId (Firebase token) must be done asynchronously. Good thing
// we have something that allows us to do that.
    class DeleteTokenAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onWaitFragmentInteractionShow();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //since we are already doing stuff in the background, go ahead
            //and remove the credentials from shared prefs here.
            SharedPreferences prefs =
                    getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
            prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
            try {
                //this call must be done asynchronously.
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                Log.e("FCM", "Delete error!");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finishAndRemoveTask();
        }
    }


    @Override
    public void onConfirmClicked() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_confirmfriend))
                .build();


        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mMemberID);
            msg.put("id_friend", mFriendID);
        } catch (JSONException e) {
            Log.wtf("ERROR! Confirmfriend ", e.getMessage());
            onWaitFragmentInteractionHide();
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConfirmFriendOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    private void handleConfirmFriendOnPostExecute(String result) {
        try {

            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Toast.makeText(this, "This connection is confirmed", Toast.LENGTH_SHORT).show();
                //loadFragment(new ConnectionOptionFragment());
            } else {
                Toast.makeText(this, "Error! This connection can't be confirmed!", Toast.LENGTH_SHORT).show();
            }
            onWaitFragmentInteractionHide();

        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }


    @Override
    public void onDeclineClicked() {
        onRemoveClicked();
    }
}
