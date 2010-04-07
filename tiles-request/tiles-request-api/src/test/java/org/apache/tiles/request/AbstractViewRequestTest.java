/**
 *
 */
package org.apache.tiles.request;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.request.scope.ContextResolver;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 * @version $Rev$ $Date$
 */
public class AbstractViewRequestTest {

    private AbstractViewRequest request;

    private Request wrappedRequest;

    private ApplicationContext applicationContext;

    private ContextResolver contextResolver;

    private Map<String, Object> applicationScope;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        wrappedRequest = createMock(Request.class);
        request = createMockBuilder(AbstractViewRequest.class).withConstructor(
                wrappedRequest).createMock();
        applicationContext = createMock(ApplicationContext.class);
        applicationScope = new HashMap<String, Object>();
        contextResolver = createMock(ContextResolver.class);
        applicationScope.put(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE, contextResolver);

        expect(wrappedRequest.getApplicationContext()).andReturn(applicationContext).anyTimes();
        expect(applicationContext.getApplicationScope()).andReturn(applicationScope).anyTimes();
    }

    /**
     * Test method for {@link org.apache.tiles.request.AbstractViewRequest#dispatch(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException {
        Map<String, Object> requestScope = new HashMap<String, Object>();

        expect(contextResolver.getContext(request, "request")).andReturn(requestScope);
        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest, request, applicationContext, contextResolver);
        request.dispatch("/my/path.html");
        assertTrue((Boolean) requestScope.get(AbstractRequest.FORCE_INCLUDE_ATTRIBUTE_NAME));
        verify(wrappedRequest, request, applicationContext, contextResolver);
    }

    /**
     * Test method for {@link org.apache.tiles.request.AbstractViewRequest#include(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testInclude() throws IOException {
        Map<String, Object> requestScope = new HashMap<String, Object>();

        expect(contextResolver.getContext(request, "request")).andReturn(requestScope);
        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest, request, applicationContext, contextResolver);
        request.include("/my/path.html");
        assertTrue((Boolean) requestScope.get(AbstractRequest.FORCE_INCLUDE_ATTRIBUTE_NAME));
        verify(wrappedRequest, request, applicationContext, contextResolver);
    }

    /**
     * Test method for {@link org.apache.tiles.request.AbstractViewRequest#doInclude(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDoInclude() throws IOException {
        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest, request, applicationContext, contextResolver);
        request.doInclude("/my/path.html");
        verify(wrappedRequest, request, applicationContext, contextResolver);
    }

}
