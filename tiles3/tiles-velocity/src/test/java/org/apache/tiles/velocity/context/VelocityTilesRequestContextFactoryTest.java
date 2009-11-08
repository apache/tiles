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

package org.apache.tiles.velocity.context;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.apache.velocity.context.Context;
import org.junit.Test;

/**
 * Tests {@link VelocityTilesRequestContextFactory}.
 */
public class VelocityTilesRequestContextFactoryTest {

    /**
     * The position of the writer in the request objects array.
     */
    private static final int WRITER_POSITION = 3;

    /**
     * The size of the request objects array.
     */
    private static final int REQUEST_OBJECTS_SIZE = 4;
    /**
     * The object to test.
     */
    private VelocityTilesRequestContextFactory factory;

    /**
     * Tests {@link VelocityTilesRequestContextFactory#createRequestContext(ApplicationContext, Object...)}.
     */
    @Test
    public void testCreateRequestContext() {
        StringWriter writer = new StringWriter();
        TilesRequestContextFactory parentFactory = createMock(TilesRequestContextFactory.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        Context velocityContext = createMock(Context.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        Request enclosedRequest = createMock(Request.class);
        expect(enclosedRequest.getRequestObjects()).andReturn(new Object[] {request, response});
        expect(parentFactory.createRequestContext(applicationContext, request, response)).andReturn(enclosedRequest);
        replay(parentFactory, enclosedRequest, applicationContext, velocityContext, request, response);
        factory = new VelocityTilesRequestContextFactory();
        factory.setRequestContextFactory(parentFactory);
        VelocityTilesRequestContext context = (VelocityTilesRequestContext) factory
                .createRequestContext(applicationContext, velocityContext,
                        request, response, writer);
        assertEquals(enclosedRequest, context.getWrappedRequest());
        Object[] requestItems = context.getRequestObjects();
        assertEquals(REQUEST_OBJECTS_SIZE, requestItems.length);
        assertEquals(velocityContext, requestItems[0]);
        assertEquals(request, requestItems[1]);
        assertEquals(response, requestItems[2]);
        assertEquals(writer, requestItems[WRITER_POSITION]);
        verify(parentFactory, enclosedRequest, applicationContext, velocityContext, request, response);
    }
}
