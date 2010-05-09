/**
 *
 */
package org.apache.tiles.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link SimpleMenuItem}.
 *
 * @version $Rev$ $Date$
 */
public class SimpleMenuItemTest {

    private SimpleMenuItem item;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        item = new SimpleMenuItem();
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setValue(java.lang.String)}.
     */
    @Test
    public void testSetValue() {
        item.setValue("value");
        assertEquals("value", item.getValue());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setLink(java.lang.String)}.
     */
    @Test
    public void testSetLink() {
        item.setLink("value");
        assertEquals("value", item.getLink());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setIcon(java.lang.String)}.
     */
    @Test
    public void testSetIcon() {
        item.setIcon("value");
        assertEquals("value", item.getIcon());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#setTooltip(java.lang.String)}.
     */
    @Test
    public void testSetTooltip() {
        item.setTooltip("value");
        assertEquals("value", item.getTooltip());
    }

    /**
     * Test method for {@link org.apache.tiles.beans.SimpleMenuItem#toString()}.
     */
    @Test
    public void testToString() {
        item.setIcon("icon");
        item.setLink("link");
        item.setTooltip("tooltip");
        item.setValue("value");
        assertEquals(
                "SimpleMenuItem[value=value, link=link, tooltip=tooltip, icon=icon, ]",
                item.toString());
    }

}
