package com.aashishkumar.androidproject.models;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * This class represents the Chat content: chat name and chat id
 *
 * @author Hien Doan
 */
public class Chat implements Serializable {

    private final String mChatName;
    private final int mChatID;

    /**
     * Helper class for building Chat.
     *
     * @author Hien Doan
     */
    public static class Builder {
        private String mChatName = "";
        private int mChatID = 0;

        /**
         * Constructs a new Builder.
         *
         * @param chatName the name of this chat
         */
        public Builder(String chatName) {
            this.mChatName = chatName;
        }

        /**
         * A method to add chat name
         *
         * @param name is the name of this chat that's added
         */
        public Builder addChatName(final String name) {
            mChatName = name;
            return this;
        }

        /**
         * A method to add chat id
         *
         * @param id is the id of this chat that's added
         */
        public Builder addChatId(final int id) {
            mChatID = id;
            return this;
        }

        /**
         * Let's build this chat
         */
        public Chat build() {
            return new Chat(this);
        }
    }

    /**
     * Construct a Chat internally from a builder.
     *
     * @param builder the builder used to construct this object
     */
    private Chat(final Builder builder) {
        this.mChatName = builder.mChatName;
        this.mChatID = builder.mChatID;
    }

    /**
     * A method to get the chat name.
     *
     * @return the name of this Chat
     */
    public String getChatName() {
        return mChatName;
    }

    /**
     * A method to get the chat id.
     *
     * @return the id of this Chat
     */
    public int getChatID() {
        return mChatID;
    }

}
