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
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Base tag for the tiles tags which interact with the container.
 * Provides standard support for security, and provides access
 * to the container and attribute context.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public abstract class ContainerTagSupport extends RoleSecurityTagSupport {

    /**
     * The log instance for this tag.
     */
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(ContainerTagSupport.class);

    /**
     * The Tiles container that can be used inside the tag.
     */
    protected TilesContainer container;

    /**
     * The attribute context to use to store and read attribute values.
     */
    protected AttributeContext attributeContext;

    /**
     * By default, all ContainerTags evaluate their body.  Subclasses may choose to be more selective.
     * In any case, children can rely upon the container and attributeContext being initialized if they
     * call <code>super.doStartTag()</code>
     *
     * @return <code>EVAL_BODY_BUFFERED</code>.
     * @throws JspException If the container has not been initialized.
     */
    public int doStartTag() throws JspException {
        container = TilesAccess.getContainer(pageContext.getServletContext());
        if (container != null) {
            startContext(pageContext);
            return EVAL_BODY_BUFFERED;
        } else {
            throw new JspException("TilesContainer not initialized");
        }
    }


    /** {@inheritDoc} */
    public int doEndTag() throws JspException {
        try {
            return super.doEndTag();
        } finally {
            endContext(pageContext);
        }
    }



    /** {@inheritDoc} */
    public void release() {
        super.release();
        this.container = null;
        this.attributeContext = null;
    }

    /**
     * Starts the context when entering the tag.
     *
     * @param context The page context to use.
     */
    protected void startContext(PageContext context) {
        if (container != null) {
            attributeContext = container.startContext(pageContext);
        }
    }

    /**
     * Ends the context when exiting the tag.
     *
     * @param context The page context to use.
     */
    protected void endContext(PageContext context) {
        if (attributeContext != null && container != null) {
            container.endContext(pageContext);
        }
    }
}
