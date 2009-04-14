/**
 * 
 */
package org.apache.tiles.velocity.template;

import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ImportAttributeVModel}.
 */
public class ImportAttributeVModelTest {

    /**
     * The model to test.
     */
    private ImportAttributeVModel model;
    
    /**
     * The template model.
     */
    private ImportAttributeModel tModel;
    
    /**
     * The servlet context.
     */
    private ServletContext servletContext;
    
    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(ImportAttributeModel.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws MethodInvocationException If something goes wrong.
     */
    @Test
    public void testExecutePage() throws MethodInvocationException, ParseErrorException, ResourceNotFoundException, IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        expect(internalContextAdapter.put("one", "value1")).andReturn("value1");
        expect(internalContextAdapter.put("two", "value2")).andReturn("value2");
        
        replay(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws MethodInvocationException If something goes wrong.
     */
    @Test
    public void testExecuteRequest() throws MethodInvocationException, ParseErrorException, ResourceNotFoundException, IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "request");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        request.setAttribute("one", "value1");
        request.setAttribute("two", "value2");
        
        replay(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws MethodInvocationException If something goes wrong.
     */
    @Test
    public void testExecuteSession() throws MethodInvocationException, ParseErrorException, ResourceNotFoundException, IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        HttpSession session = createMock(HttpSession.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "session");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        expect(request.getSession()).andReturn(session).times(2);
        session.setAttribute("one", "value1");
        session.setAttribute("two", "value2");
        
        replay(tModel, servletContext, request, response, session, velocityContext, container, internalContextAdapter);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, session, velocityContext, container, internalContextAdapter);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.ImportAttributeVModel#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws MethodInvocationException If something goes wrong.
     */
    @Test
    public void testExecuteApplication() throws MethodInvocationException, ParseErrorException, ResourceNotFoundException, IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        InternalContextAdapter internalContextAdapter = createMock(InternalContextAdapter.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        params.put("scope", "application");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("one", "value1");
        attributes.put("two", "value2");
        
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(tModel.getImportedAttributes(container, "myName", "myToName", false,
                velocityContext, request, response)).andReturn(attributes);
        servletContext.setAttribute("one", "value1");
        servletContext.setAttribute("two", "value2");
        
        replay(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
        initializeModel();
        Renderable renderable = model.execute(request, response, velocityContext, params);
        renderable.render(internalContextAdapter, writer);
        verify(tModel, servletContext, request, response, velocityContext, container, internalContextAdapter);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new ImportAttributeVModel(tModel, servletContext);
    }

    /**
     * Creates the parameters to work with the model.
     * 
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ignore", false);
        params.put("name", "myName");
        params.put("toName", "myToName");
        return params;
    }
}
