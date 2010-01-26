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

package org.apache.tiles.velocity.template;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationAccess;
import org.apache.tiles.request.velocity.VelocityRequest;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewToolContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ImportAttributeDirective}.
 */
public class ImportAttributeDirectiveTest {

    /**
     * The model to test.
     */
    private ImportAttributeDirective model;

    /**
     * The template model.
     */
    private ImportAttributeModel tModel;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(ImportAttributeModel.class);
    }

    /**
     * Test method for {@link ImportAttributeDirective#render(InternalContextAdapter, Writer, Node)}.
     */
    @Test
    public void testExecute() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        ServletContext servletContext = createMock(ServletContext.class);
        InternalContextAdapter velocityContext = createMock(InternalContextAdapter.class);
        ViewToolContext viewContext = createMock(ViewToolContext.class);
        ASTMap astMap = createMock(ASTMap.class);
        Node node = createMock(Node.class);
        TilesContainer container = createMock(TilesContainer.class);
        Writer writer = new StringWriter();
        Map<String, Object> params = createParams();
        ApplicationContext applicationContext = createMock(ApplicationContext.class);

        expect(servletContext.getAttribute(ApplicationAccess.APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(applicationContext);
        expect(velocityContext.getInternalUserContext()).andReturn(viewContext);
        expect(viewContext.getRequest()).andReturn(request);
        expect(viewContext.getResponse()).andReturn(response);
        expect(viewContext.getServletContext()).andReturn(servletContext);
        expect(node.jjtGetChild(0)).andReturn(astMap);
        expect(astMap.value(velocityContext)).andReturn(params);
        tModel.execute(eq("myName"), eq("myScope"), eq("myToName"), eq(false), isA(VelocityRequest.class));

        replay(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap, applicationContext);
        initializeModel();
        model.render(velocityContext, writer, node);
        verify(tModel, servletContext, request, response, velocityContext, container, node, viewContext, astMap, applicationContext);
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new ImportAttributeDirective(tModel);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("scope", "myScope");
        params.put("ignore", false);
        params.put("name", "myName");
        params.put("toName", "myToName");
        return params;
    }
}
