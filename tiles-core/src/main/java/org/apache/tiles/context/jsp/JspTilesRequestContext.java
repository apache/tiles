/*
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
package org.apache.tiles.context.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesException;
import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.context.servlet.ServletTilesRequestContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * Context implementation used for executing tiles within a
 * jsp tag library.
 */
public class JspTilesRequestContext extends ServletTilesRequestContext
    implements TilesRequestContext {

    private static final Log LOG =
        LogFactory.getLog(JspTilesRequestContext.class);


    private PageContext pageContext;

    public JspTilesRequestContext(ServletContext context, PageContext pageContext) {
        super(context,
            (HttpServletRequest) pageContext.getRequest(),
            (HttpServletResponse) pageContext.getResponse());
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
     * @param path  Uri or Definition name to forward.
     * @param flush If the writer should be flushed before the include
     * @throws javax.servlet.ServletException - Thrown by call to pageContext.include()
     * @throws java.io.IOException            - Thrown by call to pageContext.include()
     */
    public void include(String path, boolean flush) throws IOException, ServletException {
        JspUtil.doInclude(pageContext, path, flush);
    }


}
