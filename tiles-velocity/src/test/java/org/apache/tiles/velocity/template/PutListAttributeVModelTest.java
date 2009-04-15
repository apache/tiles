/**
 * 
 */
package org.apache.tiles.velocity.template;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.PutListAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PutListAttributeVModel}.
 */
public class PutListAttributeVModelTest {
    
    /**
     * The attribute key that will be used to store the parameter map, to use across Velocity tool calls.
     */
    private final static String PARAMETER_MAP_STACK_KEY = "org.apache.tiles.velocity.PARAMETER_MAP_STACK"; 

    /**
     * The model to test.
     */
    private PutListAttributeVModel model;
    
    /**
     * The template model.
     */
    private PutListAttributeModel tModel;
    
    /**
     * The servlet context.
     */
    private ServletContext servletContext;
    
    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(PutListAttributeModel.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.PutListAttributeVModel#start(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
     */
    @Test
    public void testStart() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Map<String, Object> params = createParams();
        Stack<Object> composeStack = new Stack<Object>();
        Stack<Map<String, Object>> parameterMapStack = new Stack<Map<String,Object>>();

        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(parameterMapStack);
        tModel.start(composeStack, "myRole", false);
        
        replay(tModel, servletContext, request, response, velocityContext);
        initializeModel();
        model.start(request, response, velocityContext, params);
        assertEquals(1, parameterMapStack.size());
        assertEquals(params, parameterMapStack.peek());
        verify(tModel, servletContext, request, response, velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.PutListAttributeVModel#end(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context)}.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        TilesContainer container = createMock(TilesContainer.class);
        Stack<Object> composeStack = new Stack<Object>();
        Map<String, Object> params = createParams();
        Stack<Map<String, Object>> parameterMapStack = new Stack<Map<String,Object>>();
        parameterMapStack.push(params);
        
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        expect(velocityContext.get(PARAMETER_MAP_STACK_KEY)).andReturn(parameterMapStack);
        expect(request.getAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        tModel.end(container, composeStack, "myName", false, velocityContext, request, response);
        
        replay(tModel, servletContext, container, request, response, velocityContext);
        initializeModel();
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.end(request, response, velocityContext));
        assertTrue(parameterMapStack.isEmpty());
        verify(tModel, servletContext, container, request, response, velocityContext);
    }

    /**
     * Creates the parameters to work with the model.
     * 
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "myName");
        params.put("role", "myRole");
        params.put("inherit", false);
        params.put("cascade", false);
        return params;
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new PutListAttributeVModel(tModel, servletContext);
    }
}
