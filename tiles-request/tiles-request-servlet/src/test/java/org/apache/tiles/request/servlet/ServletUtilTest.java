/**
 *
 */
package org.apache.tiles.request.servlet;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Test;

/**
 * Tests {@link ServletUtil}.
 *
 * @version $Rev$ $Date$
 */
public class ServletUtilTest {

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletUtil#wrapServletException(javax.servlet.ServletException, java.lang.String)}.
     */
    @Test
    public void testWrapServletException() {
        ServletException servletException = new ServletException();
        IOException exception = ServletUtil.wrapServletException(servletException, "my message");
        assertEquals(servletException, exception.getCause());
        assertEquals("my message", exception.getMessage());
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletUtil#wrapServletException(javax.servlet.ServletException, java.lang.String)}.
     */
    @Test
    public void testWrapServletExceptionWithCause() {
        Throwable cause = createMock(Throwable.class);

        replay(cause);
        ServletException servletException = new ServletException(cause);
        IOException exception = ServletUtil.wrapServletException(servletException, "my message");
        assertEquals(cause, exception.getCause());
        assertEquals("my message", exception.getMessage());
        verify(cause);
    }

    /**
     * Test method for {@link org.apache.tiles.request.servlet.ServletUtil#getApplicationContext(javax.servlet.ServletContext)}.
     */
    @Test
    public void testGetApplicationContext() {
        ServletContext servletContext = createMock(ServletContext.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(servletContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(applicationContext);

        replay(servletContext, applicationContext);
        assertEquals(applicationContext, ServletUtil.getApplicationContext(servletContext));
        verify(servletContext, applicationContext);
    }
}
