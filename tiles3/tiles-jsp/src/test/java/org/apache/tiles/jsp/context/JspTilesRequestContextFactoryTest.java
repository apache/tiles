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

package org.apache.tiles.jsp.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.apache.tiles.request.servlet.ServletTilesRequestContext;
import org.easymock.classextension.EasyMock;

import junit.framework.TestCase;

/**
 * Tests {@link JspTilesRequestContextFactory}.
 *
 * @version $Rev$ $Date$
 */
public class JspTilesRequestContextFactoryTest extends TestCase {

    /**
     * The object under test.
     */
    private JspTilesRequestContextFactory factory;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        factory = new JspTilesRequestContextFactory();
    }

    /**
     * Tests
     * {@link JspTilesRequestContextFactory#createRequestContext(org.apache.tiles.request.ApplicationContext, Object...)}
     * .
     */
    public void testCreateRequestContextWithoutParent() {
        ApplicationContext context = EasyMock
                .createMock(ApplicationContext.class);
        PageContext pageContext = EasyMock.createMock(PageContext.class);
        HttpServletRequest httpRequest = EasyMock
                .createMock(HttpServletRequest.class);
        HttpServletResponse httpResponse = EasyMock
                .createMock(HttpServletResponse.class);
        EasyMock.expect(pageContext.getRequest()).andReturn(httpRequest);
        EasyMock.expect(pageContext.getResponse()).andReturn(httpResponse);
        EasyMock.replay(context, pageContext, httpRequest, httpResponse);
        JspTilesRequestContext request = (JspTilesRequestContext) factory
                .createRequestContext(context, pageContext);
        assertTrue("The enclosed request is not of the correct class", request
                .getWrappedRequest() instanceof ServletTilesRequestContext);
        EasyMock.verify(context, pageContext, httpRequest, httpResponse);
    }

    /**
     * Tests
     * {@link JspTilesRequestContextFactory#createRequestContext(org.apache.tiles.request.ApplicationContext, Object...)}
     * .
     */
    public void testCreateRequestContextWithParent() {
        ApplicationContext context = EasyMock
                .createMock(ApplicationContext.class);
        PageContext pageContext = EasyMock.createMock(PageContext.class);
        HttpServletRequest httpRequest = EasyMock
                .createMock(HttpServletRequest.class);
        HttpServletResponse httpResponse = EasyMock
                .createMock(HttpServletResponse.class);
        EasyMock.expect(pageContext.getRequest()).andReturn(httpRequest);
        EasyMock.expect(pageContext.getResponse()).andReturn(httpResponse);

        TilesRequestContextFactory parent = EasyMock
                .createMock(TilesRequestContextFactory.class);
        Request enclosedRequest = EasyMock
                .createMock(Request.class);
        EasyMock.expect(parent.createRequestContext(context, httpRequest,
                httpResponse)).andReturn(enclosedRequest);
        factory.setRequestContextFactory(parent);

        EasyMock.replay(context, pageContext, httpRequest, httpResponse, parent);
        JspTilesRequestContext request = (JspTilesRequestContext) factory
                .createRequestContext(context, pageContext);
        assertTrue("The enclosed request is not the correct one", request
                .getWrappedRequest() == enclosedRequest);
        EasyMock.verify(context, pageContext, httpRequest, httpResponse, parent);
    }
}
