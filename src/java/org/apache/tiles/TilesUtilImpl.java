/*
 * $Id$ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
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

package org.apache.tiles;

import java.io.IOException;
import java.io.Serializable;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.util.RequestUtils;

/**
 * Default implementation of TilesUtil.
 * This class contains default implementation of utilities. This implementation
 * is intended to be used without Struts.
 */
public class TilesUtilImpl implements Serializable {
    
    /** Commons Logging instance.*/
    protected static final Log log = LogFactory.getLog(TilesUtil.class);

    /** Constant name used to store factory in servlet context */
    public static final String DEFINITIONS_FACTORY =
        "org.apache.tiles.DEFINITIONS_FACTORY";
    
    /** Constant used to store ComponentDefinitions graph. */
    public static final String DEFINITIONS_OBJECT = 
            "org.apache.tiles.ComponentDefinitions";
        
    /**
     * JSP 2.0 include method to use which supports configurable flushing.
     */
    private static Method include = null;    
    
    /**
     * Initialize the include variable with the
     * JSP 2.0 method if available.
     */
    static {

        try {
            // get version of include method with flush argument
            Class[] args = new Class[]{String.class, boolean.class};
            include = PageContext.class.getMethod("include", args);
        } catch (NoSuchMethodException e) {
            log.debug("Could not find JSP 2.0 include method.  Using old one that doesn't support " +
                      "configurable flushing.", e);
        }
    }

    /**
     * Do a forward using request dispatcher.
     *
     * This method is used by the Tiles package anytime a forward is required.
     * @param uri Uri or Definition name to forward.
     * @param request Current page request.
     * @param servletContext Current servlet context.
     */
    public void doForward(
        String uri,
        HttpServletRequest request,
        HttpServletResponse response,
        ServletContext servletContext)
        throws IOException, ServletException {
            
        request.getRequestDispatcher(uri).forward(request, response);
    }

    /**
     * Do an include using request dispatcher.
     *
     * This method is used by the Tiles package when an include is required.
     * The Tiles package can use indifferently any form of this method.
     * @param uri Uri or Definition name to forward.
     * @param request Current page request.
     * @param response Current page response.
     * @param servletContext Current servlet context.
     */
    public void doInclude(
        String uri,
        HttpServletRequest request,
        HttpServletResponse response,
        ServletContext servletContext)
        throws IOException, ServletException {
            
        request.getRequestDispatcher(uri).include(request, response);
    }

    /**
     * Do an include using PageContext.include().
     *
     * This method is used by the Tiles package when an include is required.
     * The Tiles package can use indifferently any form of this method.
     * @param uri Uri or Definition name to forward.
     * @param pageContext Current page context.
     * @param flush If the writer should be flushed before the include
     */
    public void doInclude(String uri, PageContext pageContext, boolean flush)
        throws IOException, ServletException {
        try {
            // perform include with new JSP 2.0 method that supports flushing
            if (include != null) {
                include.invoke(pageContext, new Object[]{uri, Boolean.valueOf(flush)});
                return;
            } 
        } catch (IllegalAccessException e) {
            log.debug("Could not find JSP 2.0 include method.  Using old one.", e);
        } catch (InvocationTargetException e) {
            log.debug("Unable to execute JSP 2.0 include method.  Trying old one.", e);
        }
            
        pageContext.include(uri);
    }

    /**
     * Get definition factory from appropriate servlet context.
     * @return Definitions factory or <code>null</code> if not found.
     */
    public DefinitionsFactory getDefinitionsFactory(
        ServletRequest request,
        ServletContext servletContext) {
            
        return (DefinitionsFactory) servletContext.getAttribute(DEFINITIONS_FACTORY);
    }

