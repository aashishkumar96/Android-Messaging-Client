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
    private String mMemberID;
    private String mMemberUsername;
    private String mFriendID;
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
        mMemberID = getIntent().getStringExtra("id");
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

    @Override
    public void onConnectionOnChat(Connection item) {

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

    private void handleGetAllMsgOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("messages");

            List<ChatMessage> messages = new ArrayList<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonMsg = data.getJSONObject(i);
                messages.add(new ChatMessage.Builder(jsonMsg.getString("email"))
                        .addMessage(jsonMsg.getString("message"))
                        .build());
            }

            ChatMessage[] msgArray = new ChatMessage[messages.size()];
            msgArray = messages.toArray(msgArray);

            Bundle args = new Bundle();
            args.putSerializable(MessageFragment.ARG_MESSAGE_LIST, msgArray);
            Fragment frag = new MessageFragment();
            frag.setArguments(args);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openChatList() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_myChats))
                .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mMemberID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleChatListOnPostExecute)
                .onCancelled(this::handleErrorInTask)
                .build().execute();
    }

    private void handleChatListOnPostExecute(String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("result");

            List<Chat> chats = new ArrayList<>();

            if (data.length() == 0) {
                onWaitFragmentInteractionHide();
                Toast.makeText(this, "Empty Chat List!", Toast.LENGTH_SHORT).show();
            } else {

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

    private void addConnection() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home, new SearchConnectionFragment())
                .addToBackStack(null);
        transaction.commit();
    }

    private void viewFriend() {
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

    // Done
    private void handleConnectionOnPostExecute(final String result) {

        try {
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("result");

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

    // Done
    @Override
    public void onConnectionListFragmentInteraction(Connection item) {
        Bundle args = new Bundle();
        mFriendID = Integer.toString(item.getMemID());
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

    // Done
    @Override
    public void onAddFriendClicked() {
        addConnection();
    }

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

    //Done
    @Override
    public void onViewChatListClicked() {
        openChatList();
    }

    @Override
    public void onChatListFragmentInteraction(Chat item) {
        Bundle args = new Bundle();
        String chatName = item.getChatName();
        int chatID = item.getChatID();
        args.putString("chatname", chatName);
        args.putInt("chatid", chatID);
        args.putString("username_self", mMemberUsername);
        args.putString("id_self", mMemberID);
        args.putString("username_friend", mFriendUsername);
        args.putString("id_friend", mFriendID);
        ChatWindowFragment frag = new ChatWindowFragment();
        frag.setArguments(args);
        loadFragment(frag);
    }

    // Done
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

    // Done
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
    public void onFragmentInteraction() {

    }

    // Done
    @Override
    public void onSearchListFragmentInteraction(Connection item) {
        // Get the memberid of the connection that you've searched
        // for later use (to add friend).
        mFriendID = Integer.toString(item.getMemID());

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

    // Done
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

    // Done
    private void handleSendRequestOnPostExecute(String result) {
        try {

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

    // Done
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

    // Done
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

    // Done
    @Override
    public void onDeclineClicked() {
        onRemoveClicked();
    }

    // Done
    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_home, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    // Done
    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    // Done
    private void handleErrorInTask(String result) {
        Log.e("ERROR!", result);
    }
}
