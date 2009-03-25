/**
 * 
 */
package org.apache.tiles.template;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Stack;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class InsertAttributeModelTest {

    /**
     * The mock resolver.
     */
    private AttributeResolver resolver;
    
    /**
     * The model to test.
     */
    private InsertAttributeModel model;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        resolver = createMock(AttributeResolver.class);
        model = new InsertAttributeModel(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel#start(Stack, TilesContainer, boolean, String, String, Object, String, String, String, Attribute, Object...)}.
     */
    @Test
    public void testStart() {
        Stack<Object> composeStack = new Stack<Object>();
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        Attribute attribute = new Attribute();
        AttributeContext attributeContext = createMock(AttributeContext.class);
        
        container.prepare("myPreparer", requestItem);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", requestItem)).andReturn(attribute);
        expect(container.startContext(requestItem)).andReturn(attributeContext);
        
        replay(resolver, container, attributeContext);
        model.start(composeStack, container, false, "myPreparer", "myRole", "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", "myName", attribute, requestItem);
        assertEquals(1, composeStack.size());
        assertEquals(attribute, composeStack.peek());
        verify(resolver, container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel#end(Stack, TilesContainer, boolean, Object...)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() throws IOException {
        Stack<Object> composeStack = new Stack<Object>();
        Attribute attribute = new Attribute("myValue");
        composeStack.push(attribute);
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        
        container.endContext(requestItem);
        container.render(attribute, requestItem);
        
        replay(resolver, container);
        model.end(composeStack, container, false, requestItem);
        verify(resolver, container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertAttributeModel#execute(TilesContainer, boolean, String, String, Object, String, String, String, Attribute, Object...)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        Attribute attribute = new Attribute("myValue");
        AttributeContext attributeContext = createMock(AttributeContext.class);

        container.prepare("myPreparer", requestItem);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", requestItem)).andReturn(attribute);
        expect(container.startContext(requestItem)).andReturn(attributeContext);
        container.endContext(requestItem);
        container.render(attribute, requestItem);

        replay(resolver, container);
        model.execute(container, false, "myPreparer", "myRole", "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", "myName", attribute, requestItem);
        verify(resolver, container);
    }

}