    /**
     * Create Definition factory from specified configuration object.
     * Create an instance of the factory with the class specified in the config
     * object. Then, initialize this factory and finally store the factory in
     * appropriate context by calling
     * {@link #makeDefinitionsFactoryAccessible(DefinitionsFactory, ServletContext)}.
     * Factory creation is done by {@link #createDefinitionFactoryInstance(String)}.
     * <p>
     *
     * @param servletContext Servlet Context passed to newly created factory.
     * @param factoryConfig Configuration object passed to factory.
     * @return newly created factory of type specified in the config object.
     * @throws DefinitionsFactoryException If an error occur while initializing factory
     */
    public DefinitionsFactory createDefinitionsFactory(
        ServletContext servletContext,
        DefinitionsFactoryConfig factoryConfig)
        throws DefinitionsFactoryException {
            
        // FIXME:  Pull from servlet context.
        String factoryClassName = "org.apache.tiles.definition.UrlDefinitionsFactory";
        
        // Create configurable factory
        DefinitionsFactory factory =
            createDefinitionFactoryInstance(factoryClassName);

        Map params = factoryConfig.getAttributes();
        
        factory.init(params);

        String configFiles = factoryConfig.getDefinitionConfigFiles();
        List filenames = getFilenames(configFiles);
        
        try {
            for (int i = 0; i < filenames.size(); i++) {
                String filename = (String) filenames.get(i);
                factory.addSource(servletContext.getResource(filename));
            }
        } catch (MalformedURLException e) {
            throw new DefinitionsFactoryException("Problem with filename URL: ", e);
        }
        
        ComponentDefinitions definitions = factory.readDefinitions();
        
        // Make factory accessible from jsp tags (push it in appropriate context)
        makeDefinitionsFactoryAccessible(factory, servletContext);
        makeDefinitionsAccessible(definitions, servletContext);
        
        return factory;
    }

    public ComponentDefinition getDefinition(String definitionName,
            ServletRequest request,
            ServletContext servletContext) 
            throws FactoryNotFoundException, DefinitionsFactoryException {
        
        try {
            DefinitionsFactory factory = getDefinitionsFactory(request, servletContext);
            ComponentDefinitions definitions = (ComponentDefinitions) 
                servletContext.getAttribute(TilesUtilImpl.DEFINITIONS_OBJECT);
            ComponentDefinition definition = definitions.getDefinition(
                    definitionName, request.getLocale());
            
            if (definition == null) {
                if (!factory.isLocaleProcessed(request.getLocale())) {
                    // FIXME This will modify the factory as well as the definitions
                    // but we are only locking the definitions.
                    // 
                    // We'll have to refactor again to remove this issue.
                    synchronized (definitions) {
                        factory.addDefinitions(definitions, request.getLocale());
                    }
                }
                
                definition = definitions.getDefinition(
                    definitionName, request.getLocale());
            }
            
            return definition;
        } catch (NullPointerException ex) { // Factory not found in context
            throw new FactoryNotFoundException("Can't get definitions factory from context.");
        }
    }
    
    /**
     * Create Definition factory of specified classname.
     * Factory class must extend the {@link DefinitionsFactory} class.
     * @param classname Class name of the factory to create.
     * @return newly created factory.
     * @throws DefinitionsFactoryException If an error occur while initializing factory
     */
    protected DefinitionsFactory createDefinitionFactoryInstance(String classname)
        throws DefinitionsFactoryException {
            
        try {
            Class factoryClass = RequestUtils.applicationClass(classname);
            Object factory = factoryClass.newInstance();

            return (DefinitionsFactory) factory;
            
        } catch (ClassCastException ex) { // Bad classname
            throw new DefinitionsFactoryException(
                "Error - createDefinitionsFactory : Factory class '"
                    + classname
                    + " must implement 'DefinitionsFactory'.",
                ex);
                
        } catch (ClassNotFoundException ex) { // Bad classname
            throw new DefinitionsFactoryException(
                "Error - createDefinitionsFactory : Bad class name '"
                    + classname
                    + "'.",
                ex);
                
        } catch (InstantiationException ex) { // Bad constructor or error
            throw new DefinitionsFactoryException(ex);
            
        } catch (IllegalAccessException ex) {
            throw new DefinitionsFactoryException(ex);
        }
    }
    
    /**
     * Make definition factory accessible to Tags.
     * Factory is stored in servlet context.
     * @param factory Factory to be made accessible.
     * @param servletContext Current servlet context.
     */
    protected void makeDefinitionsFactoryAccessible(
        DefinitionsFactory factory,
        ServletContext servletContext) {
            
        servletContext.setAttribute(DEFINITIONS_FACTORY, factory);
    }

    /**
     * Make definition factory accessible to Tags.
     * Factory is stored in servlet context.
     * @param definitions Definition factory to be made accessible
     * @param servletContext Current servlet context.
     */
    protected void makeDefinitionsAccessible(
        ComponentDefinitions definitions,
        ServletContext servletContext) {
            
        servletContext.setAttribute(DEFINITIONS_OBJECT, definitions);
    }

    /**
     * Parses a comma-delimited string for a list of config filenames.
     */
    protected List getFilenames(String filenameString) {
        // Init list of filenames
        StringTokenizer tokenizer = new StringTokenizer(filenameString, ",");
        List filenames = new ArrayList(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            filenames.add(tokenizer.nextToken().trim());
        }
        
        return filenames;
    }
}
