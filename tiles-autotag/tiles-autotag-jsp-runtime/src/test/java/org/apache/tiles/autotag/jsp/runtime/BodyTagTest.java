/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tiles.autotag.jsp.runtime;

import static org.easymock.EasyMock.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.jsp.JspRequest;
import org.apache.tiles.request.util.ApplicationAccess;
import org.junit.Test;

/**
 * Tests {@link BodyTag}.
 *
 * @version $Rev$ $Date$
 */
public class BodyTagTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.jsp.runtime.BodyTag#doTag()}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testDoTag() throws IOException {
        PageContext pageContext = createMock(PageContext.class);
        JspFragment jspBody = createMock(JspFragment.class);
        BodyTag tag = createMockBuilder(BodyTag.class).createMock();
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        HttpServletRequest httpServletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        JspWriter jspWriter = createMock(JspWriter.class);

        expect(pageContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE,
                PageContext.APPLICATION_SCOPE)).andReturn(applicationContext);
        expect(pageContext.getRequest()).andReturn(httpServletRequest);
        expect(pageContext.getResponse()).andReturn(httpServletResponse);
        expect(pageContext.getOut()).andReturn(jspWriter);
        tag.execute(isA(JspRequest.class), isA(JspModelBody.class));

        replay(pageContext, jspBody, tag, applicationContext, httpServletRequest, httpServletResponse, jspWriter);
        tag.setJspContext(pageContext);
        tag.setJspBody(jspBody);
        tag.doTag();
        verify(pageContext, jspBody, tag, applicationContext, httpServletRequest, httpServletResponse, jspWriter);
    }

}
