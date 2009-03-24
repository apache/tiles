/**
 * 
 */
package org.apache.tiles.template;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class DefaultAttributeResolverTest {

    /**
     * The resolver to test.
     */
    private DefaultAttributeResolver resolver;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        resolver = new DefaultAttributeResolver();
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver#computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute, java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeInContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute("myValue", "myExpression", "myRole", "myRenderer");
        Integer requestItem = new Integer(1);
        
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(attribute);
        
        replay(container, attributeContext);
        assertEquals(attribute, resolver.computeAttribute(container, null,
                "myName", null, false, null, null, null, requestItem));
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver#computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute, java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeInCall() {
        TilesContainer container = createMock(TilesContainer.class);
        Attribute attribute = new Attribute("myValue", "myExpression", "myRole", "myRenderer");
        Integer requestItem = new Integer(1);
        
        replay(container);
        assertEquals(attribute, resolver.computeAttribute(container, attribute,
                null, null, false, null, null, null, requestItem));
        verify(container);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver#computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute, java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeDefault() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Integer requestItem = new Integer(1);
        
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);
        
        replay(container, attributeContext);
        Attribute attribute = resolver.computeAttribute(container, null,
                "myName", null, false, "defaultValue", "defaultRole",
                "defaultType", requestItem);
        assertEquals("defaultValue", attribute.getValue());
        assertEquals("defaultRole", attribute.getRole());
        assertEquals("defaultType", attribute.getRenderer());
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver#computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute, java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])}.
     */
    @Test(expected = NoSuchAttributeException.class)
    public void testComputeAttributeException() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Integer requestItem = new Integer(1);
        
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);
        
        replay(container, attributeContext);
        resolver.computeAttribute(container, null, "myName", null, false, null,
                "defaultRole", "defaultType", requestItem);
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.DefaultAttributeResolver#computeAttribute(org.apache.tiles.TilesContainer, org.apache.tiles.Attribute, java.lang.String, java.lang.String, boolean, java.lang.Object, java.lang.String, java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testComputeAttributeIgnore() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Integer requestItem = new Integer(1);
        
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("myName")).andReturn(null);
        
        replay(container, attributeContext);
        assertNull(resolver.computeAttribute(container, null, "myName", null, true, null,
                "defaultRole", "defaultType", requestItem));
        verify(container, attributeContext);
    }
}
