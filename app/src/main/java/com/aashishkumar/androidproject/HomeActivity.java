package com.aashishkumar.androidproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aashishkumar.androidproject.chats.MessageFragment;
import com.aashishkumar.androidproject.connections.ConnectionOnChatFragment;
import com.aashishkumar.androidproject.models.Chat;
import com.aashishkumar.androidproject.chats.ChatFragment;
import com.aashishkumar.androidproject.chats.ChatWindowFragment;
import com.aashishkumar.androidproject.connections.ConfirmProfileFragment;
import com.aashishkumar.androidproject.models.ChatMessage;
import com.aashishkumar.androidproject.models.Connection;
import com.aashishkumar.androidproject.connections.ConnectionFragment;
import com.aashishkumar.androidproject.connections.ConnectionProfileFragment;
import com.aashishkumar.androidproject.connections.NoConnectionFragment;
import com.aashishkumar.androidproject.connections.SearchConnectionFragment;
import com.aashishkumar.androidproject.connections.SearchProfileFragment;
import com.aashishkumar.androidproject.connections.SearchResultFragment;
import com.aashishkumar.androidproject.utils.SendPostAsyncTask;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Home Activity - handle the Connection/Chat functionality/interaction
 *
 * @author Hien Doan
 * @author Robert Bohlman
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        WaitFragment.OnFragmentInteractionListener,
        ConnectionFragment.OnConnectionFragmentInteractionListener,
        NoConnectionFragment.OnNoConnectionFragmentInteractionListener,
        ConnectionProfileFragment.OnConectionProfileFragmentInteractionListener,
        ConfirmProfileFragment.OnConfirmProfileFragmentInteractionListener,
        SearchConnectionFragment.OnSearchConnetionFragmentInteractionListener,
        SearchResultFragment.OnSearchListFragmentInteractionListener,
        SearchProfileFragment.OnSearchProfileFragmentInteractionListener,
        ChatFragment.OnChatListFragmentInteractionListener,
        ConnectionOnChatFragment.OnConnectionOnChatInteractionListener {

    private HomeFragment mHomeFragment;
    private int mMemberID;
    private String mMemberUsername;
    private int mFriendID;
    private String mFriendUsername;

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

        // Retrieve the user id and username from Main activity
        mMemberID = getIntent().getIntExtra("id", -999);
        mMemberUsername = getIntent().getStringExtra("username");

        if(savedInstanceState == null) {
            //Save the home fragment
            mHomeFragment = new HomeFragment();
            Bundle args = new Bundle();
            args.putString("username", mMemberUsername);
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
            logout(); // log out this app
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A helper method to logout from this app
     */
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

    /**
     * A method helps to load new fragment
     *
     * @param frag is the fragment that needed to load
     */
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
            viewFriend();
        } else if (id == R.id.nav_add_connection) {
            addConnection();
        } else if (id == R.id.nav_weather) {
            loadFragment(new WeatherFragment());
        } else if (id == R.id.nav_chats) {
            openChatList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void handleGetAllMsgOnPostExecute(final String result) {
//        try {
//            JSONObject root = new JSONObject(result);
//            JSONArray data = root.getJSONArray("messages");
//
//            List<ChatMessage> messages = new ArrayList<>();
//
//            for (int i = 0; i < data.length(); i++) {
//                JSONObject jsonMsg = data.getJSONObject(i);
//                messages.add(new ChatMessage.Builder(jsonMsg.getString("email"))
//                        .addMessage(jsonMsg.getString("message"))
//                        .build());
//            }
//
//            ChatMessage[] msgArray = new ChatMessage[messages.size()];
//            msgArray = messages.toArray(msgArray);
//
//            Bundle args = new Bundle();
//            args.putSerializable(MessageFragment.ARG_MESSAGE_LIST, msgArray);
//            Fragment frag = new MessageFragment();
//            frag.setArguments(args);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Build the url connect to webservice to get the chat list
     */
    private void openChatList() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_myChats))
                .build();

        JSONObject msg = new JSONObject();

        // add the require body for this post request
        try {
            msg.put("id_self", mMemberID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        // send post request for the chat list
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleChatListOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();
    }

    /**
     * Handle chatlist onPostExecute of the AsyncTask.
     * The result from our webservice is a JSON formatted String.
     * Parse it for success or failure.
     *
     * @param result the JSON formatted String response from the web service
     */
    private void handleChatListOnPostExecute(String result) {
        try {
            // GEt the result from web service
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("result");

            // Create a list to hold chat list
            List<Chat> chats = new ArrayList<>();

            if (data.length() == 0) {
                onWaitFragmentInteractionHide();
                Toast.makeText(this, "Empty Chat List!", Toast.LENGTH_SHORT).show();
            } else {
                // Get the chat list as JSONObject
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonChat = data.getJSONObject(i);
                    chats.add(new Chat.Builder(jsonChat.getString("name"))
                            .addChatId(jsonChat.getInt("chatid"))
                            .build());
                }


                Chat[] chatsArray = new Chat[chats.size()];
                chatsArray = chats.toArray(chatsArray);

                Bundle args = new Bundle();
                args.putSerializable(ChatFragment.ARG_CHAT_LIST, chatsArray);
                Fragment frag = new ChatFragment();
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

    /**
     * Handle the connection list on addToChat button of chat window
     *
     * @param item is the specific connection selected to add to this chat
     */
    @Override
    public void onConnectionOnChat(Connection item) {
        // build the url to webservice
        String addToChat = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_addToChat))
                .build()
                .toString();
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("chatid", item.getChatID());
            messageJson.put("id_friend", item.getMemID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(addToChat, messageJson)
                .onPostExecute(this::handleAddToChatOnPost)
                .onCancelled(this::handleErrorInTask)
                .build().execute();
    }

    /**
     * HAndle the post request on add to chat button click
     *
     * @param result is the result from webservice
     */
    private void handleAddToChatOnPost(final String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Toast.makeText(this,"Successfully added to this chat!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Cannot leave this chat room yet!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR!", e.getMessage());
        }
    }
    /**
     * Go to SearchConnectonFragment to search for the connnection
     */
    private void addConnection() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home, new SearchConnectionFragment())
                .addToBackStack(null);
        transaction.commit();
    }

    /**
     * Build the url connect to webservice to get the list of connections
     */
    private void viewFriend() {
        // Build the url connects to webservice to view friend list
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_viewfriends))
                .build();

        JSONObject msg = new JSONObject();

        // Add the request body for this post request
        try {
            msg.put("id_self", mMemberID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        // Build the post request with JSONObject
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConnectionOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    /**
     * Handle connection onPostExecute of the AsyncTask.
     * The result from our webservice is a JSON formatted String.
     * Parse it for success or failure.
     *
     * @param result the JSON formatted String response from the web service
     */
    private void handleConnectionOnPostExecute(final String result) {

        try {
            // GEt the result from web service
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("result");

            // Create a list to hold connection list
            List<Connection> connections = new ArrayList<>();

            if (data.length() == 0) {
                Log.e("ERROR", "no connections");
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

    /**
     * Handle click from Connection list
     *
     * @param item is the specific Connection that's clicked
     */
    @Override
    public void onConnectionListFragmentInteraction(Connection item) {
        Bundle args = new Bundle();
        mFriendID = item.getMemID();
        mFriendUsername = item.getUsername();
        args.putString("username", mFriendUsername);
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

    /**
     * Handle the add friend button click
     */
    @Override
    public void onAddFriendClicked() {
        addConnection();
    }

    /**
     * Handle the start new chat button.
     * Build url link to web service and send post request
     *
     * @param chatName is the name of chat window entered by user
     */
    @Override
    public void onNewChatClicked(String chatName) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_startChat))
                .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mMemberID);
            msg.put("username_self", mMemberUsername);
            msg.put("id_friend", mFriendID);
            msg.put("username_friend", mFriendUsername);
            msg.put("chat_name", chatName);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleNewChatOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();
    }

    /**
     * Handle the result of post request for starting new chat
     *
     * @param result is the result from web service
     */
    private void handleNewChatOnPostExecute(final String result) {

        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");

            onWaitFragmentInteractionHide();
            if (success) {
                Toast.makeText(this, "Successfully create new chat!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error! Cannot create new chat!", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    /**
     * Handle the view chat list button
     */
    @Override
    public void onViewChatListClicked() {
        openChatList();
    }


    /**
     * Handle the click from Chat list
     *
     * @param item is the specific Chat window selected
     */
    @Override
    public void onChatListFragmentInteraction(Chat item) {
        Bundle args = new Bundle();
        String chatName = item.getChatName();
        int chatID = item.getChatID();
        args.putString("chatname", chatName);
        args.putInt("chatid", chatID);
        args.putString("username_self", mMemberUsername);
        args.putInt("id_self", mMemberID);
        args.putString("username_friend", mFriendUsername);
        args.putInt("id_friend", mFriendID);
        ChatWindowFragment frag = new ChatWindowFragment();
        frag.setArguments(args);
        loadFragment(frag);
    }

    /**
     * HAndle the remove friend button click
     */
    @Override
    public void onRemoveClicked() {
        // Build the url that connects to web service
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_removefriend))
                .build();


        // Add the require body
        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mMemberID);
            msg.put("id_friend", mFriendID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
            onWaitFragmentInteractionHide();
        }

        // Send the post request
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleRemoveFriendOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    /**
     * Handle the post request on remove friend
     *
     * @param result is the result from webservice
     */
    private void handleRemoveFriendOnPostExecute(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Toast.makeText(this, "Successfully removed!", Toast.LENGTH_SHORT).show();
                viewFriend();
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
    public void onFragmentInteraction() { }

    /**
     * Handle the click from Chat list
     *
     * @param item is the specific Chat that selected
     */
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

        loadFragment(frag);
    }

    /**
     * Handle the send request button click
     */
    @Override
    public void onSendRequestClicked() {

        // Build the url that connects to webservice
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_addfriend))
                .build();

        JSONObject msg = new JSONObject();

        // Add the require body
        try {
            msg.put("id_self", mMemberID);
            msg.put("id_friend", mFriendID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        // Build the post request with the require body
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleSendRequestOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    /**
     * Handle the send friend request on post request
     *
     * @param result is the result of post request
     */
    private void handleSendRequestOnPostExecute(String result) {
        try {
            // Get the result from web service
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Toast.makeText(this, "Friend request sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Friend request already sent!", Toast.LENGTH_SHORT).show();
            }
            onWaitFragmentInteractionHide();

        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    /**
     * Method to handle the confirm button clicked
     */
    @Override
    public void onConfirmClicked() {
        // Build the url that connect to web service
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_confirmfriend))
                .build();


        JSONObject msg = new JSONObject();

        // Attach the require body for this post request
        try {
            msg.put("id_self", mMemberID);
            msg.put("id_friend", mFriendID);
        } catch (JSONException e) {
            Log.wtf("ERROR! Confirmfriend ", e.getMessage());
            onWaitFragmentInteractionHide();
        }

        // Build this post request
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleConfirmFriendOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();

    }

    /**
     * Handle the confirm friend on post request
     *
     * @param result is the result of post request
     */
    private void handleConfirmFriendOnPostExecute(String result) {
        try {
            // Get the result from web service
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

    /**
     * Handle the decline button
     */
    @Override
    public void onDeclineClicked() {
        onRemoveClicked();
    }

    /**
     * Show progress circle
     */
    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_home, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Hide progress circle
     */
    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    /**
     * Helper class to handle error in post request
     *
     * @param result is the error
     */
    private void handleErrorInTask(String result) {
        Log.e("ERROR!", result);
    }
}
