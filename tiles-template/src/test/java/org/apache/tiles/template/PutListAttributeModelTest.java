/**
 * 
 */
package org.apache.tiles.template;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Stack;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.ListAttribute;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class PutListAttributeModelTest {

    /**
     * The model to test.
     */
    private PutListAttributeModel model;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        model = new PutListAttributeModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutListAttributeModel#start(Stack, String, boolean)}.
     */
    @Test
    public void testStart() {
        Stack<Object> composeStack = new Stack<Object>();
        model.start(composeStack, "myRole", false);
        assertEquals(1, composeStack.size());
        ListAttribute listAttribute = (ListAttribute) composeStack.peek();
        assertEquals("myRole", listAttribute.getRole());
    }

    /**
     * Test method for {@link org.apache.tiles.template.PutListAttributeModel#end(org.apache.tiles.TilesContainer, Stack, String, boolean, Object...)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Stack<Object> composeStack = new Stack<Object>();
        ListAttribute listAttribute = new ListAttribute();
        Integer requestItem = new Integer(1);
        composeStack.push(listAttribute);
        
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        attributeContext.putAttribute("myName", listAttribute, false);
        
        replay(container, attributeContext);
        model.end(container, composeStack, "myName", false, requestItem);
        assertEquals(0, composeStack.size());
        verify(container, attributeContext);
    }

}
