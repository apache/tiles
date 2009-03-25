/**
 * 
 */
package org.apache.tiles.template;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.Writer;
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
public class GetAsStringModelTest {

    /**
     * The mock resolver.
     */
    private AttributeResolver resolver;
    
    /**
     * The model to test.
     */
    private GetAsStringModel model;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        resolver = createMock(AttributeResolver.class);
        model = new GetAsStringModel(resolver);
    }

    /**
     * Test method for {@link org.apache.tiles.template.GetAsStringModel#start(java.util.Stack, org.apache.tiles.TilesContainer, boolean, java.lang.String, java.lang.String, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, org.apache.tiles.Attribute, java.lang.Object[])}.
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
     * Test method for {@link org.apache.tiles.template.GetAsStringModel#end(java.util.Stack, org.apache.tiles.TilesContainer, java.io.Writer, boolean, java.lang.Object[])}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testEnd() throws IOException {
        Stack<Object> composeStack = new Stack<Object>();
        Attribute attribute = new Attribute("myValue");
        composeStack.push(attribute);
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        Writer writer = createMock(Writer.class);
        
        writer.write("myValue");
        container.endContext(requestItem);
        
        replay(resolver, container, writer);
        model.end(composeStack, container, writer, false, requestItem);
        verify(resolver, container, writer);
    }

    /**
     * Test method for {@link org.apache.tiles.template.GetAsStringModel#execute(org.apache.tiles.TilesContainer, java.io.Writer, boolean, java.lang.String, java.lang.String, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, org.apache.tiles.Attribute, java.lang.Object[])}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        Attribute attribute = new Attribute("myValue");
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Writer writer = createMock(Writer.class);

        container.prepare("myPreparer", requestItem);
        expect(resolver.computeAttribute(container, attribute, "myName", "myRole", false, "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", requestItem)).andReturn(attribute);
        expect(container.startContext(requestItem)).andReturn(attributeContext);
        writer.write("myValue");
        container.endContext(requestItem);

        replay(resolver, container, writer);
        model.execute(container, writer, false, "myPreparer", "myRole", "myDefaultValue",
                "myDefaultValueRole", "myDefaultValueType", "myName", attribute, requestItem);
        verify(resolver, container, writer);
    }

}
