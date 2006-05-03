/*
 * $Id$
 *
 * Copyright 1999-2006 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.tiles.TilesContext;

/**
 * Servlet-bsed implementation of the TilesContext interface.
 *
 * @version $Rev$ $Date$
 */
public class ServletTilesContext implements TilesContext {

    private ServletContext servletContext;
    
    private ServletRequest request;
    
    private ServletResponse response;
    
    /** Creates a new instance of ServletTilesContext */
    public ServletTilesContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /** Creates a new instance of ServletTilesContext */
    public ServletTilesContext(ServletContext servletContext, ServletRequest request) {
        this.servletContext = servletContext;
        this.request = request;
    }

    /** Creates a new instance of ServletTilesContext */
    public ServletTilesContext(ServletContext servletContext, 
            ServletRequest request,
            ServletResponse response) {
        this.servletContext = servletContext;
        this.request = request;
        this.response = response;
    }

    public Map getApplicationScope() {
        Enumeration e = servletContext.getAttributeNames();
        Map contextMap = new HashMap();
        while (e.hasMoreElements()) {
            String key = (String)  e.nextElement();
            contextMap.put(key, servletContext.getAttribute(key));
        }
        
        return contextMap;
    }

    public Map getHeader() {
        return null;
    }

    public Map getHeaderValues() {
        return null;
    }

    public Map getInitParams() {
        Enumeration e = servletContext.getInitParameterNames();
        Map paramMap = new HashMap();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            paramMap.put(key, servletContext.getInitParameter(key));
        }
        
        return paramMap;
    }

    public Map getParam() {
        return null;
    }

    public Map getParamValues() {
        return request.getParameterMap();
    }

    public Map getRequestScope() {
        Enumeration e = request.getAttributeNames();
        Map params = new HashMap();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            params.put(key, request.getAttribute(key));
        }
        
        return params;
    }

    public Map getSessionScope() {
        return null;
    }

    public void dispatch(String path) throws IOException, Exception {
        RequestDispatcher rd = request.getRequestDispatcher(path);
        try {
            rd.forward(request, response);
        } catch (ServletException ex) {
            throw new Exception("Error forwarding request.", ex);
        }
    }

    public void include(String path) throws IOException, Exception {
        RequestDispatcher rd = request.getRequestDispatcher(path);
        try {
            rd.include(request, response);
        } catch (ServletException ex) {
            throw new Exception("Error including path.", ex);
        }
    }

    public URL getResource(String path) throws MalformedURLException {
        return servletContext.getResource(path);
    }

    public Locale getRequestLocale() {
        return request.getLocale();
    }
    
}
