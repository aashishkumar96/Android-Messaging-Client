package com.aashishkumar.androidproject.connections;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aashishkumar.androidproject.R;


/**
 * A fragment to show the profile of a connection that already confirmed,
 * which means we can start chat with them, view chat list or remove them
 * from our confirmed connection list.
 *
 * A simple {@link Fragment} subclass.
 *
 * @author Hien Doan
 */
public class ConnectionProfileFragment extends Fragment {

    private OnConectionProfileFragmentInteractionListener mListener;

    public ConnectionProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_connection_profile, container, false);

        // To get the input chat name
        EditText inputName = (EditText) v.findViewById(R.id.chatname_profile_frag);

        // Set the chatname and set click listener on start new chat button
        Button newChat = (Button) v.findViewById(R.id.button_chat_profile_frag);
        newChat.setOnClickListener(view ->mListener.onNewChatClicked(inputName.getText().toString()));

        // Set click listener on view chat list button
        Button viewChatList = (Button) v.findViewById(R.id.button_view_chatList_profile_frag);
        viewChatList.setOnClickListener(view ->mListener.onViewChatListClicked());

        // Set click listener on remove friend button
        Button remove = (Button) v.findViewById(R.id.button_remove_profile_frag);
        remove.setOnClickListener(view ->mListener.onRemoveClicked());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {

            // Get the username from connection list and set to this fragment
            String username = getArguments().getString("username");
            TextView tv = getActivity().findViewById(R.id.username_text_profile_frag);
            tv.setText(username);

            // Get the first name from connection list and set to this fragment
            String fname = getArguments().getString("fname");
            TextView tv1 = getActivity().findViewById(R.id.fname_text_profile_frag);
            tv1.setText(fname);

            // Get the last name from connection list and set to this fragment
            String lname = getArguments().getString("lname");
            TextView tv2 = getActivity().findViewById(R.id.lname_text_profile_frag);
            tv2.setText(lname);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConectionProfileFragmentInteractionListener) {
            mListener = (OnConectionProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConectionProfileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnConectionProfileFragmentInteractionListener {
        void onNewChatClicked(String chatName);
        void onViewChatListClicked();
        void onRemoveClicked();
    }

}
