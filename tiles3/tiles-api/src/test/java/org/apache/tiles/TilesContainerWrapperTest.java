/**
 *
 */
package org.apache.tiles;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link TilesContainerWrapper}.
 *
 * @version $Rev$ $Date$
 */
public class TilesContainerWrapperTest {

    private TilesContainer container;

    private TilesContainerWrapper wrapper;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        container = createMock(TilesContainer.class);
        wrapper = new TilesContainerWrapper(container);
    }

    /**
     * Tests {@link TilesContainerWrapper#TilesContainerWrapper(TilesContainer)}.
     */
    @Test(expected=NullPointerException.class)
    public void testTilesContainerWrapperNPE() {
        new TilesContainerWrapper(null);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#getWrappedContainer()}.
     */
    @Test
    public void testGetWrappedContainer() {
        replay(container);
        assertSame(container, wrapper.getWrappedContainer());
        verify(container);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#endContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testEndContext() {
        Request request = createMock(Request.class);

        container.endContext(request);

        replay(container, request);
        wrapper.endContext(request);
        verify(container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#evaluate(org.apache.tiles.Attribute, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testEvaluate() {
        Request request = createMock(Request.class);
        Attribute attribute = createMock(Attribute.class);

        expect(container.evaluate(attribute, request)).andReturn(new Integer(1));

        replay(container, request, attribute);
        assertEquals(new Integer(1), wrapper.evaluate(attribute, request));
        verify(container, request, attribute);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#getApplicationContext()}.
     */
    @Test
    public void testGetApplicationContext() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(container.getApplicationContext()).andReturn(applicationContext);

        replay(container, applicationContext);
        assertSame(applicationContext, wrapper.getApplicationContext());
        verify(container, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#getAttributeContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetAttributeContext() {
        Request request = createMock(Request.class);
        AttributeContext attribute = createMock(AttributeContext.class);

        expect(container.getAttributeContext(request)).andReturn(attribute);

        replay(container, request, attribute);
        assertSame(attribute, wrapper.getAttributeContext(request));
        verify(container, request, attribute);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#getDefinition(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetDefinition() {
        Request request = createMock(Request.class);
        Definition definition = createMock(Definition.class);

        expect(container.getDefinition("definition", request)).andReturn(definition);

        replay(container, request, definition);
        assertSame(definition, wrapper.getDefinition("definition", request));
        verify(container, request, definition);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#isValidDefinition(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testIsValidDefinition() {
        Request request = createMock(Request.class);

        expect(container.isValidDefinition("definition", request)).andReturn(true);

        replay(container, request);
        assertTrue(wrapper.isValidDefinition("definition", request));
        verify(container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#prepare(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testPrepare() {
        Request request = createMock(Request.class);

        container.prepare("preparer", request);

        replay(container, request);
        wrapper.prepare("preparer", request);
        verify(container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#render(java.lang.String, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testRenderStringRequest() {
        Request request = createMock(Request.class);

        container.render("definition", request);

        replay(container, request);
        wrapper.render("definition", request);
        verify(container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#render(org.apache.tiles.Definition, org.apache.tiles.request.Request)}.
     */
    @Test
    public void testRenderDefinitionRequest() {
        Request request = createMock(Request.class);
        Definition definition = createMock(Definition.class);

        container.render(definition, request);

        replay(container, request, definition);
        wrapper.render(definition, request);
        verify(container, request, definition);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#render(org.apache.tiles.Attribute, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderAttributeRequest() throws IOException {
        Request request = createMock(Request.class);
        Attribute attribute = createMock(Attribute.class);

        container.render(attribute, request);

        replay(container, request, attribute);
        wrapper.render(attribute, request);
        verify(container, request, attribute);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#renderContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testRenderContext() {
        Request request = createMock(Request.class);

        container.renderContext(request);

        replay(container, request);
        wrapper.renderContext(request);
        verify(container, request);
    }

    /**
     * Test method for {@link org.apache.tiles.TilesContainerWrapper#startContext(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testStartContext() {
        Request request = createMock(Request.class);
        AttributeContext attribute = createMock(AttributeContext.class);

        expect(container.startContext(request)).andReturn(attribute);

        replay(container, request, attribute);
        assertSame(attribute, wrapper.startContext(request));
        verify(container, request, attribute);
    }

}
