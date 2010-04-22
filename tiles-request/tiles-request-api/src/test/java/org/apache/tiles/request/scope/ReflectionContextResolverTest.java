/**
 *
 */
package org.apache.tiles.request.scope;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.util.RequestWrapper;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ReflectionContextResolver}
 *
 * @version $Rev$ $Date$
 */
public class ReflectionContextResolverTest {

    private static final String [] SCOPES = new String[] {"one", "two", "three"};

    private ReflectionContextResolver resolver;

    private Map<String, Object> oneScope;
    private Map<String, Object> twoScope;
    private Map<String, Object> threeScope;

    private Request request;

    /**
     * Initializes the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        resolver = new ReflectionContextResolver();
        oneScope = createMock(Map.class);
        twoScope = createMock(Map.class);
        threeScope = createMock(Map.class);
        request = new SampleRequest(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.scope.ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test
    public void testGetContext() {
        replay(oneScope, twoScope, threeScope);
        assertEquals(oneScope, resolver.getContext(request, "one"));
        assertEquals(twoScope, resolver.getContext(request, "two"));
        assertEquals(threeScope, resolver.getContext(request, "three"));
        verify(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.scope.ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test
    public void testGetContextWrapped() {
        replay(oneScope, twoScope, threeScope);
        RequestWrapper wrapper = new RequestWrapper(request);
        assertEquals(oneScope, resolver.getContext(wrapper, "one"));
        assertEquals(twoScope, resolver.getContext(wrapper, "two"));
        assertEquals(threeScope, resolver.getContext(wrapper, "three"));
        verify(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.scope.ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test(expected=NoSuchScopeException.class)
    public void testGetContextException() {
        resolver.getContext(request, "none");
    }

    /**
     * Test method for {@link org.apache.tiles.request.scope.ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test(expected=NoSuchScopeException.class)
    public void testGetContextException2() {
        resolver.getContext(request, "private");
    }

    /**
     * Test method for {@link org.apache.tiles.request.scope.ReflectionContextResolver#getContext(org.apache.tiles.request.Request, java.lang.String)}.
     */
    @Test(expected=NoSuchScopeException.class)
    public void testGetContextException3() {
        resolver.getContext(request, "unavailable");
    }

    /**
     * Test method for {@link org.apache.tiles.request.scope.ReflectionContextResolver#getAvailableScopes(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetAvailableScopes() {
        replay(oneScope, twoScope, threeScope);
        assertArrayEquals(SCOPES, resolver.getAvailableScopes(request));
        verify(oneScope, twoScope, threeScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.scope.ReflectionContextResolver#getAvailableScopes(org.apache.tiles.request.Request)}.
     */
    @Test
    public void testGetAvailableScopesWrapped() {
        replay(oneScope, twoScope, threeScope);
        RequestWrapper wrapper = new RequestWrapper(request);
        assertArrayEquals(SCOPES, resolver.getAvailableScopes(wrapper));
        verify(oneScope, twoScope, threeScope);
    }

    public static class SampleRequest implements Request {

        private static final String [] SCOPES = new String[] {"one", "two", "three"};

        private Map<String, Object> oneScope, twoScope, threeScope;

        public SampleRequest(Map<String, Object> oneScope,
                Map<String, Object> twoScope, Map<String, Object> threeScope) {
            this.oneScope = oneScope;
            this.twoScope = twoScope;
            this.threeScope = threeScope;
        }

        @Override
        public String[] getNativeScopes() {
            return SCOPES;
        }

        @Override
        public String[] getAvailableScopes() {
            return SCOPES;
        }

        public Map<String, Object> getOneScope() {
            return oneScope;
        }

        public Map<String, Object> getTwoScope() {
            return twoScope;
        }

        public Map<String, Object> getThreeScope() {
            return threeScope;
        }

        @SuppressWarnings("unused")
        private Map<String, Object> getPrivateScope() {
            return null;
        }

        public Map<String, Object> getUnavailableScope() {
            throw new UnsupportedOperationException("No way!");
        }

        @Override
        public void dispatch(String path) {
            // Does nothing.
        }

        @Override
        public ApplicationContext getApplicationContext() {
            return null;
        }

        @Override
        public Map<String, Object> getContext(String scope) {
            return null;
        }

        @Override
        public Map<String, String> getHeader() {
            return null;
        }

        @Override
        public Map<String, String[]> getHeaderValues() {
            return null;
        }

        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        @Override
        public Map<String, String> getParam() {
            return null;
        }

        @Override
        public Map<String, String[]> getParamValues() {
            return null;
        }

        @Override
        public PrintWriter getPrintWriter() {
            return null;
        }

        @Override
        public Locale getRequestLocale() {
            return null;
        }

        @Override
        public Object[] getRequestObjects() {
            return null;
        }

        @Override
        public Writer getWriter() {
            return null;
        }

        @Override
        public void include(String path) {
            // Does nothing.
        }

        @Override
        public boolean isResponseCommitted() {
            return false;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public void setContentType(String contentType) {
            // Does nothing.
        }
    }
}
