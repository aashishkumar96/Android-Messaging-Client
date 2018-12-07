package com.aashishkumar.androidproject.models;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * This class represents a Connection content: username, email,
 * first and last name, memberID, verified status and chatID
 *
 * @author Hien Doan
 */
public class Connection implements Serializable {

    private final String mUsername;
    private final String mEmail;
    private final String mFname;
    private final String mLname;
    private final int mMemID;
    private int mVerified;
    private final String mChatID;

    /**
     * Helper class for building Connection.
     *
     * @author Hien Doan
     */
    public static class Builder {
        private String mUsername = "";
        private String mEmail = "";
        private String mFname = "";
        private String mLname = "";
        private int mMemID = 0;
        private int mVerified = 0;
        private String mChatID = "";


        /**
         * Constructs a new Builder.
         *
         * @param username the username of connection
         */
        public Builder(String username) {
            this.mUsername = username;
        }

        /**
         * A method to add username to this connection
         *
         * @param username is the username of this connection that's added
         */
        public Builder addUsername(final String username) {
            mUsername = username;
            return this;
        }

        /**
         * A method to add email to this connection
         *
         * @param email is the email of this connection that's added
         */
        public Builder addEmail(final String email) {
            mEmail = email;
            return this;
        }

        /**
         * A method to add first name to this connection
         *
         * @param fname is the first name of this connection that's added
         */
        public Builder addFirstName(final String fname) {
            mFname = fname;
            return this;
        }

        /**
         * A method to add last name to this connection
         *
         * @param lname is the last name of this connection that's added
         */
        public Builder addLastName(final String lname) {
            mLname = lname;
            return this;
        }

        /**
         * A method to add member id to this connection
         *
         * @param memID is the member id of this connection that's added
         */
        public Builder addID(final int memID) {
            mMemID = memID;
            return this;
        }

        /**
         * A method to add verified status to this connection
         *
         * @param verified is the verified status of this connection that's added
         */
        public Builder addVerified(final int verified) {
            mVerified = verified;
            return this;
        }

        /**
         * A method to add chat id to this connection
         *
         * @param chatID is the chat id of this connection that's added
         */
        public Builder addChatID(final String chatID) {
            mChatID = chatID;
            return this;
        }

        /**
         * Let's build this connection
         */
        public Connection build() {
            return new Connection(this);
        }
    }

    /**
     * Construct a Connection internally from a builder.
     *
     * @param builder the builder used to construct this object
     */
    private Connection(final Builder builder) {
        this.mUsername = builder.mUsername;
        this.mEmail = builder.mEmail;
        this.mFname = builder.mFname;
        this.mLname = builder.mLname;
        this.mMemID = builder.mMemID;
        this.mVerified = builder.mVerified;
        this.mChatID = builder.mChatID;
    }

    /**
     * A method to get the username.
     *
     * @return the username of this Connection
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * A method to get the email.
     *
     * @return the email of this Connection
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * A method to get the first name.
     *
     * @return the first name of this Connection
     */
    public String getFirstName() {
        return mFname;
    }

    /**
     * A method to get the last name.
     *
     * @return the last name of this Connection
     */
    public String getLastName() {
        return mLname;
    }

    /**
     * A method to get the member id.
     *
     * @return the member id of this Connection
     */
    public int getMemID() {
        return mMemID;
    }

    /**
     * A method to get the full name.
     *
     * @return the full name of this Connection
     */
    public String getFullName() { return mFname + " " + mLname; }

    /**
     * A method to get the verified status.
     *
     * @return the verified status of this Connection
     */
    public int getVerified() { return mVerified; }

    /**
     * A method to get the chat id.
     *
     * @return the chat id of this Connection
     */
    public String getChatID() { return mChatID; }
}
