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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.BasicAttributeContext;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.AbstractTilesApplicationContextFactory;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.TilesContainerFactory;
import org.apache.tiles.jsp.context.JspUtil;
import org.apache.tiles.jsp.taglib.TilesBodyTag;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Init definitions impl.
 *
 * @version $Rev$ $Date$
 * @deprecated Don't use it, to initialize Tiles use
 * {@link org.apache.tiles.startup.TilesInitializer} and the various ways to
 * initialize Tiles, like {@link org.apache.tiles.web.startup.TilesServlet}.
 */
@Deprecated
public class InitContainerTag extends TilesBodyTag {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory.getLogger(InitContainerTag.class);

    /**
     * The container factory class name to use.
     */
    private String containerFactory;

    /**
     * The key under which the container will be stored.
     */
    private String containerKey;


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

    /**
     * Returns the key under which the container will be stored.
     *
     * @return the containerKey The container key.
     * @since 2.1.0
     */
    public String getContainerKey() {
        return containerKey;
    }

    /**
     * Sets the key under which the container will be stored.
     *
     * @param containerKey the containerKey The container key.
     * @since 2.1.0
     */
    public void setContainerKey(String containerKey) {
        this.containerKey = containerKey;
    }

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        containerFactory = null;
        containerKey = null;
    }

    /** {@inheritDoc} */
    public int doStartTag() {
        Stack<Object> composeStack = JspUtil.getComposeStack(pageContext);
        composeStack.push(new BasicAttributeContext());
        return EVAL_BODY_INCLUDE;
    }

    /** {@inheritDoc} */
    // TODO Add a MutableContainer so that this can be done?
    public int doEndTag() {
        Stack<Object> composeStack = JspUtil.getComposeStack(pageContext);
        AttributeContext attributeContext = (AttributeContext) composeStack.pop();

        TilesContainer container =
            JspUtil.getContainer(pageContext, containerKey);

        if (container != null) {
            log.warn("TilesContainer already instantiated for this context under key '"
                    + containerKey + "'. Ignoring request to define.");
            return SKIP_BODY;
        }

        RuntimeConfiguredContext context =
            new RuntimeConfiguredContext(pageContext.getServletContext());

        if (containerFactory != null) {
            context.setInitParameter(
                AbstractTilesContainerFactory.CONTAINER_FACTORY_INIT_PARAM,
                containerFactory);
        }

        // This is to provide compatibility with Tiles 2.0.x
        context.setInitParameter(
                TilesContainerFactory.CONTAINER_FACTORY_MUTABLE_INIT_PARAM,
                "true");

        for (String name : attributeContext.getLocalAttributeNames()) {
            context.setInitParameter(name, (String) attributeContext
                    .getAttribute(name).getValue());
        }

        TilesApplicationContext applicationContext = new ServletTilesApplicationContext(
                context);
        AbstractTilesApplicationContextFactory acFactory = AbstractTilesApplicationContextFactory
                .createFactory(applicationContext);
        applicationContext = acFactory.createApplicationContext(context);

        TilesContainer mutableContainer = AbstractTilesContainerFactory
                .getTilesContainerFactory(applicationContext).createContainer(
                        applicationContext);
        JspUtil.setContainer(pageContext, mutableContainer, containerKey);

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
        public Servlet getServlet(String string) throws ServletException {
            return rootContext.getServlet(string);
        }

        /** {@inheritDoc} */
        @SuppressWarnings({ "unchecked" })
        public Enumeration getServlets() {
            return rootContext.getServlets();
        }

        /** {@inheritDoc} */
        @SuppressWarnings({ "unchecked" })
        public Enumeration getServletNames() {
            return rootContext.getServletNames();
        }

        /** {@inheritDoc} */
        public void log(String string) {
            rootContext.log(string);
        }

        /** {@inheritDoc} */
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

        /** {@inheritDoc} */
        public String getContextPath() {
            return rootContext.getContextPath();
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
