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

import javax.servlet.jsp.PageContext;

import junit.framework.TestCase;

import org.apache.tiles.jsp.taglib.TilesJspException;
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
