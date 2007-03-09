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
package org.apache.tiles.servlet;

import javax.servlet.*;
import java.util.Set;
import java.util.Enumeration;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;

/**
 * @version $Rev$ $Date$
 */
@SuppressWarnings("deprecation")
public class ServletContextAdapter implements ServletContext {

    private ServletContext rootContext;
    private ServletConfig config;


    public ServletContextAdapter(ServletConfig config) {
        this.rootContext = config.getServletContext();
        this.config = config;
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("deprecation")
    public Servlet getServlet(String string) throws ServletException {
        return rootContext.getServlet(string);
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    public Enumeration getServlets() {
        return rootContext.getServlets();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    public Enumeration getServletNames() {
        return rootContext.getServletNames();
    }

    public void log(String string) {
        rootContext.log(string);
    }

    @SuppressWarnings("deprecation")
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
        String parm = config.getInitParameter(string);
        if(parm == null) {
            parm = rootContext.getInitParameter(string);
        }
        return parm;
    }

    @SuppressWarnings("unchecked")
    public Enumeration getInitParameterNames() {
        return new CompositeEnumeration(config.getInitParameterNames(),
            rootContext.getInitParameterNames());
    }

    public Object getAttribute(String string) {
        return rootContext.getAttribute(string);
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    class CompositeEnumeration implements Enumeration {

        private Enumeration first;
        private Enumeration second;


        public CompositeEnumeration(Enumeration first, Enumeration second) {
            this.first = first;
            this.second = second;
        }

        public boolean hasMoreElements() {
            return first.hasMoreElements() || second.hasMoreElements();
        }

        public Object nextElement() {
            if(first.hasMoreElements()) {
                return first.nextElement();
            }

            return second.nextElement();
        }
    }
}
