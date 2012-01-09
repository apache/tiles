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
package org.apache.tiles.request.jsp;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.ApplicationAccess;
import org.apache.tiles.request.ApplicationContext;
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
