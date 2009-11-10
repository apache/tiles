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

package org.apache.tiles.jsp;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import junit.framework.TestCase;

import org.apache.tiles.ArrayStack;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.jsp.taglib.TilesJspException;
import org.apache.tiles.servlet.context.ServletUtil;
import org.easymock.classextension.EasyMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests {@link JspUtil}.
 *
 * @version $Rev$ $Date$
 */
public class JspUtilTest extends TestCase {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Tests {@link ServletUtil#getContainer(ServletContext)}.
     */
    public void testGetContainer() {
        PageContext context = EasyMock.createMock(PageContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        EasyMock.expect(
                context.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE,
                        PageContext.APPLICATION_SCOPE)).andReturn(container);
        EasyMock.replay(context, container);
        assertEquals(container, JspUtil.getContainer(context));
        EasyMock.verify(context, container);
    }

    /**
     * Tests {@link ServletUtil#getContainer(ServletContext, String)}.
     */
    public void testGetContainerWithKey() {
        PageContext context = EasyMock.createMock(PageContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        EasyMock.expect(
                context.getAttribute("myKey", PageContext.APPLICATION_SCOPE))
                .andReturn(container);
        EasyMock.replay(context, container);
        assertEquals(container, JspUtil.getContainer(context, "myKey"));
        EasyMock.verify(context, container);
    }

    /**
     * Tests {@link ServletUtil#setContainer(ServletContext, TilesContainer)}.
     */
    public void testSetContainer() {
        PageContext context = EasyMock.createMock(PageContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        context.setAttribute(TilesAccess.CONTAINER_ATTRIBUTE, container,
                PageContext.APPLICATION_SCOPE);
        EasyMock.replay(context, container);
        JspUtil.setContainer(context, container);
        EasyMock.verify(context, container);
    }

    /**
     * Tests
     * {@link ServletUtil#setContainer(ServletContext, TilesContainer, String)}.
     */
    public void testSetContainerWithKey() {
        PageContext context = EasyMock.createMock(PageContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        context.setAttribute("myKey", container, PageContext.APPLICATION_SCOPE);
        EasyMock.replay(context, container);
        JspUtil.setContainer(context, container, "myKey");
        EasyMock.verify(context, container);
    }

    /**
     * Tests
     * {@link JspUtil#setCurrentContainer(javax.servlet.jsp.JspContext, String)}.
     */
    public void testSetCurrentContainer() {
        PageContext pageContext = EasyMock.createMock(PageContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        EasyMock.expect(
                pageContext
                        .getAttribute("myKey", PageContext.APPLICATION_SCOPE))
                .andReturn(container);
        pageContext.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                container, PageContext.REQUEST_SCOPE);
        EasyMock.expect(pageContext.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE))
                .andReturn(container);
        EasyMock.replay(pageContext, container);
        JspUtil.setCurrentContainer(pageContext, "myKey");
        assertTrue("The containers are not the same", JspUtil
                .getCurrentContainer(pageContext) == container);
        EasyMock.verify(pageContext, container);
    }

    /**
     * Tests
     * {@link JspUtil#setCurrentContainer(javax.servlet.jsp.JspContext, TilesContainer)}.
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
     * Tests {@link JspUtil#getCurrentContainer(javax.servlet.jsp.JspContext)}.
     */
    public void testGetCurrentContainer() {
        PageContext pageContext = EasyMock.createMock(PageContext.class);
        TilesContainer defaultContainer = EasyMock.createMock(
                TilesContainer.class);
        TilesContainer alternateContainer = EasyMock.createMock(
                TilesContainer.class);
        EasyMock.expect(pageContext.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE))
                .andReturn(null);
        EasyMock.expect(
                pageContext.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE,
                        PageContext.APPLICATION_SCOPE)).andReturn(
                defaultContainer);
        pageContext.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                defaultContainer, PageContext.REQUEST_SCOPE);
        pageContext.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                alternateContainer, PageContext.REQUEST_SCOPE);
        EasyMock.expect(pageContext.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE))
                .andReturn(alternateContainer);
        EasyMock.replay(pageContext, defaultContainer, alternateContainer);
        TilesContainer currentContainer = JspUtil.getCurrentContainer(pageContext);
        assertTrue("The containers are not the same",
                currentContainer == defaultContainer);
        JspUtil.setCurrentContainer(pageContext, alternateContainer);
        currentContainer = JspUtil.getCurrentContainer(pageContext);
        EasyMock.verify(pageContext, defaultContainer, alternateContainer);
        assertTrue("The containers are not the same",
                currentContainer == alternateContainer);
    }

    /**
     * Tests {@link JspUtil#getComposeStack(javax.servlet.jsp.JspContext)}.
     */
    public void testGetComposeStack() {
        PageContext pageContext = EasyMock.createMock(PageContext.class);

        ArrayStack<Object> stack = new ArrayStack<Object>();

        EasyMock.expect(
                pageContext.getAttribute(
                        "org.apache.tiles.template.COMPOSE_STACK",
                        PageContext.REQUEST_SCOPE)).andReturn(null);
        EasyMock.expect(
                pageContext.getAttribute(
                        "org.apache.tiles.template.COMPOSE_STACK",
                        PageContext.REQUEST_SCOPE)).andReturn(stack);
        pageContext.setAttribute(EasyMock
                .eq("org.apache.tiles.template.COMPOSE_STACK"), EasyMock
                .isA(ArrayStack.class), EasyMock.eq(PageContext.REQUEST_SCOPE));

        EasyMock.replay(pageContext);
        assertTrue(JspUtil.getComposeStack(pageContext).isEmpty());
        assertEquals(stack, JspUtil.getComposeStack(pageContext));
        EasyMock.verify(pageContext);
    }

    /**
     * Tests {@link JspUtil#getScope(String)}.
     * @throws TilesJspException If something goes wrong.
     */
    public void testGetScope() throws TilesJspException {
        assertEquals(PageContext.PAGE_SCOPE, JspUtil.getScope(null));
        assertEquals(PageContext.PAGE_SCOPE, JspUtil.getScope("page"));
        assertEquals(PageContext.REQUEST_SCOPE, JspUtil.getScope("request"));
        assertEquals(PageContext.SESSION_SCOPE, JspUtil.getScope("session"));
        assertEquals(PageContext.APPLICATION_SCOPE, JspUtil.getScope("application"));

        try {
            JspUtil.getScope("blah");
            fail("A TilesJspException should have been raised");
        } catch (TilesJspException e) {
            if (log.isDebugEnabled()) {
                log.debug("Got correct exception, ignoring", e);
            }
        }
    }
}
