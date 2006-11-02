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

package org.apache.tiles.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesRequestContext;
import org.apache.tiles.context.BasicTilesContextFactory;
import org.apache.tiles.definition.*;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Default implementation of TilesUtil.
 * This class contains default implementation of utilities. This implementation
 * is intended to be used without Struts.
 */
public class TilesUtilImpl implements Serializable {

    /**
     * Commons Logging instance.
     */
    protected static final Log log = LogFactory.getLog(TilesUtil.class);

    /**
     * Constant name used to store impl in servlet context
     */
    public static final String DEFINITIONS_FACTORY =
        "org.apache.tiles.DEFINITIONS_FACTORY";

    /**
     * Constant used to store ComponentDefinitions graph.
     */
    public static final String DEFINITIONS_OBJECT =
        "org.apache.tiles.definition.ComponentDefinitions";

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

    private TilesApplicationContext applicationContext;

    public TilesUtilImpl(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public TilesApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public TilesRequestContext createRequestContext(Object request, Object response) {
        return new BasicTilesContextFactory().createRequestContext(applicationContext, request, response);
    }

    /**
     * Do a forward using request dispatcher.
     * <p/>
     * This method is used by the Tiles package anytime a forward is required.
     *
     * @param uri          Uri or Definition name to forward.
     * @param tilesContext Current Tiles application context.
     */
    public void doForward(
        String uri,
        TilesRequestContext tilesContext)
        throws IOException, Exception {

        tilesContext.dispatch(uri);
    }

    /**
     * Do an include using request dispatcher.
     * <p/>
     * This method is used by the Tiles package when an include is required.
     * The Tiles package can use indifferently any form of this method.
     *
     * @param uri          Uri or Definition name to forward.
     * @param tilesContext Current Tiles application context.
     */
    public void doInclude(
        String uri,
        TilesRequestContext tilesContext)
        throws IOException, Exception {

        tilesContext.include(uri);
    }

    /**
     * Do an include using PageContext.include().
     * <p/>
     * This method is used by the Tiles package when an include is required.
     * The Tiles package can use indifferently any form of this method.
     *
     * @param uri         Uri or Definition name to forward.
     * @param pageContext Current page context.
     * @param flush       If the writer should be flushed before the include
     */
    public void doInclude(String uri, PageContext pageContext, boolean flush)
        throws IOException, Exception {
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
     * Get definition impl from appropriate servlet context.
     *
     * @return Definitions impl or <code>null</code> if not found.
     */
    public DefinitionsFactory getDefinitionsFactory() {

        return (DefinitionsFactory) applicationContext.getApplicationScope().get(DEFINITIONS_FACTORY);
    }

    /**
     * Create Definition impl from specified configuration object.
     * Create an instance of the impl with the class specified in the config
     * object. Then, initialize this impl and finally store the impl in
     * appropriate context by calling
     * {@link #makeDefinitionsFactoryAccessible(org.apache.tiles.definition.DefinitionsFactory)}.
     * Factory creation is done by {@link #createDefinitionFactoryInstance(String)}.
     * <p/>
     *
     * @param factoryConfig Configuration object passed to impl.
     * @return newly created impl of type specified in the config object.
     * @throws DefinitionsFactoryException If an error occur while initializing impl
     */
    public DefinitionsFactory createDefinitionsFactory(
        DefinitionsFactoryConfig factoryConfig)
        throws DefinitionsFactoryException {

        // FIXME:  Do you think it is correct to pull it from ServletConfig?
        String factoryClassName = factoryConfig.getFactoryClassname();

        if (factoryClassName == null) {
            factoryClassName = "org.apache.tiles.definition.UrlDefinitionsFactory";
        }

        // Create configurable impl
        DefinitionsFactory factory =
            createDefinitionFactoryInstance(factoryClassName);

        Map params = factoryConfig.getAttributes();

        factory.init(params);

        String configFiles = factoryConfig.getDefinitionConfigFiles();
        List filenames = getFilenames(configFiles);

        try {
            for (int i = 0; i < filenames.size(); i++) {
                String filename = (String) filenames.get(i);
                factory.addSource(applicationContext.getResource(filename));
            }
        } catch (MalformedURLException e) {
            throw new DefinitionsFactoryException("Problem with filename URL: ", e);
        }

        makeDefinitionsFactoryAccessible(factory);

        return factory;
    }

    public ComponentDefinition getDefinition(String definitionName,
                                             TilesRequestContext tilesContext)
        throws FactoryNotFoundException, DefinitionsFactoryException {

        try {
            DefinitionsFactory factory = getDefinitionsFactory();
            return factory.getDefinition(definitionName, tilesContext);
        } catch (NullPointerException ex) { // Factory not found in context
            throw new FactoryNotFoundException("Can't get definitions impl from context.");
        }
    }

    /**
     * Create Definition impl of specified classname.
     * Factory class must extend the {@link DefinitionsFactory} class.
     *
     * @param classname Class name of the impl to create.
     * @return newly created impl.
     * @throws DefinitionsFactoryException If an error occur while initializing impl
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
     * Make definition impl accessible to Tags.
     * Factory is stored in servlet context.
     *
     * @param factory Factory to be made accessible.
     */
    protected void makeDefinitionsFactoryAccessible(
        DefinitionsFactory factory) {

        applicationContext.getApplicationScope().put(DEFINITIONS_FACTORY, factory);
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
