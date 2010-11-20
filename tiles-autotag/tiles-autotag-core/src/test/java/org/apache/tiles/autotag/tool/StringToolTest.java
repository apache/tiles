/**
 *
 */
package org.apache.tiles.autotag.tool;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/**
 * Tests {@link StringTool}.
 *
 * @version $Rev$ $Date$
 */
public class StringToolTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.tool.StringTool#splitOnNewlines(java.lang.String)}.
     */
    @Test
    public void testSplitOnNewlines() {
        StringTool tool = new StringTool();
        List<String> splitted = tool.splitOnNewlines("time\nto\nsplit");
        assertEquals(3, splitted.size());
        assertEquals("time", splitted.get(0));
        assertEquals("to", splitted.get(1));
        assertEquals("split", splitted.get(2));
        splitted = tool.splitOnNewlines(null);
        assertTrue(splitted.isEmpty());
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.tool.StringTool#capitalizeFirstLetter(java.lang.String)}.
     */
    @Test
    public void testCapitalizeFirstLetter() {
        StringTool tool = new StringTool();
        assertEquals("Whatever", tool.capitalizeFirstLetter("whatever"));
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.tool.StringTool#getDefaultValue(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDefaultValue() {
        StringTool tool = new StringTool();
        assertEquals("0", tool.getDefaultValue("byte", null));
        assertEquals("1", tool.getDefaultValue("byte", "1"));
        assertEquals("null", tool.getDefaultValue("Whatever", null));
        assertEquals("thatsit", tool.getDefaultValue("Whatever", "thatsit"));
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.tool.StringTool#getClassToCast(java.lang.String)}.
     */
    @Test
    public void testGetClassToCast() {
        StringTool tool = new StringTool();
        assertEquals(Byte.class.getName(), tool.getClassToCast("byte"));
        assertEquals("Whatever", tool.getClassToCast("Whatever"));
    }

}
