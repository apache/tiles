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
package org.apache.tiles.request.jsp.autotag;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.request.ApplicationAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.jsp.JspRequest;
import org.apache.tiles.request.jsp.autotag.JspAutotagRuntime;
import org.apache.tiles.request.jsp.autotag.JspModelBody;
import org.junit.Test;

public class JspAutotagRuntimeTest {
    @Test
    public void testCreateRequest() {
        JspFragment jspBody = createMock(JspFragment.class);
        PageContext pageContext = createMock(PageContext.class);
        JspTag parent = createMock(JspTag.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        HttpServletRequest httpServletRequest = createMock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = createMock(HttpServletResponse.class);
        expect(pageContext.getAttribute(
                ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE,
                PageContext.APPLICATION_SCOPE)).andReturn(applicationContext);
        expect(pageContext.getRequest()).andReturn(httpServletRequest);
        expect(pageContext.getResponse()).andReturn(httpServletResponse);
        replay(jspBody, pageContext, parent, applicationContext, httpServletRequest, httpServletResponse);
        JspAutotagRuntime runtime = new JspAutotagRuntime();
        runtime.setJspBody(jspBody);
        runtime.setJspContext(pageContext);
        runtime.setParent(parent);
        runtime.doTag();
        Request jspRequest = runtime.createRequest();
        assertTrue(jspRequest instanceof JspRequest);
        verify(jspBody, pageContext, parent, applicationContext, httpServletRequest, httpServletResponse);
    }

    @Test
    public void testCreateModelBody() {
        JspFragment jspBody = createMock(JspFragment.class);
        JspContext jspContext = createMock(JspContext.class);
        JspTag parent = createMock(JspTag.class);
        JspWriter writer = createMock(JspWriter.class);
        expect(jspContext.getOut()).andReturn(writer);
        replay(jspBody, jspContext, parent, writer);
        JspAutotagRuntime runtime = new JspAutotagRuntime();
        runtime.setJspBody(jspBody);
        runtime.setJspContext(jspContext);
        runtime.setParent(parent);
        runtime.doTag();
        ModelBody jspModelBody = runtime.createModelBody();
        assertTrue(jspModelBody instanceof JspModelBody);
        verify(jspBody, jspContext, parent, writer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameter() {
        JspFragment jspBody = createMock(JspFragment.class);
        JspContext jspContext = createMock(JspContext.class);
        JspTag parent = createMock(JspTag.class);
        replay(jspBody, jspContext, parent);
        JspAutotagRuntime runtime = new JspAutotagRuntime();
        runtime.setJspBody(jspBody);
        runtime.setJspContext(jspContext);
        runtime.setParent(parent);
        runtime.doTag();
        runtime.getParameter("test", null);
        verify(jspBody, jspContext, parent);
    }
}
