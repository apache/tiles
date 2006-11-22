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
 *
 */


package org.apache.tiles.taglib.definition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.factory.TilesContainerFactory;
import org.apache.tiles.mgmt.MutableTilesContainer;
import org.apache.tiles.taglib.PutTagParent;
import org.apache.tiles.taglib.PutTag;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Init definitions impl.
 *
 * @version $Rev$ $Date$
 */
public class InitContainerTag extends BodyTagSupport
    implements PutTagParent {

    private static final Log LOG =
        LogFactory.getLog(InitContainerTag.class);

    private String containerFactory;
    private Map<String, String> initParameters;


    public String getContainerFactory() {
        return containerFactory;
    }

    public void setContainerFactory(String containerFactory) {
        this.containerFactory = containerFactory;
    }


    public void processNestedTag(PutTag nestedTag) throws JspException {
        initParameters.put(nestedTag.getName(), nestedTag.getValue().toString());
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        containerFactory = null;
        initParameters = null;
    }

    public int doStartTag() {
        initParameters = new HashMap<String, String>();
        return EVAL_BODY_INCLUDE;
    }

    /**
     * TODO Add a MutableContainer so that this can be done?
     * Do start tag.
     */
    public int doEndTag() throws JspException {
        TilesContainer container =
            TilesAccess.getContainer(pageContext.getServletContext());

        if (container != null) {
            LOG.warn("TilesContainer allready instantiated for this context. Ignoring request to define.");
            return SKIP_BODY;
        }

        RuntimeConfiguredContext context =
            new RuntimeConfiguredContext(pageContext.getServletContext());

        if (containerFactory != null) {
            context.setInitParameter(
                TilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM,
                containerFactory);
        }

        for(Map.Entry<String, String> entry : initParameters.entrySet()) {
            context.setInitParameter(entry.getKey(), entry.getValue());
        }

        try {
            MutableTilesContainer mutableContainer =
                TilesContainerFactory.getFactory(context)
                    .createMutableTilesContainer(context);
            TilesAccess.setContainer(context, mutableContainer);
        }
        catch (TilesException e) {
            throw new JspException("Error creating tiles container.", e);
        }

        return EVAL_PAGE;
    }




    @SuppressWarnings("deprecated")
    public class RuntimeConfiguredContext implements ServletContext {

        private ServletContext rootContext;
        private Map<String, String> initParameters;


        public RuntimeConfiguredContext(ServletContext rootContext) {
            this.rootContext = rootContext;
            this.initParameters = new HashMap<String, String>();
        }

        public ServletContext getContext(String string) {
            return rootContext.getContext(string);
        }

        public int getMajorVersion() {
            return rootContext.getMajorVersion();
        }

        public int getMinorVersion() {
            return rootContext.getMinorVersion();
        }

        public String getMimeType(String string) {
            return rootContext.getMimeType(string);
        }

        public Set getResourcePaths(String string) {
            return rootContext.getResourcePaths(string);
        }

        public URL getResource(String string) throws MalformedURLException {
            return rootContext.getResource(string);
        }

        public InputStream getResourceAsStream(String string) {
            return rootContext.getResourceAsStream(string);
        }

        public RequestDispatcher getRequestDispatcher(String string) {
            return rootContext.getRequestDispatcher(string);
        }

        public RequestDispatcher getNamedDispatcher(String string) {
            return rootContext.getNamedDispatcher(string);
        }

        public Servlet getServlet(String string) throws ServletException {
            return rootContext.getServlet(string);
        }

        public Enumeration getServlets() {
            return rootContext.getServlets();
        }

        public Enumeration getServletNames() {
            return rootContext.getServletNames();
        }

        public void log(String string) {
            rootContext.log(string);
        }

        public void log(Exception exception, String string) {
            rootContext.log(exception, string);
        }

        public void log(String string, Throwable throwable) {
            rootContext.log(string, throwable);
        }

        public String getRealPath(String string) {
            return rootContext.getRealPath(string);
        }

        public String getServerInfo() {
            return rootContext.getServerInfo();
        }

        public String getInitParameter(String string) {
            if (initParameters.containsKey(string)) {
                return initParameters.get(string);
            }
            return rootContext.getInitParameter(string);
        }

        public void setInitParameter(String name, String value) {
            initParameters.put(name, value);
        }

        public Enumeration getInitParameterNames() {
            return rootContext.getInitParameterNames();
        }

        public Object getAttribute(String string) {
            return rootContext.getAttribute(string);
        }

        public Enumeration getAttributeNames() {
            return rootContext.getAttributeNames();
        }

        public void setAttribute(String string, Object object) {
            rootContext.setAttribute(string, object);
        }

        public void removeAttribute(String string) {
            rootContext.removeAttribute(string);
        }

        public String getServletContextName() {
            return rootContext.getServletContextName();
        }
    }

}
