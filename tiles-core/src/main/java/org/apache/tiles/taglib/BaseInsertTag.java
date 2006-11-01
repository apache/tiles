/*
 * $Id$ 
 *
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.taglib;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.taglib.util.TagUtils;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.ComponentDefinition;
import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.ViewPreparer;
import org.apache.tiles.TilesUtil;

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
    protected ComponentContext cachedCurrentContext;
    
    /**
     * Final handler of tag methods.
     */
    protected TagHandler tagHandler = null;
    
    /**
     * Trick to allows inner classes to access pageContext.
     */
    protected PageContext pageContext = null;
    
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
     * <p>
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
    protected ComponentContext getCurrentContext() {
        if (cachedCurrentContext == null) {
            cachedCurrentContext = (ComponentContext) pageContext.getAttribute(
                    ComponentConstants.COMPONENT_CONTEXT,
                    PageContext.REQUEST_SCOPE);
        }
        
        return cachedCurrentContext;
    }
    
    /**
     * Get instantiated ViewPreparer. Return preparer denoted by preparerType,
     * or <code>null</code> if preparerType is null.
     * 
     * @throws JspException If preparer can't be created.
     */
    protected ViewPreparer getPreparer() throws JspException {
        if (preparerType == null) {
            return null;
        }
        
        try {
            return ComponentDefinition.createPreparer(preparerName,
                    preparerType);
            
        } catch (InstantiationException ex) {
            throw new JspException(ex);
        }
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
     * 
     */
    public abstract TagHandler createTagHandler() throws JspException;
    
    /**
     * End of Process tag attribute "definition". Overload definition with tag
     * attributes "template" and "role". Then, create appropriate tag handler.
     * 
     * @param definition Definition to process.
     * @return Appropriate TagHandler.
     * @throws JspException InstantiationException Can't create requested
     * preparer
     */
    protected TagHandler processDefinition(ComponentDefinition definition)
            throws JspException {
        // Declare local variable in order to not change Tag attribute values.
        String role = this.role;
        String page = this.template;
        ViewPreparer preparer = null;
        
        try {
            preparer = definition.getOrCreatePreparer();
            
            // Overload definition with tag's template and role.
            if (role == null) {
                role = definition.getRole();
            }
            
            if (page == null) {
                page = definition.getTemplate();
            }
            
            if (preparerName != null) {
                preparer = ComponentDefinition.createPreparer(preparerName,
                        preparerType);
            }
            
            // Can check if page is set
            return new InsertHandler(definition.getAttributes(), page, role,
                    preparer);
            
        } catch (InstantiationException ex) {
            throw new JspException(ex);
        }
    }
    
    /**
     * Do an include of specified page. This method is used internally to do all
     * includes from this class. It delegates the include call to the
     * TilesUtil.doInclude().
     * 
     * @param page The page that will be included
     * @param flush If the writer should be flushed before the include
     * @throws ServletException - Thrown by call to pageContext.include()
     * @throws IOException - Thrown by call to pageContext.include()
     */
    protected void doInclude(String page, boolean flush) throws Exception,
            IOException {
        TilesUtil.doInclude(page, pageContext, flush);
    }
    
    // ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Parse the list of roles and return <code>true</code> or
     * <code>false</code> based on whether the user has that role or not.
     * 
     * @param role Comma-delimited list of roles.
     * @param request The request.
     */
    static public boolean userHasRole(HttpServletRequest request, String role) {
        StringTokenizer st = new StringTokenizer(role, ",");
        while (st.hasMoreTokens()) {
            if (request.isUserInRole(st.nextToken())) {
                return true;
            }
        }
        
        return false;
    }
    
    // ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Inner Interface. Sub handler for tag.
     */
    protected interface TagHandler {
        /**
         * Create ComponentContext for type depicted by implementation class.
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
        
        protected ComponentContext currentContext;
        
        protected ComponentContext subCompContext;
        
        protected String role;
        
        protected ViewPreparer preparer;
        
        /**
         * Constructor. Create insert handler using Component definition.
         */
        public InsertHandler(Map attributes, String page, String role,
                ViewPreparer preparer) {
            
            this.page = page;
            this.role = role;
            this.preparer = preparer;
            subCompContext = new ComponentContext(attributes);
        }
        
        /**
         * Constructor. Create insert handler to insert page at specified
         * location.
         */
        public InsertHandler(String page, String role, ViewPreparer preparer) {
            this.page = page;
            this.role = role;
            this.preparer = preparer;
            subCompContext = new ComponentContext();
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
            this.currentContext = getCurrentContext();
            return EVAL_BODY_INCLUDE;
        }
        
        /**
         * Add attribute to sub context. Do nothing.
         */
        public void putAttribute(String name, Object value) {
            subCompContext.putAttribute(name, value);
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
                if (log.isDebugEnabled()) {
                    log.debug("insert page='" + page + "'.");
                }
                
                // set new context for included component.
                pageContext.setAttribute(ComponentConstants.COMPONENT_CONTEXT,
                        subCompContext, PageContext.REQUEST_SCOPE);
                
                // Call preparer if any
                if (preparer != null) {
                    try {
                        TilesRequestContext tilesContext = TagUtils
                                .getTilesRequestContext(pageContext);
                        preparer.execute(tilesContext, subCompContext);
                    } catch (Exception e) {
                        throw new ServletException(e);
                    }
                    
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
                
            } finally {
                // restore old context only if currentContext not null
                // (bug with Silverstream ?; related by Arvindra Sehmi 20010712)
                if (currentContext != null) {
                    pageContext.setAttribute(
                            ComponentConstants.COMPONENT_CONTEXT,
                            currentContext, PageContext.REQUEST_SCOPE);
                }
            }
            
            return EVAL_PAGE;
        }
    }
    
    // ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Handle insert direct string.
     */
    protected class DirectStringHandler implements TagHandler {
        /** Object to print as a direct string */
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
