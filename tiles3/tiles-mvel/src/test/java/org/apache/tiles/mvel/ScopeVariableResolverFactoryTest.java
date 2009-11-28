package org.apache.tiles.mvel;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.integration.VariableResolver;


public class ScopeVariableResolverFactoryTest {

    /**
     * The Tiles request.
     */
    private Request request;

    /**
     * The Tiles application context.
     */
    private ApplicationContext applicationContext;

    /**
     * The object to test.
     */
    private ScopeVariableResolverFactory factory;

    /**
     * Sets up the object.
     */
    @Before
    public void setUp() {
        request = createMock(Request.class);
        expect(request.getAvailableScopes()).andReturn(new String[]{"request",
        		"session", "application"}).anyTimes();
        TilesRequestContextHolder holder = new TilesRequestContextHolder();
        holder.setTilesRequestContext(request);
        applicationContext = createMock(ApplicationContext.class);
        factory = new ScopeVariableResolverFactory(holder);
    }

    /**
     * Tears down the object.
     */
    @After
    public void tearDown() {
        verify(request, applicationContext);
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#createVariable(String, Object)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObject() {
        replay(request, applicationContext);
        factory.createVariable("myName", "myValue");
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#createVariable(String, Object, Class)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateVariableStringObjectClassOfQ() {
        replay(request, applicationContext);
        factory.createVariable("myName", "myValue", String.class);
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#isResolveable(String)}.
     */
    @Test
    public void testIsResolveable() {
        replay(request, applicationContext);
        assertFalse(factory.isResolveable("header"));
        assertTrue(factory.isResolveable("requestScope"));
        assertTrue(factory.isResolveable("applicationScope"));
        assertFalse(factory.isResolveable("blah"));
    }

    /**
     * Test method for {@link TilesContextVariableResolverFactory#isTarget(String)}.
     */
    @Test
    public void testIsTarget() {
        replay(request, applicationContext);
        assertFalse(factory.isTarget("header"));
        assertTrue(factory.isTarget("requestScope"));
        assertTrue(factory.isTarget("applicationScope"));
        assertFalse(factory.isTarget("blah"));
    }

    /**
     * Test method for {@link org.mvel2.integration.impl.BaseVariableResolverFactory#getVariableResolver(String)}.
     */
    @Test
    public void testGetVariableResolver() {
        Map<String, Object> requestScope = new HashMap<String, Object>();
        requestScope.put("one", 1);
        expect(request.getContext("request")).andReturn(requestScope);
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        applicationScope.put("two", 2);
        expect(request.getContext("application")).andReturn(applicationScope);
        replay(request, applicationContext);

        VariableResolver resolver = factory.getVariableResolver("requestScope");
        assertEquals(requestScope, resolver.getValue());
        resolver = factory.getVariableResolver("applicationScope");
        assertEquals(applicationScope, resolver.getValue());
    }

}
