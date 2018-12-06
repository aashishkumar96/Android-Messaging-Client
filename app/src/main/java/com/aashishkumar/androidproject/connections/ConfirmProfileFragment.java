package com.aashishkumar.androidproject.connections;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aashishkumar.androidproject.R;


/**
 * A fragment to show the profile of a connection that needed to be confirmed/declined.
 * A simple {@link Fragment} subclass.
 *
 * @Author Hien Doan
 */
public class ConfirmProfileFragment extends Fragment {


    private OnConfirmProfileFragmentInteractionListener mListener;

    /*
     *  Required empty public constructor
     */
    public ConfirmProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_profile, container, false);

        // Get the confirm button and add click listener to it
        // Once the button is clicked, run the method onConfirmClicked
        Button confirm = (Button) v.findViewById(R.id.button_confirm_confirmprofile_frag);
        confirm.setOnClickListener(view ->mListener.onConfirmClicked());

        // Get the decline button and add click listener to it
        // Once the button is clicked, run the method onDeclineClicked
        Button decline = (Button) v.findViewById(R.id.button_decline_confirmprofile_frag);
        decline.setOnClickListener(view ->mListener.onDeclineClicked());

        // Return the view
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            // Get the username from the activity and display it
            String username = getArguments().getString("username");
            TextView tv = getActivity().findViewById(R.id.username_text_confirmprofile_frag);
            tv.setText(username);

            // Get the first name from the activity and display it
            String fname = getArguments().getString("fname");
            TextView tv1 = getActivity().findViewById(R.id.fname_text_confirmprofile_frag);
            tv1.setText(fname);

            // Get the last name from the activity and display it
            String lname = getArguments().getString("lname");
            TextView tv2 = getActivity().findViewById(R.id.lname_text_confirmprofile_frag);
            tv2.setText(lname);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfirmProfileFragmentInteractionListener) {
            mListener = (OnConfirmProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConfirmProfileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnConfirmProfileFragmentInteractionListener {
        void onConfirmClicked();
        void onDeclineClicked();
    }

}
