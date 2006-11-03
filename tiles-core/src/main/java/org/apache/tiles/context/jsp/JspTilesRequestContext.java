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
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.servlet.ServletTilesRequestContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;

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

    public void dispatch(String path) throws TilesException {
        include(path);
    }

    public void include(String path) throws TilesException {
        try {
            JspUtil.doInclude(pageContext, path, false);
        } catch (JspException e) {
            throw new TilesException(e);
        }
    }

}
