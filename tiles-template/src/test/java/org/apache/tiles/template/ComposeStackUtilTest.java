/**
 * 
 */
package org.apache.tiles.template;

import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.Test;

/**
 * @author antonio
 *
 */
public class ComposeStackUtilTest {

    /**
     * Test method for {@link org.apache.tiles.template.ComposeStackUtil#findAncestorWithClass(java.util.Stack, java.lang.Class)}.
     */
    @Test
    public void testFindAncestorWithClass() {
        Stack<Object> composeStack = new Stack<Object>();
        Integer integerValue = new Integer(1);
        Long longValue = new Long(2l);
        String stringValue = "my value";
        Integer integerValue2 = new Integer(3);
        composeStack.push(integerValue);
        composeStack.push(longValue);
        composeStack.push(stringValue);
        composeStack.push(integerValue2);
        assertEquals(integerValue2, ComposeStackUtil.findAncestorWithClass(composeStack, Integer.class));
        assertEquals(longValue, ComposeStackUtil.findAncestorWithClass(composeStack, Long.class));
        assertEquals(stringValue, ComposeStackUtil.findAncestorWithClass(composeStack, String.class));
        assertEquals(integerValue2, ComposeStackUtil.findAncestorWithClass(composeStack, Object.class));
    }

}
