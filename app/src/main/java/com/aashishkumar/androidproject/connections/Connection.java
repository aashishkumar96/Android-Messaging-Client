package com.aashishkumar.androidproject.connections;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class Connection implements Serializable {

    private final String mUsername;
    private final String mEmail;
    private final String mFname;
    private final String mLname;
    private final String mMemID;

    /**
     * Helper class for building Credentials.
     *
     */
    public static class Builder {
        private String mUsername = "";
        private String mEmail = "";
        private String mFname = "";
        private String mLname = "";
        private String mMemID = "";


        /**
         * Constructs a new Builder.
         *
         * @param username the username of connection
         */
        public Builder(String username) {
            this.mUsername = username;
        }

        public Builder addUsername(final String val) {
            mUsername = val;
            return this;
        }

        public Builder addEmail(final String val) {
            mEmail = val;
            return this;
        }

        public Builder addFirstName(final String val) {
            mFname = val;
            return this;
        }

        public Builder addLastName(final String val) {
            mLname = val;
            return this;
        }


        public Connection build() {
            return new Connection(this);
        }
    }

    private Connection(final Builder builder) {
        this.mUsername = builder.mUsername;
        this.mEmail = builder.mEmail;
        this.mFname = builder.mFname;
        this.mLname = builder.mLname;
        this.mMemID = builder.mMemID;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFname;
    }

    public String getLastName() {
        return mLname;
    }

    public String getMemID() {
        return mMemID;
    }

    public String getFullName() { return mFname + " " + mLname; }
}
