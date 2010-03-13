package org.apache.tiles.autotag.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tiles.autotag.core.AutotagRuntimeException;

public class StringTool {

    private Map<String, String> type2default;

    private Map<String, String> primitive2wrapped;

    public StringTool() {
        type2default = new HashMap<String, String>();
        type2default.put("byte", "0");
        type2default.put("short", "0");
        type2default.put("int", "0");
        type2default.put("long", "0L");
        type2default.put("float", "0.0f");
        type2default.put("double", "0.0d");
        type2default.put("char", "'\\u0000'");
        type2default.put("boolean", "false");

        primitive2wrapped = new HashMap<String, String>();
        primitive2wrapped.put("byte", Byte.class.getName());
        primitive2wrapped.put("short", Short.class.getName());
        primitive2wrapped.put("int", Integer.class.getName());
        primitive2wrapped.put("long", Long.class.getName());
        primitive2wrapped.put("float", Float.class.getName());
        primitive2wrapped.put("double", Double.class.getName());
        primitive2wrapped.put("char", Character.class.getName());
        primitive2wrapped.put("boolean", Boolean.class.getName());
    }

    public List<String> splitOnNewlines(String toSplit) {
        List<String> retValue = new ArrayList<String>();
        if (toSplit == null) {
            return retValue;
        }
        Reader reader = new StringReader(toSplit);
        BufferedReader bufReader = new BufferedReader(reader);
        try {
            String line;
            while((line = bufReader.readLine()) != null) {
                retValue.add(line);
            }
        } catch (IOException e) {
            throw new AutotagRuntimeException(
                    "Cannot read the string completely", e);
        }
        return retValue;
    }

    public String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public String getDefaultValue(String type, String overriddenDefaultValue) {
        if (overriddenDefaultValue != null) {
            return overriddenDefaultValue;
        }

        String retValue = type2default.get(type);
        if (retValue == null) {
            retValue = "null";
        }
        return retValue;
    }

    public String getClassToCast(String type) {
        String retValue = primitive2wrapped.get(type);
        if (retValue == null) {
            retValue = type;
        }
        return retValue;
    }
}
