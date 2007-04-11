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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Base tag for the tiles tags which provides standard support for security.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public abstract class RoleSecurityTagSupport extends BodyTagSupport {

    /**
     * The log instance for this tag.
     */
    private static final Log LOG = LogFactory.getLog(RoleSecurityTagSupport.class);

    /**
     * The role to check. If the user is in the specified role, the tag is taken
     * into account; otherwise, the tag is ignored (skipped).
     */
    private String role;

    /**
     * Returns the role to check. If the user is in the specified role, the tag is
     * taken into account; otherwise, the tag is ignored (skipped).
     *
     * @return The role to check.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role to check. If the user is in the specified role, the tag is
     * taken into account; otherwise, the tag is ignored (skipped).
     *
     * @param role The role to check.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /** {@inheritDoc} */
    public int doEndTag() throws JspException {
        try {
            if (isAccessAllowed()) {
                execute();
            }
        } catch (TilesException e) {
            String message = "Error executing tag: " + e.getMessage();
            LOG.error(message, e);
            throw new JspException(message, e);
        } catch (IOException io) {
            String message = "IO Error executing tag: " + io.getMessage();
            LOG.error(message, io);
            throw new JspException(message, io);
        }

        return EVAL_PAGE;
    }



    /** {@inheritDoc} */
    public void release() {
        super.release();
        this.role = null;
    }

    /**
     * Executes the tag. It is called inside {@link #doEndTag()}.
     *
     * @throws TilesException If something goews wrong during the use of Tiles.
     * @throws JspException If something goes wrong during rendering.
     * @throws IOException If something goes wrong during writing content.
     */
    protected abstract void execute() throws TilesException, JspException, IOException;

    /**
     * Checks if the user is inside the specified role.
     *
     * @return <code>true</code> if the user is allowed to have the tag
     * rendered.
     */
    protected boolean isAccessAllowed() {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        return (role == null || req.isUserInRole(role));
    }
}
