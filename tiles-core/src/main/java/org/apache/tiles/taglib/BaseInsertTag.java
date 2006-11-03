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

package org.apache.tiles.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.context.BasicComponentContext;
import org.apache.tiles.context.jsp.JspUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the base abstract class for all Tiles composition JSP tags. The tag's
 * body content can consist of &lt;tiles:put&gt; and &lt;tiles:putList&gt; tags,
 * which are accessed by &lt;tiles:attribute&gt;, &lt;tiles:getAsString&gt; and
 * &lt;tiles:importAttribute&gt; in the template.
 *
 * @version $Rev$ $Date$
 */
public abstract class BaseInsertTag extends DefinitionTagSupport implements
    PutTagParent, ComponentConstants, PutListTagParent {

    /**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(BaseInsertTag.class);

    /* JSP Tag attributes */

    /**
     * Flush attribute value.
     */
    protected boolean flush = true;

    /**
     * Are errors ignored. This is the property for attribute 'ignore'. Default
     * value is false, which throw an exception. Only 'attribute not found'
     * errors are ignored.
     */
    protected boolean isErrorIgnored = false;

    /* Internal properties */
    /**
     * Does the end tag need to be processed. Default value is true. Boolean set
     * in case of ignored errors.
     */
    protected boolean processEndTag = true;

    /**
     * Current component context.
     */
    protected BasicComponentContext cachedCurrentContext;

    /**
     * Final handler of tag methods.
     */
    protected TagHandler tagHandler = null;

    /**
     * Trick to allows inner classes to access pageContext.
     */
    protected PageContext pageContext = null;

    /**
     * Container within which we execute.
     */
    protected TilesContainer container;

    /**
     * Reset member values for reuse. This method calls super.release(), which
     * invokes TagSupport.release(), which typically does nothing.
     */
    public void release() {

        super.release();

        flush = true;
        template = null;
        role = null;
        isErrorIgnored = false;

        releaseInternal();
    }

    /**
     * Reset internal member values for reuse.
     */
    protected void releaseInternal() {
        cachedCurrentContext = null;
        processEndTag = true;
        // pageContext = null; // orion doesn't set it between two tags
        tagHandler = null;
    }

    /**
     * Set the current page context. Called by the page implementation prior to
     * doStartTag().
     * <p/>
     * Needed to allow inner classes to access pageContext.
     */
    public void setPageContext(PageContext pc) {
        this.pageContext = pc;
        super.setPageContext(pc);
    }

    /**
     * Get the pageContext property.
     */
    public PageContext getPageContext() {
        return pageContext;
    }

    /**
     * Set flush.
     */
    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    /**
     * Get flush.
     */
    public boolean getFlush() {
        return flush;
    }

    /**
     * Set flush. Method added for compatibility with JSP1.1
     */
    public void setFlush(String flush) {
        this.flush = (Boolean.valueOf(flush).booleanValue());
    }

    /**
     * Set ignore.
     */
    public void setIgnore(boolean ignore) {
        this.isErrorIgnored = ignore;
    }

    /**
     * Get ignore.
     */
    public boolean getIgnore() {
        return isErrorIgnored;
    }

    // ///////////////////////////////////////////////////////////////////////

    /**
     * Add a body attribute. Erase any attribute with same name previously set.
     */
    public void putAttribute(String name, Object value) {
        tagHandler.putAttribute(name, value);
    }

    /**
     * Process nested &lg;put&gt; tag. Method calls by nested &lg;put&gt; tags.
     * Nested list is added to current list. If role is defined, it is checked
     * immediately.
     */
    public void processNestedTag(PutTag nestedTag) throws JspException {
        // Check role
        HttpServletRequest request = (HttpServletRequest) pageContext
            .getRequest();
        String role = nestedTag.getRole();
        if (role != null && !request.isUserInRole(role)) {
            // not allowed : skip attribute
            return;
        }

        putAttribute(nestedTag.getName(), new ComponentAttribute(nestedTag
            .getRealValue(), nestedTag.getRole(), nestedTag.getType()));
    }

    /**
     * Process nested &lg;putList&gt; tag. Method calls by nested
     * &lg;putList&gt; tags. Nested list is added to sub-component attributes If
     * role is defined, it is checked immediately.
     */
    public void processNestedTag(PutListTag nestedTag) throws JspException {
        // Check role
        HttpServletRequest request = (HttpServletRequest) pageContext
            .getRequest();
        String role = nestedTag.getRole();
        if (role != null && !request.isUserInRole(role)) {
            // not allowed : skip attribute
            return;
        }

        // Check if a name is defined
        if (nestedTag.getName() == null) {
            throw new JspException(
                "Error - PutList : attribute name is not defined. It is mandatory as the list is added as attribute of 'insert'.");
        }

        // now add attribute to enclosing parent (i.e. : this object).
        putAttribute(nestedTag.getName(), nestedTag.getList());
    }

    /**
     * Method calls by nested &lg;putList&gt; tags. A new list is added to
     * current insert object.
     */
    public void putAttribute(PutListTag nestedTag) throws JspException {
        // Check role
        HttpServletRequest request = (HttpServletRequest) pageContext
            .getRequest();
        String role = nestedTag.getRole();
        if (role != null && !request.isUserInRole(role)) {
            // not allowed : skip attribute
            return;
        }

        putAttribute(nestedTag.getName(), nestedTag.getList());
    }

    /**
     * Get current component context.
     */
    protected BasicComponentContext getCurrentContext() {
        if (cachedCurrentContext == null) {
            cachedCurrentContext = (BasicComponentContext) pageContext.getAttribute(
                ComponentConstants.COMPONENT_CONTEXT,
                PageContext.REQUEST_SCOPE);
        }

        return cachedCurrentContext;
    }


    /**
     * Process the start tag by checking tag's attributes and creating
     * appropriate handler. Possible handlers :
     * <ul>
     * <li> URL
     * <li> definition
     * <li> direct String
     * </ul>
     * Handlers also contain sub-component context.
     */
    public int doStartTag() throws JspException {

        // Additional fix for Bug 20034 (2005-04-28)
        cachedCurrentContext = null;

        // Check role immediatly to avoid useless stuff.
        // In case of insertion of a "definition", definition's role still
        // checked later.
        // This lead to a double check of "role" ;-(
        HttpServletRequest request = (HttpServletRequest) pageContext
            .getRequest();
        if (role != null && !request.isUserInRole(role)) {
            processEndTag = false;
            return SKIP_BODY;
        }

        container = TilesAccess.getContainer(pageContext.getServletContext());

        try {
            tagHandler = createTagHandler();

        } catch (JspException e) {
            if (isErrorIgnored) {
                processEndTag = false;
                return SKIP_BODY;
            } else {
                throw e;
            }
        }

        return tagHandler.doStartTag();
    }

    /**
     * Process the end tag by including the template. Simply call the handler
     * doEndTag
     */
    public int doEndTag() throws JspException {
        if (!processEndTag) {
            releaseInternal();
            return EVAL_PAGE;
        }

        int res = tagHandler.doEndTag();
        // Reset properties used by object, in order to be able to reuse object.
        releaseInternal();
        return res;
    }

    /**
     * Processes tag attributes and create corresponding tag handler.<br>
     * Each implementation will create the correct instance of
     * {@link TagHandler} depending on their needs.
     */
    public abstract TagHandler createTagHandler() throws JspException;

    /**
     * End of Process tag attribute "definition". Overload definition with tag
     * attributes "template" and "role". Then, create appropriate tag handler.
     *
     * @param definition Definition to process.
     * @return Appropriate TagHandler.
     * @throws JspException InstantiationException Can't create requested
     *                      preparerInstance
     */
    protected TagHandler processDefinition(String definition, Map<String, Object> attributes)
        throws JspException {
        // Declare local variable in order to not change Tag attribute values.
        String role = this.role;
        String preparer = this.preparer;

        // Can check if page is set
        return new DefinitionHandler(attributes, definition, role, preparer);
    }

    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Inner Interface. Sub handler for tag.
     */
    protected interface TagHandler {
        /**
         * Create BasicComponentContext for type depicted by implementation class.
         */
        public int doStartTag() throws JspException;

        /**
         * Do include for type depicted by implementation class.
         */
        public int doEndTag() throws JspException;

        /**
         * Add a component parameter (attribute) to subContext.
         */
        public void putAttribute(String name, Object value);
    } // end inner interface

    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Real handler, after attribute resolution. Handle include sub-component.
     */
    protected class InsertHandler implements TagHandler {
        protected String page;

        protected String role;

        protected String preparer;

        protected Map<String, ComponentAttribute> attributes;

        /**
         * Constructor. Create insert handler using Component definition.
         *
         * @param attributes custom attributes
         * @param page       resulting page
         * @param role       required role
         * @param preparer   custom preparer
         */
        public InsertHandler(Map<String, Object> attributes, String page, String role,
                             String preparer) {

            this.page = page;
            this.role = role;
            this.preparer = preparer;
            this.attributes = new HashMap<String, ComponentAttribute>();
            if (attributes != null) {
                for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                    ComponentAttribute attr = null;
                    if (entry.getValue() instanceof ComponentAttribute) {
                        attr = (ComponentAttribute) entry.getValue();
                    } else {
                        attr = new ComponentAttribute(entry.getValue());
                    }
                    this.attributes.put(entry.getKey(), attr);
                }
            }
        }

        /**
         * Constructor. Create insert handler to insert page at specified
         * location.
         *
         * @param page     resulting page
         * @param role     required role
         * @param preparer custom preparer
         */
        public InsertHandler(String page, String role, String preparer) {
            this.page = page;
            this.role = role;
            this.preparer = preparer;
            this.attributes = new HashMap<String, ComponentAttribute>();
        }


        public void putAttribute(String name, Object value) {
            if (value instanceof ComponentAttribute) {
                attributes.put(name, (ComponentAttribute) value);
            } else {
                attributes.put(name, new ComponentAttribute(value));
            }
        }

        /**
         * Create a new empty context.
         */
        public int doStartTag() throws JspException {
            // Check role
            HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();

            if (role != null && !request.isUserInRole(role)) {
                return SKIP_BODY;
            }

            // save current context
            return EVAL_BODY_INCLUDE;
        }


        /**
         * Include requested page.
         */
        public int doEndTag() throws JspException {
            // Check role
            HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();

            if (role != null && !request.isUserInRole(role)) {
                return EVAL_PAGE;
            }

            try {
                ComponentContext context = container.getComponentContext(pageContext);
                for (Map.Entry<String, ComponentAttribute> entry : attributes.entrySet()) {
                    context.putAttribute(entry.getKey(), entry.getValue());
                }

                if (log.isDebugEnabled()) {
                    log.debug("insert page='" + page + "'.");
                }

                // Call preparerInstance if any
                if (preparer != null) {
                    container.prepare(request, pageContext.getResponse(),
                        preparer);
                }

                // include requested component.
                if (flush) {
                    pageContext.getOut().flush();
                }

                doInclude(page, flush);

            } catch (IOException e) {
                String msg = "Can't insert page '" + page + "' : "
                    + e.getMessage();
                log.error(msg, e);
                throw new JspException(msg);

            } catch (IllegalArgumentException e) {
                // Can't resolve page uri, should we ignore it?
                if (!(page == null && isErrorIgnored)) {
                    String msg = "Can't insert page '" + page
                        + "'. Check if it exists.\n" + e.getMessage();

                    log.error(msg, e);
                    throw new JspException(msg, e);
                }

            } catch (Exception e) {
                Throwable cause = e;
                if (e.getCause() != null) {
                    cause = e.getCause();
                }

                String msg = "Exception in '" + page + "': "
                    + cause.getMessage();

                log.error(msg, e);
                throw new JspException(msg, e);
            }

            return EVAL_PAGE;
        }

        /**
         * Do an include of specified page. This method is used internally to do all
         * includes from this class. It delegates the include call to the
         * JspUtil.doInclude().
         *
         * @param page  The page that will be included
         * @param flush If the writer should be flushed before the include
         * @throws javax.servlet.jsp.JspException
         */
        protected void doInclude(String page, boolean flush) throws JspException {
            JspUtil.doInclude(pageContext, page, flush);
        }
    }

    protected class DefinitionHandler extends InsertHandler {

        public DefinitionHandler(Map<String, Object> attributes,
                                 String page,
                                 String role,
                                 String preparer) {
            super(attributes, page, role, preparer);
        }

        public DefinitionHandler(String page,
                                 String role,
                                 String preparer) {
            super(page, role, preparer);
        }

        public void doInclude(String page, boolean flush) throws JspException {
            try {

                container.render(pageContext.getRequest(),
                    pageContext.getResponse(), page);
            } catch (TilesException e) {
                throw new JspException(e);
            }

        }

    }

    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Handle insert direct string.
     */
    protected class DirectStringHandler implements TagHandler {
        /**
         * Object to print as a direct string
         */
        private Object value;

        /**
         * Constructor.
         */
        public DirectStringHandler(Object value) {
            this.value = value;
        }

        /**
         * Do nothing, there is no context for a direct string.
         */
        public int doStartTag() throws JspException {
            return SKIP_BODY;
        }

        /**
         * Add attribute to sub context. Do nothing.
         */
        public void putAttribute(String name, Object value) {
        }

        /**
         * Print String in page output stream.
         */
        public int doEndTag() throws JspException {
            try {
                if (flush) {
                    pageContext.getOut().flush();
                }

                pageContext.getOut().print(value);

            } catch (IOException ex) {
                if (log.isDebugEnabled()) {
                    log.debug("Can't write string '" + value + "' : ", ex);
                }

                pageContext.setAttribute(ComponentConstants.EXCEPTION_KEY, ex,
                    PageContext.REQUEST_SCOPE);

                throw new JspException("Can't write string '" + value + "' : "
                    + ex.getMessage(), ex);
            }

            return EVAL_PAGE;
        }
    }
}
