package org.apache.tiles.autotag.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.autotag.core.AutotagRuntimeException;

public class StringTool {

    public List<String> splitOnNewlines(String toSplit) {
        List<String> retValue = new ArrayList<String>();
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
}
