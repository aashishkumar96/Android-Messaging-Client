package com.aashishkumar.androidproject.models;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * This class represents Chat message content: username of sender, message and timestamp
 *
 */
public class ChatMessage implements Serializable {
    private final String mUsername;
    private final String mMessage;
    private final String mTimeStamp;

    /**
     * Helper class for building ChatMessage.
     *
     * @author Hien Doan
     */
    public static class Builder {
        private String mUsername = "";
        private String mMessage = "";
        private String mTimeStamp = "";


        /**
         * Constructs a new ChatMessage.
         *
         * @param username the username of this message
         */
        public Builder(String username) {
            this.mUsername = username;
        }

        /**
         * A method to add username
         *
         * @param userName is the name of this message that's added
         */
        public Builder addUserName(final String userName) {
            mUsername = userName;
            return this;
        }

        /**
         * A method to add message
         *
         * @param message is the message of this message that's added
         */
        public Builder addMessage(final String message) {
            mMessage = message;
            return this;
        }

        /**
         * A method to add message
         *
         * @param time is the timestamp of this message that's added
         */
        public Builder addTimeStamp(final String time) {
            mTimeStamp = time;
            return this;
        }

        /**
         * Let's build this message
         */
        public ChatMessage build() {
            return new ChatMessage(this);
        }
    }

    /**
     * Construct a ChatMessage internally from a builder.
     *
     * @param builder the builder used to construct this object
     */
    private ChatMessage(final Builder builder) {
        this.mUsername = builder.mUsername;
        this.mMessage = builder.mMessage;
        this.mTimeStamp = builder.mTimeStamp;
    }

    /**
     * A method to get the username.
     *
     * @return the username
     */
    public String getUserName() {
        return mUsername;
    }

    /**
     * A method to get the message.
     *
     * @return the message
     */
    public String getMsg() {
        return mMessage;
    }

    /**
     * A method to get the timestamp.
     *
     * @return the timestamp
     */
    public String getTimeStamp() { return mTimeStamp; }
}
