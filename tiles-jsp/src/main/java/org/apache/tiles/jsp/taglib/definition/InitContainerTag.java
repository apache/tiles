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


package org.apache.tiles.jsp.taglib.definition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.factory.TilesContainerFactory;
import org.apache.tiles.jsp.taglib.PutAttributeTag;
import org.apache.tiles.jsp.taglib.PutAttributeTagParent;
import org.apache.tiles.mgmt.MutableTilesContainer;

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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Init definitions impl.
 *
 * @version $Rev$ $Date$
 */
public class InitContainerTag extends BodyTagSupport
    implements PutAttributeTagParent {

    /**
     * The logging object.
     */
    private static final Log LOG =
        LogFactory.getLog(InitContainerTag.class);

    /**
     * The container factory class name to use.
     */
    private String containerFactory;

    /**
     * Init parameters map.
     */
    private Map<String, String> initParameters;


    /**
     * Returns the container factory class name.
     *
     * @return The container factory class name.
     */
    public String getContainerFactory() {
        return containerFactory;
    }

    /**
     * Sets the container factory class name.
     *
     * @param containerFactory The container factory class name.
     */
    public void setContainerFactory(String containerFactory) {
        this.containerFactory = containerFactory;
    }


    /** {@inheritDoc} */
    public void processNestedTag(PutAttributeTag nestedTag) throws JspException {
        initParameters.put(nestedTag.getName(), nestedTag.getValue().toString());
    }

    /** {@inheritDoc} */
    public void release() {
        super.release();
        containerFactory = null;
        initParameters = null;
    }

    /** {@inheritDoc} */
    public int doStartTag() {
        initParameters = new HashMap<String, String>();
        return EVAL_BODY_INCLUDE;
    }

    /** {@inheritDoc} */
    // TODO Add a MutableContainer so that this can be done?
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

        for (Map.Entry<String, String> entry : initParameters.entrySet()) {
            context.setInitParameter(entry.getKey(), entry.getValue());
        }

        try {
            MutableTilesContainer mutableContainer =
                TilesContainerFactory.getFactory(context)
                    .createMutableTilesContainer(context);
            TilesAccess.setContainer(context, mutableContainer);
        } catch (TilesException e) {
            throw new JspException("Error creating tiles container.", e);
        }

        return EVAL_PAGE;
    }




    /**
     * A servlet context created "on the fly" for container initialization.
     */
    public static class RuntimeConfiguredContext implements ServletContext {

        /**
         * The root servlet context.
         */
        private ServletContext rootContext;

        /**
         * The custom init parameters.
         */
        private Hashtable<String, String> initParameters;


        /**
         * Constructor.
         *
         * @param rootContext The "real" servlet context.
         */
        @SuppressWarnings("unchecked")
        public RuntimeConfiguredContext(ServletContext rootContext) {
            this.rootContext = rootContext;
            this.initParameters = new Hashtable<String, String>();
            Enumeration<String> enumeration = rootContext
                    .getInitParameterNames();
            while (enumeration.hasMoreElements()) {
                String paramName = enumeration.nextElement();
                initParameters.put(paramName, rootContext
                        .getInitParameter(paramName));
            }
        }

        /** {@inheritDoc} */
        public ServletContext getContext(String string) {
            return rootContext.getContext(string);
        }

        /** {@inheritDoc} */
        public int getMajorVersion() {
            return rootContext.getMajorVersion();
        }

        /** {@inheritDoc} */
        public int getMinorVersion() {
            return rootContext.getMinorVersion();
        }

        /** {@inheritDoc} */
        public String getMimeType(String string) {
            return rootContext.getMimeType(string);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public Set getResourcePaths(String string) {
            return rootContext.getResourcePaths(string);
        }

        /** {@inheritDoc} */
        public URL getResource(String string) throws MalformedURLException {
            return rootContext.getResource(string);
        }

        /** {@inheritDoc} */
        public InputStream getResourceAsStream(String string) {
            return rootContext.getResourceAsStream(string);
        }

        /** {@inheritDoc} */
        public RequestDispatcher getRequestDispatcher(String string) {
            return rootContext.getRequestDispatcher(string);
        }

        /** {@inheritDoc} */
        public RequestDispatcher getNamedDispatcher(String string) {
            return rootContext.getNamedDispatcher(string);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("deprecation")
        public Servlet getServlet(String string) throws ServletException {
            return rootContext.getServlet(string);
        }

        /** {@inheritDoc} */
        @SuppressWarnings({ "deprecation", "unchecked" })
        public Enumeration getServlets() {
            return rootContext.getServlets();
        }

        /** {@inheritDoc} */
        @SuppressWarnings({ "deprecation", "unchecked" })
        public Enumeration getServletNames() {
            return rootContext.getServletNames();
        }

        /** {@inheritDoc} */
        public void log(String string) {
            rootContext.log(string);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("deprecation")
        public void log(Exception exception, String string) {
            rootContext.log(exception, string);
        }

        /** {@inheritDoc} */
        public void log(String string, Throwable throwable) {
            rootContext.log(string, throwable);
        }

        /** {@inheritDoc} */
        public String getRealPath(String string) {
            return rootContext.getRealPath(string);
        }

        /** {@inheritDoc} */
        public String getServerInfo() {
            return rootContext.getServerInfo();
        }

        /**
         * Takes the init parameters either from the custom parameters or from
         * the root context.
         *
         * @param string The parameter name.
         * @return The value of the parameter.
         * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
         */
        public String getInitParameter(String string) {
            return initParameters.get(string);
        }

        /**
         * Sets an init parameter value.
         *
         * @param name The name of the parameter.
         * @param value The value of the parameter.
         */
        public void setInitParameter(String name, String value) {
            initParameters.put(name, value);
        }

        /**
         * Returns init parameter names, including the custom and the original
         * ones.
         *
         * @return An enumeration of String representing the init parameter
         * names.
         * @see javax.servlet.ServletContext#getInitParameterNames()
         */
        @SuppressWarnings("unchecked")
        public Enumeration getInitParameterNames() {
            return initParameters.keys();
        }

        /** {@inheritDoc} */
        public Object getAttribute(String string) {
            return rootContext.getAttribute(string);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public Enumeration getAttributeNames() {
            return rootContext.getAttributeNames();
        }

        /** {@inheritDoc} */
        public void setAttribute(String string, Object object) {
            rootContext.setAttribute(string, object);
        }

        /** {@inheritDoc} */
        public void removeAttribute(String string) {
            rootContext.removeAttribute(string);
        }

        /** {@inheritDoc} */
        public String getServletContextName() {
            return rootContext.getServletContextName();
        }

        /**
         * Composes an enumeration and an iterator into a single enumeration.
         */
        @SuppressWarnings("unchecked")
        static class CompositeEnumeration implements Enumeration {

            /**
             * The first enumeration to consider.
             */
            private Enumeration first;

            /**
             * The second enumeration to consider.
             */
            private Iterator second;


            /**
             * Constructor.
             *
             * @param first The first enumeration to consider.
             * @param second The second enumeration to consider.
             */
            public CompositeEnumeration(Enumeration first, Iterator second) {
                this.first = first;
                this.second = second;
            }

            /** {@inheritDoc} */
            public boolean hasMoreElements() {
                return first.hasMoreElements() || second.hasNext();
            }

            /** {@inheritDoc} */
            public Object nextElement() {
                if (first.hasMoreElements()) {
                    return first.nextElement();
                }

                return second.next();
            }
        }
    }

}
