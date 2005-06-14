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

package org.apache.tiles.servlets;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tiles.*;

/**
 * This is the entry point for Tiles. The <code>TilesServlet</code> initializes
 * Tiles by creating a factory of tile definitions.
 * <p/>
 * <strong>Using the Tiles Servlet</strong>
 * <p/>
 * Add this servlet to your web.xml file,
 * like this:
 * <pre>
&lt;web-app&gt;
   ...
&lt;servlet&gt;
   &lt;servlet-name&gt;<strong>Tiles Servlet</strong>&lt;/servlet-name&gt;
   &lt;servlet-class&gt;<strong>org.apache.tiles.servlets.TilesServlet</strong>&lt;/servlet-class&gt;
   &lt;load-on-startup/&gt;
&lt;/servlet&gt;
   ...
&lt;/web-app&gt;
 * </pre>Notice there are no mappings for this servlet. That's because
 * this servlet does everything when it's loaded. After that, it does not
 * service requests. This is not a front-controller servlet, like the Struts or JSF servlets.<br/><br/>
 * TilesServlet reads through your Tiles configuration file(s) and stores
 * tile definitions in a factory. That factory is accessed by the Tiles
 * tag libraries. You can specify a configuration file like this:
 * <p/>
 * <pre>&lt;web-app&gt;
   ...
&lt;servlet&gt;
   &lt;servlet-name&gt;Tiles Servlet&lt;/servlet-name&gt;
   &lt;servlet-class&gt;org.apache.tiles.servlets.TilesServlet&lt;/servlet-class&gt;
   &lt;init-param&gt;
      &lt;param-name&gt;<strong>definitions-config</strong>&lt;/param-name&gt;
      &lt;param-value&gt;<strong>/WEB-INF/tiles.xml</strong>&lt;/param-value&gt;
   &lt;/init-param&gt;
   &lt;load-on-startup/&gt;
&lt;/servlet&gt;
   ...
&lt;/web-app&gt;</pre>Notice that we didn't specify a config file in the first servlet
definition. In that case, Tiles assumes the existence of a file named /WEB-INF/tiles.xml. You
can also define multiple configuration files like this:
<p/><pre>&lt;web-app&gt;
   ...
&lt;servlet&gt;
   &lt;servlet-name&gt;Tiles Servlet&lt;/servlet-name&gt;
   &lt;servlet-class&gt;org.apache.tiles.servlets.TilesServlet&lt;/servlet-class&gt;
   &lt;init-param&gt;
      &lt;param-name&gt;<strong>definitions-config</strong>&lt;/param-name&gt;
      &lt;param-value&gt;<strong>/WEB-INF/tile-adaptors.xml, /WEB-INF/tiles.xml</strong>&lt;/param-value&gt;
   &lt;/init-param&gt;
   &lt;load-on-startup/&gt;
&lt;/servlet&gt;
   ...
&lt;/web-app&gt;</pre>Here, we've specified two config files for Tiles to process. Both
 * files must exist and each can reference definitions made in the other. 
 * <p/>
 * <strong>Exception Handling</strong>
 * <p/>
 * When Tiles was bundled with Struts it reliably produced the same error message if anything
 * was amiss in your Tiles configuration file: <i>Can't find definitions config file.</i>
 * Standalone Tiles, OTOH, will display explicit error messages, such as <i>Error while parsing file
 * '/WEB-INF/tiles.xml'. The element type "tiles-definitions" must be terminated by the matching 
 * end-tag tiles-definitions".</i> The following explains how it works.
 * <p/>
 * The Tiles servlet reads your tile configuration file(s) when the Tiles servlet is loaded. If 
 * problems are found with your configuration files, the Tiles servlet of yesteryear would 
 * throw a <code>FactoryNotFound</code> exception and log the error message to the servlet 
 * container's log. Subsequently, when the <code>tiles:insert</code> tag blows up because 
 * there's no definition factory, Tiles would throw an exception with the familiar 
 * <i>Cant find definitions config file</i> message. It was up to you to dig through 
 * the servlet container's log to find out what really went wrong.
 *
 * The standalone Tiles servlet, OTOH, places the exception's message in application scope
 * and retrieves it when tiles:insert blows up. It throws an exception with the original error 
 * message, which is subsequently displayed in the browser, saving you the trouble of looking
 * at the log file.
 * <p>
 * @author David Geary
 */
public class TilesServlet extends HttpServlet {
	 

	/**
	 * The logger for this class
	*/
   protected static Logger logger = Logger.getLogger(TilesServlet.class.
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
	 

	/**
	 * Initializes the servlet by creating the Tiles definition
	 * factory and placing that factory in application scope. The
	 * Tiles tags will subsequently access that factory.
	 *
	 * @param config The servlet config
	 */
	public void init(ServletConfig config) 
		throws javax.servlet.ServletException {
		logger.info("Initializing TilesServlet");
		configFiles = config.getInitParameter("definitions-config");

		try {
			// Create factory config object
			DefinitionsFactoryConfig fconfig = readFactoryConfig();
			fconfig.setModuleAware(false);

			ServletContext context = config.getServletContext();
			TilesUtil.setTilesUtil(new TilesUtilStrutsImpl());
			initDefinitionsFactory(context, fconfig);
		}
		catch(Exception ex) {
			saveExceptionMessage(config, ex);
			throw new ServletException(ex.getMessage());
		}
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
	 *
	 * @param config The servlet config
	 */
	protected DefinitionsFactoryConfig readFactoryConfig() 
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

			factoryConfig.populate(map);
		} 
		catch (Exception ex) {
			saveExceptionMessage(getServletConfig(), ex);
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
			definitionFactory = DefinitionsUtil.createDefinitionsFactory(
											servletContext, factoryConfig);
		} catch (DefinitionsFactoryException ex) {
			saveExceptionMessage(getServletConfig(), ex);
			throw new ServletException(ex.getMessage());
		}
	}


	/**
	 * Stores the message associated with any exception thrown in this
	 * servlet in application scope. Tiles later accesses that message
	 * if an exception is thrown when the tiles:insert tag is
	 * activated.
	 *
	 * @param servletContext The servlet context
	 * @param ex An exception
	 */
	private void saveExceptionMessage(ServletConfig config, Exception ex) {
	   logger.warning("Caught exception when initializing definitions factory");
	   logger.warning(ex.getMessage());
	   logger.warning(ex.toString());
	   config.getServletContext().setAttribute(Globals.TILES_INIT_EXCEPTION, ex.getMessage());
	}
}
