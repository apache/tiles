package org.apache.tiles.request.velocity.autotag;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import java.io.Writer;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.request.ApplicationAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.velocity.VelocityRequest;
import org.apache.tiles.request.velocity.autotag.VelocityAutotagRuntime;
import org.apache.tiles.request.velocity.autotag.VelocityModelBody;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewToolContext;
import org.junit.Test;

public class VelocityAutotagRuntimeTest {
    @Test
    public void testCreateRequest() {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        Node node = createMock(Node.class);
        ViewToolContext viewContext = createMock(ViewToolContext.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ServletContext servletContext = createMock(ServletContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(context.getInternalUserContext()).andReturn(viewContext);
        expect(viewContext.getRequest()).andReturn(request);
        expect(viewContext.getResponse()).andReturn(response);
        expect(viewContext.getServletContext()).andReturn(servletContext);
        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext);

        replay(context, writer, node, viewContext, request, response, servletContext, applicationContext);
        VelocityAutotagRuntime runtime = new VelocityAutotagRuntime();
        runtime.render(context, writer, node);
        Request velocityRequest = runtime.createRequest();
        assertTrue(velocityRequest instanceof VelocityRequest);
        verify(context, writer, node, viewContext, request, response, servletContext, applicationContext);
    }

    @Test
    public void testCreateModelBody() {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        Node node = createMock(Node.class);
        ASTBlock block = createMock(ASTBlock.class);
        expect(node.jjtGetChild(1)).andReturn(block);
        replay(context, writer, node, block);
        VelocityAutotagRuntime runtime = new VelocityAutotagRuntime();
        runtime.render(context, writer, node);
        ModelBody modelBody = runtime.createModelBody();
        assertTrue(modelBody instanceof VelocityModelBody);
        verify(context, writer, node, block);
    }

    @Test
    public void testGetParameter() {
        InternalContextAdapter context = createMock(InternalContextAdapter.class);
        Writer writer = createMock(Writer.class);
        Node node = createMock(Node.class);
        ASTMap astMap = createMock(ASTMap.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> params = createMock(Map.class);
        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(context)).andReturn(params);
        expect(params.get(eq("notnullParam"))).andReturn(new Integer(42)).anyTimes();
        expect(params.get(eq("nullParam"))).andReturn(null).anyTimes();
        replay(context, writer, node, astMap, params);
        VelocityAutotagRuntime runtime = new VelocityAutotagRuntime();
        runtime.render(context, writer, node);
        Object notnullParam = runtime.getParameter("notnullParam", null);
        Object nullParam = runtime.getParameter("nullParam", null);
        Object notnullParamDefault = runtime.getParameter("notnullParam", new Integer(24));
        Object nullParamDefault = runtime.getParameter("nullParam", new Integer(24));
        assertEquals(42, notnullParam);
        assertEquals(null, nullParam);
        assertEquals(42, notnullParamDefault);
        assertEquals(24, nullParamDefault);
        verify(context, writer, node, astMap, params);
    }
}
