/*
 * Copyright 2004-2005 The Apache Software Foundation.
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
 *
 * $Id$
 */

package org.apache.tiles.listener;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import org.apache.tiles.DefinitionsFactory;
import org.apache.tiles.DefinitionsFactoryConfig;
import org.apache.tiles.DefinitionsFactoryException;
import org.apache.tiles.TilesContext;
import org.apache.tiles.TilesUtil;
import org.apache.tiles.TilesUtilImpl;
import org.apache.tiles.context.TilesContextFactory;

/**
 * @version $Rev$ $Date$
 */
public class TilesListener implements ServletContextListener {

    /**
     * The logger for this class
    */
   protected static Logger logger = Logger.getLogger(TilesListener.class.
                                                                      getName());


    /**
     * The default name of a context init parameter that specifies the Tiles configuration file
    */
    private static final String DEFAULT_CONFIG_FILE_PARAM = "definitions-config";


    /**
     * The default name of the Tiles configuration file
    */
    private static final String DEFAULT_CONFIG_FILE = "/WEB-INF/tiles.xml";


    /**
     * An error message stating that something went wrong during initialization
    */
    private static final String CANT_POPULATE_FACTORY_ERROR =
         "CAN'T POPULATE TILES DEFINITION FACTORY";


    /**
     * The Tiles definition factory
    */
   protected DefinitionsFactory definitionFactory = null;


    /**
     * A comma-separated list of filenames representing the
     * application's Tiles configuration files.
    */
    private String configFiles = null;

    public void contextInitialized(ServletContextEvent event) {
        logger.info("Initializing TilesListener");
        configFiles = event.getServletContext().getInitParameter(DEFAULT_CONFIG_FILE_PARAM);

        try {
            ServletContext context = event.getServletContext();

            // Create factory config object
            DefinitionsFactoryConfig fconfig = readFactoryConfig(context);
            fconfig.setModuleAware(false);

            TilesUtil.setTilesUtil(new TilesUtilImpl());
            initDefinitionsFactory(context, fconfig);
        }
        catch(Exception ex) {
            saveExceptionMessage(event.getServletContext(), ex);
                    throw new RuntimeException(ex.getMessage(), ex);
        }

    }

    public void contextDestroyed(ServletContextEvent event) {
        this.definitionFactory = null;
    }


    /**
     * Populates the tiles factory configuration. If a
     * context init param named <i>definitions-config</i>
     * was defined, that param's value is assumed to be
     * a comma-separated list of configuration file names,
     * all of which are processed. If a
     * <i>definitions-config</i> context param was not
     * specified, Tiles assumes that your Tiles definition
     * file is <code>/WEB-INF/tiles.xml</code>.
     */
    protected DefinitionsFactoryConfig readFactoryConfig(ServletContext context)
        throws ServletException {
        DefinitionsFactoryConfig factoryConfig = new DefinitionsFactoryConfig();
        Map map = new HashMap();

        try {
            if(configFiles != null) {
                logger.info("CONFIG FILES DEFINED IN WEB.XML");
               map.put(DEFAULT_CONFIG_FILE_PARAM, configFiles);
           }
            else {
                logger.info("CONFIG FILES WERE NOT DEFINED IN WEB.XML, " +
                              "LOOKING FOR " + DEFAULT_CONFIG_FILE);
               map.put(DEFAULT_CONFIG_FILE_PARAM, DEFAULT_CONFIG_FILE);
            }

            populateConfigParameterMap(context, map);
            factoryConfig.populate(map);
        }
        catch (Exception ex) {
            saveExceptionMessage(context, ex);
           throw new UnavailableException(CANT_POPULATE_FACTORY_ERROR + ex.getMessage());
        }
        return factoryConfig;
    }


    /**
     * Initializes the Tiles definitions factory.
     *
     * @param servletContext The servlet context
     * @param factoryConfig The definitions factory config
     */
   private void initDefinitionsFactory(ServletContext servletContext,
                                       DefinitionsFactoryConfig factoryConfig)
                                                    throws ServletException {
        logger.info("initializing definitions factory...");
        // Create configurable factory
        try {
            TilesContext tilesContext = TilesContextFactory.getInstance(servletContext);
            definitionFactory = TilesUtil.createDefinitionsFactory(
                                            tilesContext, factoryConfig);
        } catch (DefinitionsFactoryException ex) {
                    ex.printStackTrace();
            throw new ServletException(ex.getMessage(), ex);
        }
    }


    /**
     * Stores the message associated with any exception thrown in this
     * servlet in application scope. Tiles later accesses that message
     * if an exception is thrown when the tiles:insert tag is
     * activated.
     *
     * @param context The servlet configuration
     * @param ex An exception
     */
    private void saveExceptionMessage(ServletContext context, Exception ex) {
       logger.warning("Caught exception when initializing definitions factory");
       logger.warning(ex.getMessage());
       logger.warning(ex.toString());
       context.setAttribute("TILES_INIT_EXCEPTION", ex.getMessage());
    }

    /**
     * Populates a map with the parameters contained in the context configuration.
     *
     * @param context The servlet context
     * @param paramMap The map to fill
     */
    private void populateConfigParameterMap(ServletContext context, Map paramMap) {
        Enumeration enumeration;
        String paramName;

        enumeration = context.getInitParameterNames();
        while (enumeration.hasMoreElements()) {
            paramName = (String) enumeration.nextElement();
            if (!paramMap.containsKey(paramName)) {
                paramMap.put(paramName, context.getInitParameter(paramName));
            }
        }
    }
}
