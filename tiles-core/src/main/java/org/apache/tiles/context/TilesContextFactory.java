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

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.tiles.TilesContext;
import org.apache.tiles.context.portlet.PortletTilesContext;
import org.apache.tiles.context.servlet.ServletTilesContext;

/**
 * Creates an instance of the appropriate TilesContext implementation.
 *
 * @version $Rev$ $Date$
 */
public class TilesContextFactory {
    
    /**
     * Creates a TilesContext from parameters found in the Servlet environment.
     */
    public static TilesContext getInstance(Object context, 
            Object request) {
        
        if (context instanceof ServletContext) {
            return new ServletTilesContext((ServletContext) context,
                                           (HttpServletRequest) request);
        } else if (context instanceof PortletContext) {
            return new PortletTilesContext((PortletContext) context,
                                           (PortletRequest) request);
        } else {
            throw new IllegalArgumentException("Invalid context specified. " 
                    + context.getClass().getName());
        }
    }

    public static TilesContext getInstance(Object context, Object request,
            Object response) {
        
        if (context instanceof ServletContext) {
            return new ServletTilesContext((ServletContext) context,
                                           (HttpServletRequest) request,
                                           (HttpServletResponse) response);
        } else if (context instanceof PortletContext) {
            return new PortletTilesContext((PortletContext) context,
                                           (PortletRequest) request,
                                           (PortletResponse) response);
        } else {
            throw new IllegalArgumentException("Invalid context specified. " 
                    + context.getClass().getName());
        }
    }

    public static TilesContext getInstance(Object context) {
        
        if (context instanceof ServletContext) {
            return new ServletTilesContext((ServletContext)context);
        } else if (context instanceof PortletContext) {
            return new PortletTilesContext((PortletContext)context);
        } else {
            throw new IllegalArgumentException("Invalid context specified. " 
                    + context.getClass().getName());
        }
    }

}
