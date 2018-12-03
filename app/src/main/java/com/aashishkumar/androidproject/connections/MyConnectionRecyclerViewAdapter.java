package com.aashishkumar.androidproject.connections;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aashishkumar.androidproject.R;
import com.aashishkumar.androidproject.connections.ConnectionFragment.OnConnectionFragmentInteractionListener;
import com.aashishkumar.androidproject.models.Connection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Connection} and makes a call to the
 * specified {@link OnConnectionFragmentInteractionListener}.
 */
public class MyConnectionRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionRecyclerViewAdapter.ViewHolder> {

    private final List<Connection> mValues;
    private final OnConnectionFragmentInteractionListener mListener;

    public MyConnectionRecyclerViewAdapter(List<Connection> items, OnConnectionFragmentInteractionListener listener) {
        mValues = items;
        Collections.sort(mValues, new Comparator<Connection>() {
            @Override
            public int compare(Connection o1, Connection o2) {
                return o2.getVerified() - o1.getVerified();
            }
        });
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mFullName.setText(mValues.get(position).getFullName());

        if (mValues.get(position).getVerified() == 0) {
            holder.mUsername.setText(mValues.get(position).getUsername() + " [UNVERIFIED]");
            holder.mUsername.setTextColor(Color.GRAY);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onConnectionListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsername;
        public final TextView mFullName;
        public Connection mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = (TextView) view.findViewById(R.id.username_connection_frag);
            mFullName = (TextView) view.findViewById(R.id.fullname_connection_frag);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsername.getText() + "'";
        }
    }
}
