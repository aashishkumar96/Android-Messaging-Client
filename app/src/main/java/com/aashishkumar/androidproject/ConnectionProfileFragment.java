package com.aashishkumar.androidproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionProfileFragment extends Fragment {


    public ConnectionProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            String username = getArguments().getString("username");
            TextView tv = getActivity().findViewById(R.id.username_text_profile_frag);
            tv.setText(username);

            String fname = getArguments().getString("fname");
            TextView tv1 = getActivity().findViewById(R.id.fname_text_profile_frag);
            tv1.setText(fname);

            String lname = getArguments().getString("lname");
            TextView tv2 = getActivity().findViewById(R.id.lname_text_profile_frag);
            tv2.setText(lname);

        }
    }



}
