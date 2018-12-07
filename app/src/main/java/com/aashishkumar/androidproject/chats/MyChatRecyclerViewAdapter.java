package com.aashishkumar.androidproject.chats;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aashishkumar.androidproject.R;
import com.aashishkumar.androidproject.models.Chat;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Chat} and makes a call to the
 * specified {@link ChatFragment.OnChatListFragmentInteractionListener}.
 *
 * @author Hien Doan
 */
public class MyChatRecyclerViewAdapter extends RecyclerView.Adapter<MyChatRecyclerViewAdapter.ViewHolder> {

    private final List<Chat> mValues;
    private final ChatFragment.OnChatListFragmentInteractionListener mListener;

    public MyChatRecyclerViewAdapter(List<Chat> items, ChatFragment.OnChatListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mChatName.setText(mValues.get(position).getChatName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onChatListFragmentInteraction(holder.mItem);
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
        public final TextView mChatName;
        public Chat mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mChatName = (TextView) view.findViewById(R.id.chatname_chat_fragment);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mChatName.getText() + "'";
        }
    }
}
