/**
 * 
 */
package org.apache.tiles.velocity.template;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.DefinitionModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefinitionVModel}.
 */
public class DefinitionVModelTest {

    /**
     * The model to test.
     */
    private DefinitionVModel model;
    
    /**
     * The template model.
     */
    private DefinitionModel tModel;
    
    /**
     * The servlet context.
     */
    private ServletContext servletContext;
    
    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(DefinitionModel.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.DefinitionVModel#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
     * @throws IOException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws MethodInvocationException If something goes wrong.
     */
    @Test
    public void testExecute() throws MethodInvocationException, ParseErrorException, ResourceNotFoundException, IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Map<String, Object> params = createParams();
        Stack<Object> composeStack = new Stack<Object>();
        
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.execute(container, composeStack, "myName", "myTemplate", "myRole", "myExtends", "myPreparer",
                velocityContext, request, response);
        
        replay(tModel, servletContext, request, response, velocityContext, container);
        initializeModel();
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.execute(request, response, velocityContext, params));
        verify(tModel, servletContext, request, response, velocityContext, container);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.DefinitionVModel#start(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Map<String, Object> params = createParams();
        Stack<Object> composeStack = new Stack<Object>();

        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.start(composeStack, "myName", "myTemplate", "myRole", "myExtends", "myPreparer");
        
        replay(tModel, servletContext, request, response, velocityContext);
        initializeModel();
        model.start(request, response, velocityContext, params);
        verify(tModel, servletContext, request, response, velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.DefinitionVModel#end(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context)}.
     * @throws IOException If something goes wrong.
     * @throws ResourceNotFoundException If something goes wrong.
     * @throws ParseErrorException If something goes wrong.
     * @throws MethodInvocationException If something goes wrong.
     */
    @Test
    public void testEnd() throws MethodInvocationException, ParseErrorException, ResourceNotFoundException, IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        MutableTilesContainer container = createMock(MutableTilesContainer.class);
        Stack<Object> composeStack = new Stack<Object>();
        
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.end(container, composeStack, velocityContext, request, response);
        
        replay(tModel, servletContext, request, response, velocityContext, container);
        initializeModel();
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.end(request, response, velocityContext));
        verify(tModel, servletContext, request, response, velocityContext, container);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new DefinitionVModel(tModel, servletContext);
    }

    /**
     * Creates the parameters to work with the model.
     * 
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "myName");
        params.put("template", "myTemplate");
        params.put("role", "myRole");
        params.put("extends", "myExtends");
        params.put("preparer", "myPreparer");
        return params;
    }
}
