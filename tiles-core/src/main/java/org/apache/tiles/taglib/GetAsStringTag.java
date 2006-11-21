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
package org.apache.tiles.taglib;

import org.apache.tiles.taglib.AttributeTagSupport;
import org.apache.tiles.taglib.ComponentConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Retrieve the value of the specified component/template attribute property,
 * and render it to the current JspWriter as a String.
 * The usual toString() conversion is applied on the found value.
 */
public class GetAsStringTag extends AttributeTagSupport implements ComponentConstants {

    private String role = null;

    public void release() {
        super.release();
        role = null;
    }

    /**
     * Set role.
     *
     * @param role The role the user must be in to store content.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Get role.
     *
     * @return Role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Print the attribute.
     *
     * @throws JspException On error processing tag.
     * @throws IOException  if io error occurs.
     */
    public void execute() throws JspException, IOException {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        if (role != null && !req.isUserInRole(role)) {
            return;
        }

        pageContext.getOut().print(attribute.getValue().toString());

    }
}
