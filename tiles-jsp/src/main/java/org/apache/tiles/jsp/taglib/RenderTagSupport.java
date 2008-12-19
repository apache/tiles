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
package org.apache.tiles.jsp.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Support for all tags which render (an attribute, a template, or definition).
 * </p>
 * <p>
 * Properly invokes the defined preparer and invokes the abstract render method
 * upon completion.
 * </p>
 * This tag takes special care to ensure that the attribute context is reset to
 * it's original state after the execution of the tag is complete. This ensures
 * that all all included attributes in subsequent tiles are scoped properly and
 * do not bleed outside their intended scope.
 *
 * @version $Rev$ $Date$
 * @deprecated Use {@link RenderTag}.
 */
@Deprecated
public abstract class RenderTagSupport extends RenderTag {

    /**
     * Execute the tag by invoking the preparer, if defined, and then
     * rendering.
     *
     * @throws TilesJspException if a jsp exception occurs.
     * @throws IOException if an io exception occurs.
     * @deprecated Use {@link #render()}.
     */
    protected void execute() throws TilesJspException, IOException {
        if (preparer != null) {
            container.prepare(preparer, pageContext);
        }
        render();
        if (flush) {
            pageContext.getOut().flush();
        }
    }

    /**
     * Checks if the user is inside the specified role.
     *
     * @return <code>true</code> if the user is allowed to have the tag
     * rendered.
     * @deprecated Implement access allowance in your own tag.
     */
    protected boolean isAccessAllowed() {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        return (role == null || req.isUserInRole(role));
    }
}
