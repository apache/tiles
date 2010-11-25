/**
 *
 */
package org.apache.tiles.autotag.jsp.runtime;

import static org.easymock.EasyMock.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.jsp.JspRequest;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Test;

/**
 * Tests {@link BodylessTag}.
 *
 * @version $Rev$ $Date$
 */
public class BodylessTagTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.jsp.runtime.BodylessTag#doTag()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDoTag() throws IOException {
        PageContext pageContext = createMock(PageContext.class);
        BodylessTag tag = createMockBuilder(BodylessTag.class).createMock();
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        HttpServletRequest httpServletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);

        expect(pageContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE,
                PageContext.APPLICATION_SCOPE)).andReturn(applicationContext);
        expect(pageContext.getRequest()).andReturn(httpServletRequest);
        expect(pageContext.getResponse()).andReturn(httpServletResponse);
        tag.execute(isA(JspRequest.class));

        replay(pageContext, tag, applicationContext, httpServletRequest, httpServletResponse);
        tag.setJspContext(pageContext);
        tag.doTag();
        verify(pageContext, tag, applicationContext, httpServletRequest, httpServletResponse);
    }

}
