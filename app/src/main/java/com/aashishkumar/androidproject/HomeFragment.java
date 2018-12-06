package com.aashishkumar.androidproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Fragment to display the home page of this app.
 *
 * @author Robert Bohlman
 * @author Hien Doan
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Display the email/username used to login
        if (getArguments() != null) {
            String username = getArguments().getString("username");
            TextView tv = getActivity().findViewById(R.id.textViewEmail);
            tv.setText(username);
        }
    }

}
