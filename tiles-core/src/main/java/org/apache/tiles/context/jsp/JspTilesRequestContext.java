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
package org.apache.tiles.context.jsp;

import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.TilesException;
import org.apache.tiles.context.servlet.ServletTilesRequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

/**
 * Context implementation used for executing tiles within a
 * jsp tag library.
 *
 */
public class JspTilesRequestContext extends ServletTilesRequestContext
        implements TilesRequestContext {

    private static final Log LOG =
        LogFactory.getLog(JspTilesRequestContext.class);

    /**
     * JSP 2.0 include method to use which supports configurable flushing.
     */
    private static Method include = null;

    /**
     * Initialize the include variable with the
     * JSP 2.0 method if available.
     */
    static {
        try {
            // get version of include method with flush argument
            Class[] args = new Class[]{String.class, boolean.class};
            include = PageContext.class.getMethod("include", args);
        } catch (NoSuchMethodException e) {
            LOG.debug("Could not find JSP 2.0 include method.  Using old one that doesn't support " +
                    "configurable flushing.", e);
        }
    }

    private PageContext pageContext;

    public JspTilesRequestContext(ServletContext context, PageContext pageContext) {
        super(context,
              (HttpServletRequest)pageContext.getRequest(),
              (HttpServletResponse)pageContext.getResponse());
        this.pageContext = pageContext;
    }



    public void include(String path) throws TilesException {
        try {
            include(path, false);
        } catch (IOException e) {
            throw new TilesException(e);
        } catch (ServletException e) {
            throw new TilesException(e);
        }
    }

     /**
     * Do an include of specified page using PageContext.include().
     * <p/>
     * This method is used by the Tiles package when an include is required.
     * The Tiles package can use indifferently any form of this method.
     *
     * @param path   Uri or Definition name to forward.
     * @param flush If the writer should be flushed before the include
     * @throws javax.servlet.ServletException - Thrown by call to pageContext.include()
     * @throws java.io.IOException      - Thrown by call to pageContext.include()
     */
    public void include(String path, boolean flush) throws IOException, ServletException {

        try {
            // perform include with new JSP 2.0 method that supports flushing
            if (include != null) {
                include.invoke(pageContext, path, flush);
                return;
            }
        } catch (IllegalAccessException e) {
            LOG.debug("Could not find JSP 2.0 include method.  Using old one.", e);
        } catch (InvocationTargetException e) {
            LOG.debug("Unable to execute JSP 2.0 include method.  Trying old one.", e);
        }

        pageContext.include(path);
    }


}
