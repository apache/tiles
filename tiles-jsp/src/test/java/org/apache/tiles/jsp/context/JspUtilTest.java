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

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.servlet.context.ServletUtil;
import org.easymock.classextension.EasyMock;

import junit.framework.TestCase;

/**
 * Tests {@link JspUtil}.
 *
 * @version $Rev$ $Date$
 */
public class JspUtilTest extends TestCase {

    /**
     * Tests
     * {@link JspUtil#setCurrentContainer(PageContext, String)}.
     */
    public void testSetCurrentContainer() {
        PageContext pageContext = EasyMock.createMock(PageContext.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        EasyMock.expect(pageContext.getServletContext()).andReturn(context);
        EasyMock.expect(context.getAttribute("myKey")).andReturn(container);
        pageContext.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                container, PageContext.REQUEST_SCOPE);
        EasyMock.expect(pageContext.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE))
                .andReturn(container);
        EasyMock.replay(pageContext, context, container);
        JspUtil.setCurrentContainer(pageContext, "myKey");
        assertTrue("The containers are not the same", JspUtil
                .getCurrentContainer(pageContext) == container);
        EasyMock.verify(pageContext, context, container);
    }

    /**
     * Tests
     * {@link JspUtil#setCurrentContainer(PageContext, TilesContainer)}.
     */
    public void testSetCurrentContainerWithContainer() {
        PageContext pageContext = EasyMock.createMock(PageContext.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        pageContext.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                container, PageContext.REQUEST_SCOPE);
        EasyMock.expect(pageContext.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE))
                .andReturn(container);
        EasyMock.replay(pageContext, context, container);
        JspUtil.setCurrentContainer(pageContext, container);
        assertTrue("The containers are not the same", JspUtil
                .getCurrentContainer(pageContext) == container);
        EasyMock.verify(pageContext, context, container);
    }

    /**
     * Tests {@link JspUtil#getCurrentContainer(PageContext)}.
     */
    public void testGetCurrentContainer() {
        PageContext pageContext = EasyMock.createMock(PageContext.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer defaultContainer = EasyMock.createMock(
                TilesContainer.class);
        TilesContainer alternateContainer = EasyMock.createMock(
                TilesContainer.class);
        EasyMock.expect(pageContext.getServletContext()).andReturn(context);
        EasyMock.expect(pageContext.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE))
                .andReturn(null);
        EasyMock.expect(context.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE))
                .andReturn(defaultContainer);
        pageContext.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                defaultContainer, PageContext.REQUEST_SCOPE);
        pageContext.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                alternateContainer, PageContext.REQUEST_SCOPE);
        EasyMock.expect(pageContext.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE))
                .andReturn(alternateContainer);
        EasyMock.replay(pageContext, context, defaultContainer, alternateContainer);
        TilesContainer currentContainer = JspUtil.getCurrentContainer(pageContext);
        assertTrue("The containers are not the same",
                currentContainer == defaultContainer);
        JspUtil.setCurrentContainer(pageContext, alternateContainer);
        currentContainer = JspUtil.getCurrentContainer(pageContext);
        EasyMock.verify(pageContext, context, defaultContainer, alternateContainer);
        assertTrue("The containers are not the same",
                currentContainer == alternateContainer);
    }
}
