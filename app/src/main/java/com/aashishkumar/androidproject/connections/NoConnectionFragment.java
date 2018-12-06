package com.aashishkumar.androidproject.connections;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aashishkumar.androidproject.R;


/**
 * A simple {@link Fragment} subclass.
 * This fragment is loaded when there is no connection
 *
 * @author Hien Doan
 */
public class NoConnectionFragment extends Fragment {

    private OnNoConnectionFragmentInteractionListener mListener;

    public NoConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_no_connection, container, false);
        Button add = (Button) v.findViewById(R.id.add_friend_button);
        add.setOnClickListener(view ->mListener.onAddFriendClicked());
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoConnectionFragmentInteractionListener) {
            mListener = (OnNoConnectionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoConnectionFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNoConnectionFragmentInteractionListener {
        void onAddFriendClicked();
    }
}
