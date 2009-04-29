/**
 *
 */
package org.apache.tiles.velocity.template;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.velocity.context.Context;
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
     * @throws java.lang.Exception
     * @since 2.2.0
     */
    @Before
    public void setUp() throws Exception {
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
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#cloneAttribute(org.apache.tiles.Attribute)}.
     */
    @Test
    public void testCloneAttribute() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#createTemplateAttribute(java.lang.String)}.
     */
    @Test
    public void testCreateTemplateAttribute() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#renderAttribute(org.apache.tiles.Attribute)}.
     */
    @Test
    public void testRenderAttribute() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#renderDefinition(java.lang.String)}.
     */
    @Test
    public void testRenderDefinition() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#renderAttributeContext()}.
     */
    @Test
    public void testRenderAttributeContext() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#startAttributeContext()}.
     */
    @Test
    public void testStartAttributeContext() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#endAttributeContext()}.
     */
    @Test
    public void testEndAttributeContext() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#getAttributeContext()}.
     */
    @Test
    public void testGetAttributeContext() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#setCurrentContainer(java.lang.String)}.
     */
    @Test
    public void testSetCurrentContainer() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.VelocityStyleTilesTool#toString()}.
     */
    @Test
    public void testToString() {
        fail("Not yet implemented");
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
