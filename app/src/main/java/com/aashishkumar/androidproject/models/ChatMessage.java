package com.aashishkumar.androidproject.models;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class ChatMessage implements Serializable {
    private final String mUsername;
    private final String mMessage;
    private final String mTimeStamp;

    /**
     * Helper class for building Credentials.
     *
     */
    public static class Builder {
        private String mUsername = "";
        private String mMessage = "";
        private String mTimeStamp = "";


        /**
         * Constructs a new Builder.
         *
         * @param username the username of connection
         */
        public Builder(String username) {
            this.mUsername = username;
        }

        public Builder addUserName(final String val) {
            mUsername = val;
            return this;
        }

        public Builder addMessage(final String val) {
            mMessage = val;
            return this;
        }

        public Builder addTimeStamp(final String val) {
            mTimeStamp = val;
            return this;
        }


        public ChatMessage build() {
            return new ChatMessage(this);
        }
    }

    private ChatMessage(final Builder builder) {
        this.mUsername = builder.mUsername;
        this.mMessage = builder.mMessage;
        this.mTimeStamp = builder.mTimeStamp;
    }

    public String getUserName() {
        return mUsername;
    }

    public String getMsg() {
        return mMessage;
    }

    public String getTimeStamp() { return mTimeStamp; }
}
