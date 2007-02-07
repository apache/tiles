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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.TilesException;
import org.apache.tiles.taglib.PutAttributeTag;
import org.apache.tiles.taglib.PutAttributeTagParent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TryCatchFinally;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Support for all tags which render (a template, or definition).
 * </p>
 * Properly invokes the defined preparer and invokes the abstract
 * render method upon completion.
 * </p>
 * This tag takes special care to ensure that the component context is
 * reset to it's original state after the execution of the tag is
 * complete. This ensures that all all included attributes in subsequent
 * tiles are scoped properly and do not bleed outside their intended
 * scope.
 *
 * @since Tiles 2.0
 * @version $Rev$ $Date$
 */
public abstract class RenderTagSupport extends ContainerTagSupport
    implements TryCatchFinally, PutAttributeTagParent {
    
    private static Log LOG = LogFactory.getLog(RenderTagSupport.class);

    protected String preparer;
    protected boolean flush;
    protected boolean ignore;

    private Map<String, ComponentAttribute> originalState;

    public String getPreparer() {
        return preparer;
    }

    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    public boolean isFlush() {
        return flush;
    }

    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }


    public void release() {
        preparer = null;
        flush = false;
        ignore = false;
        super.release();
    }

        public int doStartTag() {
        super.doStartTag();
        cacheState();
        return isAccessAllowed() ? EVAL_BODY_BUFFERED : SKIP_BODY;
    }

    public void doCatch(Throwable throwable) throws Throwable {
        LOG.error("Error during rendering", throwable);
    }

    public void doFinally() {
        restoreState();
    }

    /**
     * Execute the tag by invoking the preparer, if defined, and then
     * rendering.
     *
     * @throws TilesException if a prepare or render exception occurs.
     * @throws JspException if a jsp exception occurs.
     * @throws IOException if an io exception occurs.
     */
    protected void execute() throws TilesException, JspException, IOException {
        if (preparer != null) {
            container.prepare(pageContext, preparer);
        }
        render();
    }

    /**
     * Render the specified content.
     *
     * @throws TilesException if a prepare or render exception occurs.
     * @throws JspException if a jsp exception occurs.
     * @throws IOException if an io exception occurs.
     */
    protected abstract void render() throws JspException, TilesException, IOException;

    /**
     * Process nested &lg;put&gt; tag.
     * <p/>
     * Places the value of the nested tag within the
     * {@link org.apache.tiles.ComponentContext}.It is the responsibility
     * of the descendent to check security.  Tags extending
     * the {@link ContainerTagSupport} will automatically provide
     * the appropriate security.
     * </p>
     *
     * @param nestedTag the put tag desciendent.
     */
    public void processNestedTag(PutAttributeTag nestedTag) {
        ComponentAttribute attribute = new ComponentAttribute(
            nestedTag.getValue(), nestedTag.getRole(),
            nestedTag.getType());

        componentContext.putAttribute(
            nestedTag.getName(),
            attribute
        );
    }

    private void cacheState() {
        originalState = new HashMap<String, ComponentAttribute>();
        Iterator<String> i = componentContext.getAttributeNames();
        while(i.hasNext()) {
            String name = i.next();
            ComponentAttribute original = componentContext.getAttribute(name);
            ComponentAttribute a = new ComponentAttribute(
                original.getValue(), original.getRole(), original.getType()
            );
            originalState.put(name, a);
        }
    }

    private void restoreState() {
        originalState.clear();
        originalState.putAll(originalState);
    }

}
