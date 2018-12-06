package com.aashishkumar.androidproject.chats;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aashishkumar.androidproject.R;
import com.aashishkumar.androidproject.models.ChatMessage;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ChatMessage} and makes a call to the
 * specified {@link MessageFragment.OnChatMessageListFragmentInteractionListener}.
 *
 * @author Hien Doan
 */
public class MyMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {

    private final List<ChatMessage> mValues;
    private final MessageFragment.OnChatMessageListFragmentInteractionListener mListener;

    public MyMessageRecyclerViewAdapter(List<ChatMessage> items, MessageFragment.OnChatMessageListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUsername.setText(mValues.get(position).getUserName());
        holder.mContentView.setText(mValues.get(position).getMsg());

//        if (mValues.get(position).getUserName().equals()) {
//            holder.mUsername.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//            holder.mUsername.setTextColor(Color.CYAN);
//            //holder.mContentView.setForegroundGravity(View.);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
//            params.gravity = Gravity.RIGHT;
//            holder.mContentView.setLayoutParams(params);
//
//        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onChatMessageListFragmentInteraction(holder.mItem);
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
        public final TextView mContentView;
        public ChatMessage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = (TextView) view.findViewById(R.id.text_username_message_frag);
            mContentView = (TextView) view.findViewById(R.id.text_content_message_frag);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsername.getText() + "'";
        }
    }
}
