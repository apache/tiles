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

package org.apache.tiles.servlet.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.easymock.EasyMock;

import junit.framework.TestCase;

/**
 * Tests {@link ServletUtil}.
 *
 * @version $Rev$ $Date$
 */
public class ServletUtilTest extends TestCase {

    /**
     * Tests {@link ServletUtil#getContainer(ServletContext)}.
     */
    public void testGetContainer() {
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        EasyMock.expect(context.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE)).andReturn(container);
        EasyMock.replay(context, container);
        assertEquals(container, ServletUtil.getContainer(context));
        EasyMock.verify(context, container);
    }

    /**
     * Tests {@link ServletUtil#getContainer(ServletContext, String)}.
     */
    public void testGetContainerWithKey() {
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        EasyMock.expect(context.getAttribute("myKey")).andReturn(container);
        EasyMock.replay(context, container);
        assertEquals(container, ServletUtil.getContainer(context, "myKey"));
        EasyMock.verify(context, container);
    }

    /**
     * Tests {@link ServletUtil#setContainer(ServletContext, TilesContainer)}.
     */
    public void testSetContainer() {
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        context.setAttribute(TilesAccess.CONTAINER_ATTRIBUTE, container);
        EasyMock.replay(context, container);
        ServletUtil.setContainer(context, container);
        EasyMock.verify(context, container);
    }

    /**
     * Tests
     * {@link ServletUtil#setContainer(ServletContext, TilesContainer, String)}.
     */
    public void testSetContainerWithKey() {
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        context.setAttribute("myKey", container);
        EasyMock.replay(context, container);
        ServletUtil.setContainer(context, container, "myKey");
        EasyMock.verify(context, container);
    }

    /**
     * Tests
     * {@link ServletUtil#setCurrentContainer(ServletRequest, ServletContext, String)}.
     */
    public void testSetCurrentContainer() {
        ServletRequest request = EasyMock.createMock(ServletRequest.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        EasyMock.expect(context.getAttribute("myKey")).andReturn(container);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                container);
        EasyMock.expect(request.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        EasyMock.replay(request, context, container);
        ServletUtil.setCurrentContainer(request, context, "myKey");
        assertTrue("The containers are not the same", ServletUtil
                .getCurrentContainer(request, context) == container);
    }

    /**
     * Tests
     * {@link ServletUtil#setCurrentContainer(ServletRequest, ServletContext, TilesContainer)}.
     */
    public void testSetCurrentContainerWithContainer() {
        ServletRequest request = EasyMock.createMock(ServletRequest.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer container = EasyMock.createMock(TilesContainer.class);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                container);
        EasyMock.expect(request.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(container);
        EasyMock.replay(request, context, container);
        ServletUtil.setCurrentContainer(request, context, container);
        assertTrue("The containers are not the same", ServletUtil
                .getCurrentContainer(request, context) == container);
    }

    /**
     * Tests {@link ServletUtil#getCurrentContainer(ServletRequest, ServletContext)}.
     */
    public void testGetCurrentContainer() {
        ServletRequest request = EasyMock.createMock(ServletRequest.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        TilesContainer defaultContainer = EasyMock.createMock(
                TilesContainer.class);
        TilesContainer alternateContainer = EasyMock.createMock(
                TilesContainer.class);
        EasyMock.expect(request.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(null);
        EasyMock.expect(context.getAttribute(TilesAccess.CONTAINER_ATTRIBUTE))
                .andReturn(defaultContainer);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                defaultContainer);
        request.setAttribute(ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                alternateContainer);
        EasyMock.expect(request.getAttribute(ServletUtil
                .CURRENT_CONTAINER_ATTRIBUTE_NAME)).andReturn(alternateContainer);
        EasyMock.replay(request, context, defaultContainer, alternateContainer);
        TilesContainer currentContainer = ServletUtil.getCurrentContainer(
                request, context);
        assertTrue("The containers are not the same",
                currentContainer == defaultContainer);
        ServletUtil.setCurrentContainer(request, context, alternateContainer);
        currentContainer = ServletUtil.getCurrentContainer(request, context);
        EasyMock.verify(request, context, defaultContainer, alternateContainer);
        assertTrue("The containers are not the same",
                currentContainer == alternateContainer);
    }
}
