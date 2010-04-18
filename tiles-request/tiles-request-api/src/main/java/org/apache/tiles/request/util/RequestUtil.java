package org.apache.tiles.request.util;

import java.util.Enumeration;

public class RequestUtil {

    private RequestUtil() {

    }

    /**
     * Returns the string representation of the key.
     *
     * @param key The key.
     * @return The string representation of the key.
     * @throws IllegalArgumentException If the key is <code>null</code>.
     */
    public static String key(Object key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key instanceof String) {
            return ((String) key);
        } else {
            return (key.toString());
        }
    }

    public static int enumerationSize(Enumeration<?> keys) {
        int n = 0;
        while (keys.hasMoreElements()) {
            keys.nextElement();
            n++;
        }
        return n;
    }
}
