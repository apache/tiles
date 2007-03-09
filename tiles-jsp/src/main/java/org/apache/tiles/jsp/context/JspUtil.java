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
package org.apache.tiles.jsp.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for working within a Jsp environment.
 *
 * @version $Rev$ $Date$
 */
public class JspUtil {

    private static final Log LOG =
        LogFactory.getLog(JspUtil.class);

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
            Class<?>[] args = new Class<?>[]{String.class, boolean.class};
            include = PageContext.class.getMethod("include", args);
        } catch (NoSuchMethodException e) {
            LOG.debug("Could not find JSP 2.0 include method.  Using old one that doesn't support " +
                "configurable flushing.", e);
        }
    }

    public static void doInclude(PageContext pageContext, String uri, boolean flush)
        throws JspException {

        try {
            // perform include with new JSP 2.0 method that supports flushing
            if (include != null) {
                include.invoke(pageContext, uri, flush);
                return;
            }
        } catch (IllegalAccessException e) {
            LOG.debug("Could not find JSP 2.0 include method.  Using old one.", e);
        } catch (InvocationTargetException e) {
            LOG.debug("Unable to execute JSP 2.0 include method.  Trying old one.", e);
        }


        try {
            pageContext.include(uri);
        }
        catch (IOException e) {
            throw new JspException("IOException while including page.", e);
        } catch (ServletException e) {
            throw new JspException("ServletException while including page.", e);
        }

    }


}
