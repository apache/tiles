/**
 * 
 */
package org.apache.tiles.template;

import static org.junit.Assert.*;

import java.util.Stack;

import org.apache.tiles.ListAttribute;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class AddListAttributeModelTest {

    /**
     * The model to test.
     */
    private AddListAttributeModel model;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        model = new AddListAttributeModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.AddListAttributeModel#start(java.util.Stack, java.lang.String)}.
     */
    @Test
    public void testStart() {
        Stack<Object> composeStack = new Stack<Object>();
        model.start(composeStack, "myRole");
        assertEquals(1, composeStack.size());
        ListAttribute listAttribute = (ListAttribute) composeStack.peek();
        assertEquals("myRole", listAttribute.getRole());
    }

    /**
     * Test method for {@link org.apache.tiles.template.AddListAttributeModel#end(java.util.Stack)}.
     */
    @Test
    public void testEnd() {
        Stack<Object> composeStack = new Stack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        composeStack.push(listAttribute);
        model.end(composeStack);
        assertEquals(0, composeStack.size());
    }

}
