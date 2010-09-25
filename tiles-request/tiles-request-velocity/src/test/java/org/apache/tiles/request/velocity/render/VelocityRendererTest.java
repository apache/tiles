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
package org.apache.tiles.request.velocity.render;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.render.TypeDetectingRenderer;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.velocity.render.VelocityRenderer;
import org.apache.velocity.Template;
import org.apache.velocity.tools.view.VelocityView;
import org.apache.velocity.tools.view.ViewToolContext;
import org.junit.Test;

/**
 * Tests {@link VelocityRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class VelocityRendererTest {

    /**
     * Tests {@link VelocityRenderer#render(String, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testRender() throws IOException {
        VelocityView view = createMock(VelocityView.class);
        ServletRequest request = createMock(ServletRequest.class);
        HttpServletRequest httpRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ViewToolContext context = createMock(ViewToolContext.class);
        Template template = createMock(Template.class);
        Writer writer = createMock(Writer.class);

        expect(request.getRequest()).andReturn(httpRequest);
        expect(request.getResponse()).andReturn(response);
        expect(view.createContext(httpRequest, response)).andReturn(context);
        expect(view.getTemplate("/test.vm")).andReturn(template);
        expect(request.getWriter()).andReturn(writer);
        view.merge(template, context, writer);

        replay(view, request, httpRequest, response, context, template, writer);
        TypeDetectingRenderer renderer = new VelocityRenderer(view);
        renderer.render("/test.vm", request);
        verify(view, request, httpRequest, response, context, template, writer);
    }

    /**
     * Test method for
     * {@link VelocityRenderer#isRenderable(String, org.apache.tiles.request.Request)}
     * .
     */
    @Test
    public void testIsRenderable() {
        VelocityView view = createMock(VelocityView.class);
        replay(view);
        TypeDetectingRenderer renderer = new VelocityRenderer(view);
        assertTrue(renderer.isRenderable("/my/template.vm", null));
        assertFalse(renderer.isRenderable("my/template.vm", null));
        assertFalse(renderer.isRenderable("/my/template.jsp", null));
        verify(view);
    }

}
