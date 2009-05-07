/**
 *
 */
package org.apache.tiles.velocity.template;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.Renderable;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class VelocityStyleTilesToolTest {

    /**
     * The tool to test.
     */
    private VelocityStyleTilesTool tool;

    /**
     * The request object.
     */
    private HttpServletRequest request;

    /**
     * The response object.
     */
    private HttpServletResponse response;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * The current velocity context.
     */
    private Context velocityContext;

    /**
     * Sets up the tool to test.
     *
     * @since 2.2.0
     */
    @Before
    public void setUp() {
        tool = new VelocityStyleTilesTool();
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        velocityContext = createMock(Context.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#getAttribute(java.lang.String)}.
     */
    @Test
    public void testGetAttribute() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute("myValue");

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME))
                .andReturn(container);
        expect(container.getAttributeContext(velocityContext, request, response))
                .andReturn(attributeContext);
        expect(attributeContext.getAttribute("myAttribute")).andReturn(attribute);

        replay(velocityContext, request, response, servletContext, container, attributeContext);
        initializeTool();
        assertEquals(attribute, tool.getAttribute("myAttribute"));
        verify(velocityContext, request, response, servletContext, container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#createAttribute()}.
     */
    @Test
    public void testCreateAttribute() {
        replay(velocityContext, request, response, servletContext);
        initializeTool();
        Attribute attribute =  tool.createAttribute();
        assertNull(attribute.getValue());
        assertNull(attribute.getRenderer());
        assertNull(attribute.getExpression());
        verify(velocityContext, request, response, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#cloneAttribute(org.apache.tiles.Attribute)}.
     */
    @Test
    public void testCloneAttribute() {
        Attribute attribute = new Attribute("myValue", "myExpression", "myRole", "myRendererName");

        replay(velocityContext, request, response, servletContext);
        initializeTool();
        assertEquals(attribute, tool.cloneAttribute(attribute));
        verify(velocityContext, request, response, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#createTemplateAttribute(java.lang.String)}.
     */
    @Test
    public void testCreateTemplateAttribute() {
        replay(velocityContext, request, response, servletContext);
        initializeTool();
        Attribute attribute = tool.createTemplateAttribute("myTemplate");
        assertEquals("myTemplate", attribute.getValue());
        assertEquals("template", attribute.getRenderer());
        verify(velocityContext, request, response, servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#renderAttribute(org.apache.tiles.Attribute)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderAttribute() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        StringWriter writer = new StringWriter();
        Attribute attribute = new Attribute("myValue");

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME))
                .andReturn(container);
        container.render(attribute, velocityContext, request, response, writer);

        replay(velocityContext, request, response, servletContext, container, internalContextAdapter);
        initializeTool();
        Renderable renderable = tool.renderAttribute(attribute);
        renderable.render(internalContextAdapter, writer);
        verify(velocityContext, request, response, servletContext, container, internalContextAdapter);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#renderDefinition(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderDefinition() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        StringWriter writer = new StringWriter();

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME))
                .andReturn(container);
        container.render("myDefinition", velocityContext, request, response, writer);

        replay(velocityContext, request, response, servletContext, container, internalContextAdapter);
        initializeTool();
        Renderable renderable = tool.renderDefinition("myDefinition");
        renderable.render(internalContextAdapter, writer);
        verify(velocityContext, request, response, servletContext, container, internalContextAdapter);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#renderAttributeContext()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRenderAttributeContext() throws IOException {
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        StringWriter writer = new StringWriter();

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME))
                .andReturn(container);
        container.renderContext(velocityContext, request, response, writer);

        replay(velocityContext, request, response, servletContext, container, internalContextAdapter);
        initializeTool();
        Renderable renderable = tool.renderAttributeContext();
        renderable.render(internalContextAdapter, writer);
        verify(velocityContext, request, response, servletContext, container, internalContextAdapter);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#startAttributeContext()}.
     */
    @Test
    public void testStartAttributeContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME))
                .andReturn(container);
        expect(container.startContext(velocityContext, request, response))
                .andReturn(attributeContext);

        replay(velocityContext, request, response, servletContext, container, attributeContext);
        initializeTool();
        assertEquals(attributeContext, tool.startAttributeContext());
        verify(velocityContext, request, response, servletContext, container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#endAttributeContext()}.
     */
    @Test
    public void testEndAttributeContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME))
                .andReturn(container);
        container.endContext(velocityContext, request, response);

        replay(velocityContext, request, response, servletContext, container, attributeContext);
        initializeTool();
        tool.endAttributeContext();
        verify(velocityContext, request, response, servletContext, container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#getAttributeContext()}.
     */
    @Test
    public void testGetAttributeContext() {
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME))
                .andReturn(container);
        expect(container.getAttributeContext(velocityContext, request, response))
                .andReturn(attributeContext);

        replay(velocityContext, request, response, servletContext, container, attributeContext);
        initializeTool();
        assertEquals(attributeContext, tool.getAttributeContext());
        verify(velocityContext, request, response, servletContext, container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#setCurrentContainer(java.lang.String)}.
     */
    @Test
    public void testSetCurrentContainer() {
        TilesContainer container = createMock(TilesContainer.class);

        expect(servletContext.getAttribute("myKey")).andReturn(container);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);

        replay(velocityContext, request, response, servletContext, container);
        initializeTool();
        assertEquals(tool, tool.setCurrentContainer("myKey"));
        verify(velocityContext, request, response, servletContext, container);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("", tool.toString());
    }

    /**
     * Initializes the tool for the test.
     */
    private void initializeTool() {
        tool.setRequest(request);
        tool.setResponse(response);
        tool.setServletContext(servletContext);
        tool.setVelocityContext(velocityContext);
    }
}
