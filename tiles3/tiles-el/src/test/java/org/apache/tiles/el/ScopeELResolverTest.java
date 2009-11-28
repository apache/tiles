package org.apache.tiles.el;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;


public class ScopeELResolverTest {

    /**
     * The resolver to test.
     */
    private ScopeELResolver resolver;

    /** {@inheritDoc} */
    @Before
    public void setUp() {
        resolver = new ScopeELResolver();
    }

    /**
     * Test method for
     * {@link TilesContextELResolver#getType(javax.el.ELContext, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testGetType() {
        Request request = createMock(Request.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ELContext context = new ELContextImpl(resolver);
        replay(request, applicationContext);
        context.putContext(Request.class, request);
        context.putContext(ApplicationContext.class, applicationContext);
        assertEquals("The requestScope object is not a map.", Map.class,
                resolver.getType(context, null, "requestScope"));
        assertEquals("The sessionScope object is not a map.", Map.class,
                resolver.getType(context, null, "sessionScope"));
        assertEquals("The applicationScope object is not a map.", Map.class,
                resolver.getType(context, null, "applicationScope"));
    }

    /**
     * Test method for
     * {@link TilesContextELResolver#getValue(javax.el.ELContext, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testGetValue() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("objectKey", "objectValue");
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        sessionScope.put("sessionObjectKey", "sessionObjectValue");
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("applicationObjectKey", "applicationObjectValue");
        Request request = createMock(Request.class);
        expect(request.getContext("request")).andReturn(requestScope);
        expect(request.getContext("session")).andReturn(sessionScope);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
		expect(request.getContext("application")).andReturn(applicationScope);
        ELContext context = new ELContextImpl(resolver);
        replay(request, applicationContext);
        context.putContext(Request.class, request);
        context.putContext(ApplicationContext.class, applicationContext);
        assertEquals("The requestScope map does not correspond", requestScope,
                resolver.getValue(context, null, "requestScope"));
        assertEquals("The sessionScope map does not correspond", sessionScope,
                resolver.getValue(context, null, "sessionScope"));
        assertEquals("The applicationScope map does not correspond",
                applicationScope, resolver.getValue(context, null,
                        "applicationScope"));
    }

}
