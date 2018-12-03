package com.aashishkumar.androidproject.models;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class Chat implements Serializable {

    private final String mChatName;
    private final int mChatID;

    /**
     * Helper class for building Credentials.
     *
     */
    public static class Builder {
        private String mChatName = "";
        private int mChatID = 0;


        /**
         * Constructs a new Builder.
         *
         * @param chatName the username of connection
         */
        public Builder(String chatName) {
            this.mChatName = chatName;
        }

        public Builder addChatName(final String val) {
            mChatName = val;
            return this;
        }

        public Builder addChatId(final int val) {
            mChatID = val;
            return this;
        }


        public Chat build() {
            return new Chat(this);
        }
    }

    private Chat(final Builder builder) {
        this.mChatName = builder.mChatName;
        this.mChatID = builder.mChatID;
    }

    public String getChatName() {
        return mChatName;
    }

    public int getChatID() {
        return mChatID;
    }

}
