package com.aashishkumar.androidproject.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Connection> CONNECTIONS = new ArrayList<Connection>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Connection> CONNECTION_MAP = new HashMap<String, Connection>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createConnection(i));
        }
    }

    private static void addItem(Connection item) {
        CONNECTIONS.add(item);
        CONNECTION_MAP.put(item.id, item);
    }

    private static Connection createConnection(int position) {
        return new Connection(String.valueOf(position), "Nickname " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Connection: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Connection {
        public final String id;
        public final String nickName;
        public final String details;

        public Connection(String id, String nickName, String details) {
            this.id = id;
            this.nickName = nickName;
            this.details = details;
        }

        @Override
        public String toString() {
            return nickName;
        }
    }
}
