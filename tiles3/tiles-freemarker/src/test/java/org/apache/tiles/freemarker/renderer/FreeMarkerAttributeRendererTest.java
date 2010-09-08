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
package org.apache.tiles.freemarker.renderer;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createMockBuilder;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.servlet.ServletApplicationContext;
import org.apache.tiles.request.servlet.ServletRequest;
import org.junit.Before;
import org.junit.Test;

import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.ObjectWrapper;

/**
 * Tests {@link FreeMarkerAttributeRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class FreeMarkerAttributeRendererTest {

    private static final String ATTR_APPLICATION_MODEL =
        ".freemarker.Application";

    private static final String ATTR_JSP_TAGLIBS_MODEL =
        ".freemarker.JspTaglibs";

    private static final String ATTR_REQUEST_MODEL = ".freemarker.Request";

    private static final String ATTR_REQUEST_PARAMETERS_MODEL =
        ".freemarker.RequestParameters";

    private FreeMarkerAttributeRenderer renderer;

    private ApplicationContext applicationContext;

    private ServletContext servletContext;

    private AttributeEvaluatorFactory attributeEvaluatorFactory;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        applicationContext = createMock(ServletApplicationContext.class);
        attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        servletContext = createMock(ServletContext.class);

        expect(applicationContext.getContext()).andReturn(servletContext);

        replay(applicationContext, servletContext);
        renderer = new FreeMarkerAttributeRenderer();
        renderer.setApplicationContext(applicationContext);
        renderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        renderer.setParameter("TemplatePath", "/");
        renderer.setParameter("NoCache", "true");
        renderer.setParameter("ContentType", "text/html");
        renderer.setParameter("template_update_delay", "0");
        renderer.setParameter("default_encoding", "ISO-8859-1");
        renderer.setParameter("number_format", "0.##########");
        renderer.commit();
    }

    /**
     * Tests {@link FreeMarkerAttributeRenderer#write(Object, org.apache.tiles.Attribute, org.apache.tiles.request.Request)}.
     * @throws IOException If something goes wrong.
     * @throws ServletException If something goes wrong.
     */
    @Test
    public void testWrite() throws IOException, ServletException {
        ApplicationContext applicationContext = createMock(ServletApplicationContext.class);
        AttributeEvaluatorFactory attributeEvaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        ServletContext servletContext = createMock(ServletContext.class);
        GenericServlet servlet = createMockBuilder(GenericServlet.class).createMock();
        ServletConfig servletConfig = createMock(ServletConfig.class);
        ObjectWrapper objectWrapper = createMock(ObjectWrapper.class);

        expect(servletConfig.getServletContext()).andReturn(servletContext);

        replay(servlet, servletConfig);
        servlet.init(servletConfig);
        ServletContextHashModel servletContextHashModel = new ServletContextHashModel(servlet, objectWrapper);

        expect(applicationContext.getContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getRealPath(isA(String.class))).andReturn(null).anyTimes();
        URL resource = getClass().getResource("/test.ftl");
        expect(servletContext.getResource(isA(String.class))).andReturn(resource).anyTimes();
        expect(servletContext.getAttribute(ATTR_APPLICATION_MODEL)).andReturn(servletContextHashModel);
        expect(servletContext.getAttribute(ATTR_JSP_TAGLIBS_MODEL)).andReturn(null);

        replay(applicationContext, servletContext, objectWrapper);

        FreeMarkerAttributeRenderer renderer = new FreeMarkerAttributeRenderer();
        renderer.setApplicationContext(applicationContext);
        renderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
        renderer.setParameter("TemplatePath", "/");
        renderer.setParameter("NoCache", "true");
        renderer.setParameter("ContentType", "text/html");
        renderer.setParameter("template_update_delay", "0");
        renderer.setParameter("default_encoding", "ISO-8859-1");
        renderer.setParameter("number_format", "0.##########");
        renderer.commit();

        ServletRequest request = createMock(ServletRequest.class);
        HttpServletRequest httpRequest = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        HttpRequestHashModel requestModel = new HttpRequestHashModel(httpRequest, response, objectWrapper);
        HttpRequestParametersHashModel parametersModel = new HttpRequestParametersHashModel(httpRequest);

        expect(request.getRequest()).andReturn(httpRequest);
        expect(request.getResponse()).andReturn(response);
        expect(request.getPrintWriter()).andReturn(printWriter);
        expect(httpRequest.getSession(false)).andReturn(null);
        expect(httpRequest.getAttribute(ATTR_REQUEST_MODEL)).andReturn(requestModel);
        expect(httpRequest.getAttribute(ATTR_REQUEST_PARAMETERS_MODEL)).andReturn(parametersModel);
        response.setContentType("text/html; charset=ISO-8859-1");
        response.setHeader(eq("Cache-Control"), isA(String.class));
        response.setHeader("Pragma", "no-cache");
        response.setHeader(eq("Expires"), isA(String.class));

        replay(attributeEvaluatorFactory, request, httpRequest, response);
        renderer.write("hello", null, request);
        stringWriter.close();
        assertTrue(stringWriter.toString().startsWith("Hello!"));
        verify(applicationContext, servletContext, attributeEvaluatorFactory,
                request, httpRequest, response, servlet, servletConfig,
                objectWrapper);
    }

    /**
     * Test method for
     * {@link FreeMarkerAttributeRenderer
     * #isRenderable(Object, org.apache.tiles.Attribute, org.apache.tiles.context.TilesRequestContext)}
     * .
     */
    @Test
    public void testIsRenderable() {
        replay(attributeEvaluatorFactory);
        assertTrue(renderer.isRenderable("/my/template.ftl", null, null));
        assertFalse(renderer.isRenderable("my/template.ftl", null, null));
        assertFalse(renderer.isRenderable("/my/template.jsp", null, null));
        assertFalse(renderer.isRenderable(0, null, null));
        verify(applicationContext, servletContext, attributeEvaluatorFactory);
    }

}
