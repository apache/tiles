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
package org.apache.tiles.autotag.jsp.runtime;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.jsp.JspRequest;

/**
 * Base class for a tag without body.
 *
 * @version $Rev$ $Date$
 */
public abstract class BodylessTag extends SimpleTagSupport {

    @Override
    public void doTag() throws IOException {
        JspContext pageContext = getJspContext();
        Request request = JspRequest.createServletJspRequest(
                org.apache.tiles.request.jsp.JspUtil
                        .getApplicationContext(pageContext),
                (PageContext) pageContext);
        execute(request);
    }

    /**
     * Executes the tag.
     *
     * @param request The request.
     * @throws IOException If something goes wrong.
     */
    protected abstract void execute(Request request) throws IOException;
}
