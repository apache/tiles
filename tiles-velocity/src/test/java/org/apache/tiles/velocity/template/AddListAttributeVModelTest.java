/**
 * 
 */
package org.apache.tiles.velocity.template;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.AddListAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.Context;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link AddListAttributeVModel}.
 */
public class AddListAttributeVModelTest {

    /**
     * The model to test.
     */
    private AddListAttributeVModel model;
    
    /**
     * The template model.
     */
    private AddListAttributeModel tModel;
    
    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(AddListAttributeModel.class);
        model = new AddListAttributeVModel(tModel);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.AddListAttributeVModel#start(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context, java.util.Map)}.
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
        tModel.start(composeStack, "myRole");
        
        replay(tModel, request, response, velocityContext);
        model.start(request, response, velocityContext, params);
        assertTrue(parameterMapStack.isEmpty());
        verify(tModel, request, response, velocityContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.AddListAttributeVModel#end(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.velocity.context.Context)}.
     */
    @Test
    public void testEnd() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Context velocityContext = createMock(Context.class);
        Stack<Object> composeStack = new Stack<Object>();
        Stack<Map<String, Object>> parameterMapStack = new Stack<Map<String,Object>>();
        
        expect(request.getAttribute(ServletUtil.COMPOSE_STACK_ATTRIBUTE_NAME))
                .andReturn(composeStack);
        tModel.end(composeStack);
        
        replay(tModel, request, response, velocityContext);
        assertEquals(VelocityUtil.EMPTY_RENDERABLE, model.end(request, response, velocityContext));
        assertTrue(parameterMapStack.isEmpty());
        verify(tModel, request, response, velocityContext);
    }

    /**
     * Creates the parameters to work with the model.
     * 
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("role", "myRole");
        return params;
    }
}
