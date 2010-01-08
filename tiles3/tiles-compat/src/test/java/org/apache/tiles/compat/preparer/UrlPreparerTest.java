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

package org.apache.tiles.compat.preparer;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.request.Request;
import org.easymock.EasyMock;

/**
 * Tests {@link UrlPreparer}.
 *
 * @version $Rev$ $Date$
 */
public class UrlPreparerTest extends TestCase {

    /**
     * The preparer to test.
     */
    private UrlPreparer preparer;

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        preparer = new UrlPreparer("/my/url.do");
    }

    /**
     * Test method for
     * {@link org.apache.tiles.compat.preparer.UrlPreparer#execute(
     * org.apache.tiles.request.Request, org.apache.tiles.AttributeContext)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    public void testExecute() throws IOException {
        Request requestContext = EasyMock.createMock(Request.class);
        AttributeContext attributeContext = EasyMock
                .createMock(AttributeContext.class);

        requestContext.include("/my/url.do");
        EasyMock
                .replay(requestContext, attributeContext);
        preparer.execute(requestContext, attributeContext);
        EasyMock.verify(requestContext, attributeContext);
    }
}
