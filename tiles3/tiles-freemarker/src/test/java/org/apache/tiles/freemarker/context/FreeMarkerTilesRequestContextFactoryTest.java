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

package org.apache.tiles.freemarker.context;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * Tests {@link FreeMarkerTilesRequestContextFactory}.
 *
 * @version $Rev$ $Date$
 */
public class FreeMarkerTilesRequestContextFactoryTest {

    /**
     * The object to test.
     */
    private FreeMarkerTilesRequestContextFactory factory;

    /**
     * Tests {@link FreeMarkerTilesRequestContextFactory#createRequestContext(ApplicationContext, Object...)}.
     *
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testCreateRequestContext() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel model = createMock(TemplateHashModel.class);
        StringWriter writer = new StringWriter();
        expect(template.getMacros()).andReturn(new HashMap<Object, Object>());
        TilesRequestContextFactory parentFactory = createMock(TilesRequestContextFactory.class);
        ApplicationContext applicationContext = createMock(ApplicationContext.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ObjectWrapper wrapper = createMock(ObjectWrapper.class);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(request, response, wrapper);
        expect(model.get("Request")).andReturn(requestModel);
        Request enclosedRequest = createMock(Request.class);
        expect(parentFactory.createRequestContext(applicationContext, request, response)).andReturn(enclosedRequest);
        replay(template, model, parentFactory, applicationContext, request, response, wrapper);
        Environment env = new Environment(template, model, writer);
        factory = new FreeMarkerTilesRequestContextFactory();
        factory.setRequestContextFactory(parentFactory);
        FreeMarkerTilesRequestContext context = (FreeMarkerTilesRequestContext) factory
                .createRequestContext(applicationContext, env);
        assertEquals(env, context.getEnvironment());
        assertEquals(enclosedRequest, context.getWrappedRequest());

    }

}
