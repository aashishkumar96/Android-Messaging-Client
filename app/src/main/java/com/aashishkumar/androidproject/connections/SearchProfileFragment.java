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
 * A simple {@link Fragment} subclass.
 * Show the connection profile of a search result
 *
 * @author Hien Doan
 */
public class SearchProfileFragment extends Fragment {

    private OnSearchProfileFragmentInteractionListener mListener;

    public SearchProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_profile, container, false);
        Button addFriend = (Button) v.findViewById(R.id.button_add_searchprofile_frag);
        addFriend.setOnClickListener(view ->mListener.onSendRequestClicked());
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            // Get the username from search list and set to this fragment
            String username = getArguments().getString("username");
            TextView tv = getActivity().findViewById(R.id.username_text_searchprofile_frag);
            tv.setText(username);

            // Get the first name from search list and set to this fragment
            String fname = getArguments().getString("fname");
            TextView tv1 = getActivity().findViewById(R.id.fname_text_searchprofile_frag);
            tv1.setText(fname);

            // Get the last name from search list and set to this fragment
            String lname = getArguments().getString("lname");
            TextView tv2 = getActivity().findViewById(R.id.lname_text_searchprofile_frag);
            tv2.setText(lname);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchProfileFragmentInteractionListener) {
            mListener = (OnSearchProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchProfileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSearchProfileFragmentInteractionListener {
        void onSendRequestClicked();
    }
}
