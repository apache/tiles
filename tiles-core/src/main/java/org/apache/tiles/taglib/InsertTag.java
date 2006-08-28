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
import java.io.PrintWriter;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.TilesContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.taglib.util.TagUtils;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.ComponentDefinition;
import org.apache.tiles.Controller;
import org.apache.tiles.DefinitionsFactoryException;
import org.apache.tiles.FactoryNotFoundException;
import org.apache.tiles.NoSuchDefinitionException;
import org.apache.tiles.TilesUtil;

/**
 * This is the tag handler for &lt;tiles:insert&gt;, which includes
 * a template. The tag's body content consists of &lt;tiles:put&gt;
 * tags, which are accessed by &lt;tiles:get&gt; in the template.
 *
 * @version $Rev$ $Date$
 */
public class InsertTag
	extends DefinitionTagSupport
	implements PutTagParent, ComponentConstants, PutListTagParent {

	/** 
	 * The role delimiter. 
	 * @deprecated This will be removed in a release after Struts 1.2.
	 */
	public static final String ROLE_DELIMITER = ",";

	/** 
	 * Commons Logging instance. 
	 */
	protected static Log log = LogFactory.getLog(InsertTag.class);

	/* JSP Tag attributes */

	/** 
	 * Flush attribute value. 
	 */
	protected boolean flush = true;

	/** 
	 * Name to insert. 
	 */
	protected String name = null;

	/**
	 * Are errors ignored. This is the property for attribute 'ignore'.
	 * Default value is false, which throw an exception.
	 * Only 'attribute not found' errors are ignored.
	 */
	protected boolean isErrorIgnored = false;

	/**
	 * Name of component instance to include.
	 */
	protected String definitionName = null;

        /**
         * Optional attribute to explicitly specify whether the thing being 
         * inserted is a(n) definition, attribute, or template.
         */
        private String type = null;
        
	/* Internal properties */
	/**
	 * Does the end tag need to be processed.
	 * Default value is true. Boolean set in case of ignored errors.
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
	 * Reset member values for reuse. This method calls super.release(),
	 * which invokes TagSupport.release(), which typically does nothing.
	 */
	public void release() {

		super.release();

		definitionName = null;
		flush = true;
		name = null;
		template = null;
		role = null;
		isErrorIgnored = false;
                type = null;

		releaseInternal();
	}

	/**
	 * Reset internal member values for reuse.
	 */
	protected void releaseInternal() {
		cachedCurrentContext = null;
		processEndTag = true;
		// pageContext = null;  // orion doesn't set it between two tags
		tagHandler = null;
	}

	/**
	 * Set the current page context.
	 * Called by the page implementation prior to doStartTag().
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
	 * Set name.
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Get name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get definition name.
	 */
	public String getDefinitionName() {
		return definitionName;
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
	 * Set flush.
	 * Method added for compatibility with JSP1.1
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

	/////////////////////////////////////////////////////////////////////////

	/**
	 * Add a body attribute.
	 * Erase any attribute with same name previously set.
	 */
	public void putAttribute(String name, Object value) {
		tagHandler.putAttribute(name, value);
	}

	/**
	 * Process nested &lg;put&gt; tag.
	 * Method calls by nested &lg;put&gt; tags.
	 * Nested list is added to current list.
	 * If role is defined, it is checked immediately.
	 */
	public void processNestedTag(PutTag nestedTag) throws JspException {
		// Check role
		HttpServletRequest request =
			(HttpServletRequest) pageContext.getRequest();
		String role = nestedTag.getRole();
		if (role != null && !request.isUserInRole(role)) {
			// not allowed : skip attribute
			return;
		}

		putAttribute(nestedTag.getName(), nestedTag.getRealValue());
	}

	/**
	 * Process nested &lg;putList&gt; tag.
	 * Method calls by nested &lg;putList&gt; tags.
	 * Nested list is added to sub-component attributes
	 * If role is defined, it is checked immediately.
	 */
	public void processNestedTag(PutListTag nestedTag) throws JspException {
		// Check role
		HttpServletRequest request =
			(HttpServletRequest) pageContext.getRequest();
		String role = nestedTag.getRole();
		if (role != null && !request.isUserInRole(role)) {
			// not allowed : skip attribute
			return;
		}

		// Check if a name is defined
		if (nestedTag.getName() == null) {
			throw new JspException("Error - PutList : attribute name is not defined. It is mandatory as the list is added as attribute of 'insert'.");
		}

		// now add attribute to enclosing parent (i.e. : this object).
		putAttribute(nestedTag.getName(), nestedTag.getList());
	}

	/**
	 * Method calls by nested &lg;putList&gt; tags.
	 * A new list is added to current insert object.
	 */
	public void putAttribute(PutListTag nestedTag) throws JspException {
		// Check role
		HttpServletRequest request =
			(HttpServletRequest) pageContext.getRequest();
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
	private ComponentContext getCurrentContext() {
		if (cachedCurrentContext == null) {
			cachedCurrentContext =
				(ComponentContext) pageContext.getAttribute(
					ComponentConstants.COMPONENT_CONTEXT,
					PageContext.REQUEST_SCOPE);
		}

		return cachedCurrentContext;
	}

	/**
	 * Get instantiated Controller.
	 * Return controller denoted by controllerType, or <code>null</code> if controllerType
	 * is null.
	 * @throws JspException If controller can't be created.
	 */
	private Controller getController() throws JspException {
		if (controllerType == null) {
			return null;
		}

		try {
			return ComponentDefinition.createController(
				controllerName,
				controllerType);

		} catch (InstantiationException ex) {
			throw new JspException(ex);
		}
	}

	/**
	 * Process the start tag by checking tag's attributes and creating appropriate handler.
	 * Possible handlers :
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
		// In case of insertion of a "definition", definition's role still checked later.
		// This lead to a double check of "role" ;-(
		HttpServletRequest request =
			(HttpServletRequest) pageContext.getRequest();
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
	 * Process the end tag by including the template.
	 * Simply call the handler doEndTag
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
	 * Process tag attribute and create corresponding tag handler.
         *
         * Supported types include <code>string</code>, <code>definition</code>,
         * <code>atribute</code>, and <code>template</code>  If type is string, 
         * the attribute value passed in will be printed directly to the PrintWriter.  If 
         * type is definition the value will be processed as a Tiles definition.
         * If type is attribute, the value will be processed as an attribute of 
         * the current definition.  If type is template, the value will be a 
         * JSP include.
         *
	 */
	public TagHandler createTagHandler() throws JspException {
            
            if (type != null && type.length() > 0) {
                // Type is specified.  Process accordingly.
                if (type.equalsIgnoreCase("string")) {
                    return new DirectStringHandler(name);
                } else if (type.equalsIgnoreCase("definition")) {
                    return processDefinitionName(name);
                } else if (type.equalsIgnoreCase("attribute")) {
                    return processAttribute(name);
                } else if (type.equalsIgnoreCase("template")) {
                    return processUrl(name);
                } else {
                    throw new JspException("Error - Incorrect type value \"" + type + "\"passed in.");
                }
            } else {
                // Type is not specified.  Try to determine.
		if (name != null) {
			return processName(name);
		} else if (template != null) {
			return processUrl(template);
		} else {
			throw new JspException("Error - Tag Insert : At least one of the following attribute must be defined : template|name. Check tag syntax");
		}
            }
	}

	/**
	 * Process name.
	 * Search in following order :
	 * <ul>
	 * <li>Component context -  if found, process it as value.</li>
	 * <li>definitions factory</li>
	 * <li>URL</li>
	 * <li></li>
	 * </ul>
	 *
	 * @return appropriate tag handler.
	 * @throws JspException - Throws by underlying nested call to 
	 * processDefinitionName()
	 */
	public TagHandler processName(String name) throws JspException {
        Object attrValue = null;
        ComponentContext context = getCurrentContext();
        
        if (context != null) {
            attrValue = context.getAttribute(name);
        }

		if (attrValue != null && attrValue instanceof ComponentAttribute) {
                    return processTypedAttribute((ComponentAttribute) attrValue);
		} else {
                    return processDefinitionName(name);
                }
	}

	/**
	 * Process the url.
	 * @throws JspException If failed to create controller
	 */
	public TagHandler processUrl(String url) throws JspException {
		return new InsertHandler(url, role, getController());
	}

	/**
	 * Process tag attribute "definition".
	 * First, search definition in the factory, then create handler from this definition.
	 * @param name Name of the definition.
	 * @return Appropriate TagHandler.
	 * @throws JspException- NoSuchDefinitionException No Definition  found for name.
	 * @throws JspException- FactoryNotFoundException Can't find Definitions factory.
	 * @throws JspException- DefinedComponentFactoryException General error in factory.
	 * @throws JspException InstantiationException Can't create requested controller
	 */
	protected TagHandler processDefinitionName(String name)
		throws JspException {

            try {
              TilesContext tilesContext = TilesContextFactory.getInstance(
                      pageContext.getServletContext(),
                      pageContext.getRequest(), pageContext.getResponse());
                ComponentDefinition definition = TilesUtil.getDefinition(
                        name, tilesContext);

                if (definition == null) { // is it possible ?
                    throw new NoSuchDefinitionException();
                }

                return processDefinition(definition);

            } catch (NoSuchDefinitionException ex) {
                throw new JspException(
                        "Error -  Tag Insert : Can't get definition '"
                        + definitionName
                        + "'. Check if this name exists in definitions factory.", ex);

            } catch (FactoryNotFoundException ex) {
                throw new JspException(ex.getMessage());

            } catch (DefinitionsFactoryException ex) {
                if (log.isDebugEnabled()) {
                    ex.printStackTrace();
                }

                // Save exception to be able to show it later
                pageContext.setAttribute(
                    ComponentConstants.EXCEPTION_KEY,
                    ex,
                    PageContext.REQUEST_SCOPE);
                throw new JspException(ex);
            }
	}

	/**
	 * End of Process tag attribute "definition".
	 * Overload definition with tag attributes "template" and "role".
	 * Then, create appropriate tag handler.
	 * @param definition Definition to process.
	 * @return Appropriate TagHandler.
	 * @throws JspException InstantiationException Can't create requested controller
	 */
	protected TagHandler processDefinition(ComponentDefinition definition)
		throws JspException {
		// Declare local variable in order to not change Tag attribute values.
		String role = this.role;
		String page = this.template;
		Controller controller = null;

		try {
			controller = definition.getOrCreateController();

			// Overload definition with tag's template and role.
			if (role == null) {
				role = definition.getRole();
			}

			if (page == null) {
				page = definition.getTemplate();
			}

			if (controllerName != null) {
				controller =
					ComponentDefinition.createController(
						controllerName,
						controllerType);
			}

			// Can check if page is set
			return new InsertHandler(
				definition.getAttributes(),
				page,
				role,
				controller);

		} catch (InstantiationException ex) {
			throw new JspException(ex);
		}
	}

	/**
	 * Process tag attribute "attribute".
	 * Get value from component attribute.
	 * Found value is process by processObjectValue().
	 * @param name Name of the attribute.
	 * @return Appropriate TagHandler.
	 * @throws JspException - NoSuchDefinitionException No Definition  found for name.
	 * @throws JspException - Throws by underlying nested call to processDefinitionName()
	 */
	public TagHandler processAttribute(String name) throws JspException {
            Object attrValue = null;
            ComponentContext context = getCurrentContext();
            
            if (context != null) {
                attrValue = context.getAttribute(name);
            }

            if (attrValue == null) {
                    throw new JspException(
                            "Error - Tag Insert : No value found for attribute '"
                                    + name
                                    + "'.");
            } else if (attrValue instanceof ComponentAttribute) {
                return processTypedAttribute((ComponentAttribute) attrValue);
            } else {
                throw new JspException("Invalid attribute type: " + attrValue.getClass().getName());
            }
	}

	/**
	 * Process typed attribute explicitly according to its type.
         *
	 * @param value Typed attribute to process.
	 * @return appropriate TagHandler.
	 * @throws JspException - Throws by underlying nested call to processDefinitionName()
	 */
	public TagHandler processTypedAttribute(ComponentAttribute value)
		throws JspException {
            
            if (value == null) {
                // FIXME.
                return null;
            } 
            
            String type = value.getType();
            if (type == null) {
                Object valueContent = value.getValue();
                if (valueContent instanceof String) {
                    String valueString = (String) valueContent;
                    if (valueString.startsWith("/")) {
                        type = "template";
                    } else {
                        type = "string";
                    }
                } else if (valueContent instanceof ComponentDefinition) {
                    type = "definition";
                }
            }
            
            if (type == null) {
                throw new JspException("Unrecognized type for attribute value "
                + value.getValue());
            }
            
            if (type.equalsIgnoreCase("string")) {
		return new DirectStringHandler((String) value.getValue());
            } else if (type.equalsIgnoreCase("definition")) {
		return processDefinition((ComponentDefinition) value.getValue());
            } else {
                return new InsertHandler((String) value.getValue(),
			role, getController());
            }
	}

	/**
	 * Do an include of specified page.
	 * This method is used internally to do all includes from this class. It delegates
	 * the include call to the TilesUtil.doInclude().
	 * @param page The page that will be included
     * @param flush If the writer should be flushed before the include
	 * @throws ServletException - Thrown by call to pageContext.include()
	 * @throws IOException - Thrown by call to pageContext.include()
	 */
	protected void doInclude(String page, boolean flush)
		throws Exception, IOException {
		TilesUtil.doInclude(page, pageContext, flush);
	}

	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Inner Interface.
	 * Sub handler for tag.
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

	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Real handler, after attribute resolution.
	 * Handle include sub-component.
	 */
	protected class InsertHandler implements TagHandler {
		protected String page;
		protected ComponentContext currentContext;
		protected ComponentContext subCompContext;
		protected String role;
		protected Controller controller;

		/**
		 * Constructor.
		 * Create insert handler using Component definition.
		 */
		public InsertHandler(
			Map attributes,
			String page,
			String role,
			Controller controller) {

			this.page = page;
			this.role = role;
			this.controller = controller;
			subCompContext = new ComponentContext(attributes);
		}

		/**
		 * Constructor.
		 * Create insert handler to insert page at specified location.
		 */
		public InsertHandler(String page, String role, Controller controller) {
			this.page = page;
			this.role = role;
			this.controller = controller;
			subCompContext = new ComponentContext();
		}

		/**
		 * Create a new empty context.
		 */
		public int doStartTag() throws JspException {
			// Check role
			HttpServletRequest request =
				(HttpServletRequest) pageContext.getRequest();

			if (role != null && !request.isUserInRole(role)) {
				return SKIP_BODY;
			}

			// save current context
			this.currentContext = getCurrentContext();
			return EVAL_BODY_INCLUDE;
		}

		/**
		 * Add attribute to sub context.
		 * Do nothing.
		 */
		public void putAttribute(String name, Object value) {
			subCompContext.putAttribute(name, value);
		}

		/**
		 * Include requested page.
		 */
		public int doEndTag() throws JspException {
                    // Check role
                    HttpServletRequest request =
                            (HttpServletRequest) pageContext.getRequest();

                    if (role != null && !request.isUserInRole(role)) {
                        return EVAL_PAGE;
                    }

                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("insert page='" + page + "'.");
                        }

                        // set new context for included component.
                        pageContext.setAttribute(
                                ComponentConstants.COMPONENT_CONTEXT,
                                subCompContext,
                                PageContext.REQUEST_SCOPE);

                        // Call controller if any
                        if (controller != null) {
                            try {
                              TilesContext tilesContext = TilesContextFactory.getInstance(
                                      pageContext.getServletContext(),
                                      pageContext.getRequest(), pageContext.getResponse());
                                controller.execute(tilesContext, subCompContext);
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
                        String msg =
                                "Can't insert page '" + page + "' : " + e.getMessage();
                        log.error(msg, e);
                        throw new JspException(msg);

                    } catch (IllegalArgumentException e) {
                        // Can't resolve page uri, should we ignore it?
                        if (!(page == null && isErrorIgnored)) {
                            String msg =
                                    "Can't insert page '" + page
                                    + "'. Check if it exists.\n"
                                    + e.getMessage();

                            log.error(msg, e);
                            throw new JspException(msg,e);
                        }

                    } catch (Exception e) {
                        Throwable cause = e;
                        if (e.getCause() != null) {
                            cause = e.getCause();
                        }

                        String msg =
                                "Exception in '" + page + "': " + cause.getMessage();

                        log.error(msg, e);
                        throw new JspException(msg,e);

                    } finally {
                        // restore old context only if currentContext not null 
                        // (bug with Silverstream ?; related by Arvindra Sehmi 20010712)
                        if (currentContext != null) {
                            pageContext.setAttribute(
                                        ComponentConstants.COMPONENT_CONTEXT,
                                        currentContext,
                                        PageContext.REQUEST_SCOPE);
                        }
                    }

                    return EVAL_PAGE;
		}

		/**
		 * Process an exception.
		 * Depending of debug attribute, print full exception trace or only
		 * its message in output page.
		 * @param ex Exception
		 * @param msg An additional message to show in console and to propagate if we can't output exception.
		 * @deprecated This method will be removed in a release after Struts 1.2.
		 */
		protected void processException(Throwable ex, String msg)
			throws JspException {

			try {
				if (msg == null) {
					msg = ex.getMessage();
				}

				if (log.isDebugEnabled()) { // show full trace
					log.debug(msg, ex);
					pageContext.getOut().println(msg);
					ex.printStackTrace(
						new PrintWriter(pageContext.getOut(), true));
				} else { // show only message
					pageContext.getOut().println(msg);
				}

			} catch (IOException ioex) { // problems. Propagate original exception
				pageContext.setAttribute(
					ComponentConstants.EXCEPTION_KEY,
					ex,
					PageContext.REQUEST_SCOPE);
				throw new JspException(msg,ioex);
			}
		}
	}

	/**
	 * Parse the list of roles and return <code>true</code> or <code>false</code> based on whether
	 * the user has that role or not.
	 * @param role Comma-delimited list of roles.
	 * @param request The request.
	 */
	static public boolean userHasRole(
		HttpServletRequest request,
		String role) {
		StringTokenizer st = new StringTokenizer(role, ",");
		while (st.hasMoreTokens()) {
			if (request.isUserInRole(st.nextToken())) {
				return true;
			}
		}

		return false;
	}

	/////////////////////////////////////////////////////////////////////////////

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
		 * Add attribute to sub context.
		 * Do nothing.
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

				pageContext.setAttribute(
					ComponentConstants.EXCEPTION_KEY,
					ex,
					PageContext.REQUEST_SCOPE);

				throw new JspException(
					"Can't write string '" + value + "' : " + ex.getMessage(), ex);
			}

			return EVAL_PAGE;
		}
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
