/**
 *
 */
package org.apache.tiles.request.jsp;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Test;

/**
 * Tests {@link JspUtil}.
 *
 * @version $Rev$ $Date$
 */
public class JspUtilTest {

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspUtil#getApplicationContext(javax.servlet.jsp.JspContext)}.
     */
    @Test
    public void testGetApplicationContext() {
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        JspContext jspContext = createMock(JspContext.class);

        expect(jspContext.getAttribute(ApplicationAccess
                .APPLICATION_CONTEXT_ATTRIBUTE, PageContext.APPLICATION_SCOPE))
                .andReturn(applicationContext);

        replay(applicationContext, jspContext);
        assertEquals(applicationContext, JspUtil.getApplicationContext(jspContext));
        verify(applicationContext, jspContext);
    }
}
