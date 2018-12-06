package com.aashishkumar.androidproject.chats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aashishkumar.androidproject.R;
import com.aashishkumar.androidproject.chats.MessageFragment;
import com.aashishkumar.androidproject.connections.ConnectionOnChatFragment;
import com.aashishkumar.androidproject.models.Connection;
import com.aashishkumar.androidproject.utils.MyFirebaseMessagingService;
import com.aashishkumar.androidproject.utils.SendPostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * A class that represents the chat window
 *
 * @author Hien Doan
 */
public class ChatWindowFragment extends Fragment {

    private static final String TAG = "CHAT_WINDOW_FRAG";

    private int mChatID;

    private TextView mMessageOutputTextView;
    private EditText mMessageInputEditText;

    private String mUsername;
    private String mUserID;

//    private String mAddToChat;
    private String mLeaveChat;
    private String mSendUrl;
    private String mGetAllMsg;

//    private final Fragment mMsgFrag = new MessageFragment();
    private FirebaseMessageReciever mFirebaseMessageReciever;


    public ChatWindowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_chat_window, container, false);

        mMessageOutputTextView = rootLayout.findViewById(R.id.text_chat_messge_display);
        mMessageInputEditText = rootLayout.findViewById(R.id.edit_chat_message_input);
        rootLayout.findViewById(R.id.button_chat_send_message).setOnClickListener(this::handleSendClick);

        rootLayout.findViewById(R.id.button_addToChat_chatwindow_frag).setOnClickListener(this::viewFriends);
        rootLayout.findViewById(R.id.button_leaveChat_chatwindow_frag).setOnClickListener(this::onLeaveChatClicked);

        return rootLayout;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.text_chat_messge_display, mMsgFrag).commit();
//    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the chat name from previous chat list and set to this chat window
        String chatName = getArguments().getString("chatname");
        TextView tv = getActivity().findViewById(R.id.text_chatname_chatwindow_frag);
        tv.setText(chatName);

        // Get the chat id, username and user id of the msg sender
        mChatID = getArguments().getInt("chatid");
        //Log.e("chatid is: ", Integer.toString(mChatID));
        mUsername = getArguments().getString("username_self");
        mUserID = getArguments().getString("id_self");

        //We will use this url every time the user hits send. Let's only build it once, ya?
        mSendUrl = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_send))
                .build()
                .toString();

        // build the leave chat url
        mLeaveChat = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_leaveChat))
                .build()
                .toString();

        // build the get all msg url
        mGetAllMsg = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging))
                .appendPath(getString(R.string.ep_getAll))
                .appendQueryParameter("chatId", Integer.toString(mChatID))
                .build()
                .toString();


        JSONObject msg = new JSONObject();
        try {
            msg.put("chatid", mChatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(mGetAllMsg, msg)
                .onPostExecute(this::handleGetAllMsgOnPostExecute)
                .onCancelled(error ->Log.e(TAG, error))
                .build().execute();

    }

    /**
     * Handle the send message button
     *
     * @param theButton is the view
     */
    private void handleSendClick(final View theButton) {
        String msg = mMessageInputEditText.getText().toString();

        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("id_self", mUserID);
            messageJson.put("message", msg);
            messageJson.put("chatid", mChatID);
            messageJson.put("username_self", mUsername);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(mSendUrl, messageJson)
                .onPostExecute(this::endOfSendMsgTask)
                .onCancelled(error ->Log.e(TAG, error))
                .build().execute();
    }

    /**
     * Handle the post request on send message
     * @param result is the result from web service
     */
    private void endOfSendMsgTask(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);

            if(res.has("success")  &&res.getBoolean("success")) {
                //The web service got our message. Time to clear out the input EditText
                mMessageInputEditText.setText("");

                //its up to you to decide if you want to send the message to the output here
                //or wait for the message to come back from the web service.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hanlde the add friend to chat button by open list of connection to select.
     * Only verified connections are shown in this list
     *
     * @param theButton is the view
     */
    private void viewFriends(final View theButton) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connections))
                .appendPath(getString(R.string.ep_viewfriends))
                .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("id_self", mUserID);
        } catch (JSONException e) {
            Log.wtf("ERROR! ", e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::getFriendList)
                .onCancelled(error ->Log.e(TAG, error))
                .build().execute();
    }

    /**
     * Handle the post request to show the verified friend list
     *
     * @param result is the result from web service
     */
    private void getFriendList(String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("result");

            List<Connection> connections = new ArrayList<>();

            for(int i = 0; i < data.length(); i++) {
                JSONObject jsonConnection = data.getJSONObject(i);
                if (jsonConnection.getInt("verified") == 1) {
                    connections.add(new Connection.Builder(jsonConnection.getString("username"))
                            .addFirstName(jsonConnection.getString("firstname"))
                            .addLastName(jsonConnection.getString("lastname"))
                            .addID(jsonConnection.getInt("memberid"))
                            .addChatID(Integer.toString(mChatID))
                            .build());
                }
            }

            Connection[] connectionsArray = new Connection[connections.size()];
            connectionsArray = connections.toArray(connectionsArray);

            Bundle args = new Bundle();
            args.putSerializable(ConnectionOnChatFragment.ARG_CONNECTION_ON_CHAT_LIST, connectionsArray);
            Fragment frag = new ConnectionOnChatFragment();
            frag.setArguments(args);

            // load the list of verified connection to add to chat
            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_home, frag)
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

    /**
     * Handle the leave chat button
     *
     * @param theButton is the view
     */
    private void onLeaveChatClicked(final View theButton) {
        JSONObject messageJson = new JSONObject();

        try {
            messageJson.put("chatid", mChatID);
            messageJson.put("id_self", mUserID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(mLeaveChat, messageJson)
                .onPostExecute(this::handleLeaveChatOnPost)
                .onCancelled(error ->Log.e(TAG, error))
                .build().execute();
    }

    /**
     * Handle the post request on leave chat
     *
     * @param result is the result from web service
     */
    private void handleLeaveChatOnPost(final String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Toast.makeText(getActivity(), "Successfully leave this chat room", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Cannot leave this chat room yet!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR!", e.getMessage());
        }
    }

    private void handleGetAllMsgOnPostExecute(final String result) {
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
//            mMsgFrag.setArguments(args);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }




    /**
     * A BroadcastReceiver setup to listen for messages sent from MyFirebaseMessagingService
     * that Android allows to run all the time.
     */
    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("FCM Chat Frag", "start onRecieve");
            if(intent.hasExtra("DATA")) {

                String data = intent.getStringExtra("DATA");
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(data);
                    if(jObj.has("message") &&jObj.has("sender")) {

                        String sender = jObj.getString("sender");
                        String msg = jObj.getString("message");

                        mMessageOutputTextView.append(sender + ": " + msg);
                        mMessageOutputTextView.append(System.lineSeparator());
                        mMessageOutputTextView.append(System.lineSeparator());
                        Log.i("FCM Chat Frag", sender + " " + msg);
                    }
                } catch (JSONException e) {
                    Log.e("JSON PARSE", e.toString());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirebaseMessageReciever == null) {
            mFirebaseMessageReciever = new FirebaseMessageReciever();
        }
        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.RECEIVED_NEW_MESSAGE);
        getActivity().registerReceiver(mFirebaseMessageReciever, iFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFirebaseMessageReciever != null){
            getActivity().unregisterReceiver(mFirebaseMessageReciever);
        }
    }
}
