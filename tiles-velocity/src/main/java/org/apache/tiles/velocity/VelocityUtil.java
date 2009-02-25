package org.apache.tiles.velocity;

import javax.servlet.ServletContext;

import org.apache.tiles.template.GetAsStringModel;

public class VelocityUtil {

    private final static String TEMPLATE_PREFIX="org.apache.tiles.template.";
    
    public static boolean toSimpleBoolean(Boolean obj, boolean defaultValue) {
        return obj != null ? obj : defaultValue;
    }
}
