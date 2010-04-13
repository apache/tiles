/**
 *
 */
package org.apache.tiles.request.util;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.scope.ContextResolver;
import org.junit.Test;

/**
 * Tests {@link RequestWrapper}.
 *
 * @version $Rev$ $Date$
 */
public class RequestWrapperTest {

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getWrappedRequest()}.
     */
    @Test
    public void testGetWrappedRequest() {
        Request wrappedRequest = createMock(Request.class);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(wrappedRequest, request.getWrappedRequest());
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getHeader()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetHeader() {
        Request wrappedRequest = createMock(Request.class);
        Map<String, String> header = createMock(Map.class);

        expect(wrappedRequest.getHeader()).andReturn(header);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(header, request.getHeader());
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getHeaderValues()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetHeaderValues() {
        Request wrappedRequest = createMock(Request.class);
        Map<String, String[]> header = createMock(Map.class);

        expect(wrappedRequest.getHeaderValues()).andReturn(header);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(header, request.getHeaderValues());
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getContext(java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetContext() {
        Request wrappedRequest = createMock(Request.class);
        Map<String, Object> context = createMock(Map.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> applicationScope = createMock(Map.class);

        RequestWrapper request = new RequestWrapper(wrappedRequest);

        expect(wrappedRequest.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.get(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE)).andReturn(resolver);
        expect(resolver.getContext(request, "one")).andReturn(context);

        replay(wrappedRequest, context, applicationContext, resolver, applicationScope);
        assertEquals(context, request.getContext("one"));
        verify(wrappedRequest, context, applicationContext, resolver, applicationScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getNativeScopes()}.
     */
    @Test
    public void testGetNativeScopes() {
        Request wrappedRequest = createMock(Request.class);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertNull(request.getNativeScopes());
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getAvailableScopes()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAvailableScopes() {
        Request wrappedRequest = createMock(Request.class);
        Map<String, Object> context = createMock(Map.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        ContextResolver resolver = createMock(ContextResolver.class);
        Map<String, Object> applicationScope = createMock(Map.class);

        RequestWrapper request = new RequestWrapper(wrappedRequest);

        expect(wrappedRequest.getApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getApplicationScope()).andReturn(applicationScope);
        expect(applicationScope.get(ApplicationAccess.CONTEXT_RESOLVER_ATTRIBUTE)).andReturn(resolver);
        String[] scopes = new String[] {"one", "two", "three"};
        expect(resolver.getAvailableScopes(request)).andReturn(scopes);

        replay(wrappedRequest, context, applicationContext, resolver, applicationScope);
        assertArrayEquals(scopes, request.getAvailableScopes());
        verify(wrappedRequest, context, applicationContext, resolver, applicationScope);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getApplicationContext()}.
     */
    @Test
    public void testGetApplicationContext() {
        Request wrappedRequest = createMock(Request.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(wrappedRequest.getApplicationContext()).andReturn(applicationContext);

        replay(wrappedRequest, applicationContext);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(applicationContext, request.getApplicationContext());
        verify(wrappedRequest, applicationContext);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#dispatch(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDispatch() throws IOException {
        Request wrappedRequest = createMock(Request.class);

        wrappedRequest.dispatch("/my/path.html");

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        request.dispatch("/my/path.html");
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#include(java.lang.String)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testInclude() throws IOException {
        Request wrappedRequest = createMock(Request.class);

        wrappedRequest.include("/my/path.html");

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        request.include("/my/path.html");
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getOutputStream()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetOutputStream() throws IOException {
        Request wrappedRequest = createMock(Request.class);
        OutputStream stream = createMock(OutputStream.class);

        expect(wrappedRequest.getOutputStream()).andReturn(stream);

        replay(wrappedRequest, stream);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(stream, request.getOutputStream());
        verify(wrappedRequest, stream);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetWriter() throws IOException {
        Request wrappedRequest = createMock(Request.class);
        Writer writer = createMock(Writer.class);

        expect(wrappedRequest.getWriter()).andReturn(writer);

        replay(wrappedRequest, writer);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(writer, request.getWriter());
        verify(wrappedRequest, writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getPrintWriter()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testGetPrintWriter() throws IOException {
        Request wrappedRequest = createMock(Request.class);
        PrintWriter writer = createMock(PrintWriter.class);

        expect(wrappedRequest.getPrintWriter()).andReturn(writer);

        replay(wrappedRequest, writer);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(writer, request.getPrintWriter());
        verify(wrappedRequest, writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#isResponseCommitted()}.
     */
    @Test
    public void testIsResponseCommitted() {
        Request wrappedRequest = createMock(Request.class);

        expect(wrappedRequest.isResponseCommitted()).andReturn(Boolean.TRUE);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertTrue(request.isResponseCommitted());
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#setContentType(java.lang.String)}.
     */
    @Test
    public void testSetContentType() {
        Request wrappedRequest = createMock(Request.class);

        wrappedRequest.setContentType("text/html");

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        request.setContentType("text/html");
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getParam()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParam() {
        Request wrappedRequest = createMock(Request.class);
        Map<String, String> param = createMock(Map.class);

        expect(wrappedRequest.getParam()).andReturn(param);

        replay(wrappedRequest, param);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(param, request.getParam());
        verify(wrappedRequest, param);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getParamValues()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetParamValues() {
        Request wrappedRequest = createMock(Request.class);
        Map<String, String[]> param = createMock(Map.class);

        expect(wrappedRequest.getParamValues()).andReturn(param);

        replay(wrappedRequest, param);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(param, request.getParamValues());
        verify(wrappedRequest, param);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getRequestLocale()}.
     */
    @Test
    public void testGetRequestLocale() {
        Request wrappedRequest = createMock(Request.class);
        Locale param = Locale.ITALY;

        expect(wrappedRequest.getRequestLocale()).andReturn(param);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertEquals(param, request.getRequestLocale());
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#isUserInRole(java.lang.String)}.
     */
    @Test
    public void testIsUserInRole() {
        Request wrappedRequest = createMock(Request.class);

        expect(wrappedRequest.isUserInRole("myrole")).andReturn(Boolean.TRUE);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertTrue(request.isUserInRole("myrole"));
        verify(wrappedRequest);
    }

    /**
     * Test method for {@link org.apache.tiles.request.util.RequestWrapper#getRequestObjects()}.
     */
    @Test
    public void testGetRequestObjects() {
        Request wrappedRequest = createMock(Request.class);
        String[] param = new String[] {"one", "two", "three"};

        expect(wrappedRequest.getRequestObjects()).andReturn(param);

        replay(wrappedRequest);
        RequestWrapper request = new RequestWrapper(wrappedRequest);
        assertArrayEquals(param, request.getRequestObjects());
        verify(wrappedRequest);
    }

}
