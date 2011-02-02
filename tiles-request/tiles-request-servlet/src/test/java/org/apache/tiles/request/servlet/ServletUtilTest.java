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
     * Test method for {@link ServletUtil#wrapServletException(ServletException, String)}.
     */
    @Test
    public void testWrapServletException() {
        ServletException servletException = new ServletException();
        IOException exception = ServletUtil.wrapServletException(servletException, "my message");
        assertEquals(servletException, exception.getCause());
        assertEquals("my message", exception.getMessage());
    }

    /**
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
     * Test method for {@link ServletUtil#getApplicationContext(ServletContext)}.
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
