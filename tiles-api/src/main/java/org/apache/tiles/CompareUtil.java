package org.apache.tiles;

public final class CompareUtil {

    private CompareUtil() {}

    public static boolean nullSafeEquals(Object obj1, Object obj2) {
        if (obj1 != null) {
            return obj1.equals(obj2);
        }
        return obj2 == null;
    }
}
