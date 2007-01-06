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

package org.apache.tiles.showcase.portal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles2.actions.TilesAction;
import org.apache.tiles.ComponentAttribute;
import org.apache.tiles.ComponentContext;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.servlet.ServletTilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;

/**
 * This controller load user menu settings and put them in tile context.
 * If menu settings are not defined for user, defined them based on tiles
 * attributes used as default.
 *
 * This implementation extends Struts Action, and also define Tiles Controller interface.
 * This allows to use it as well as a controller type or a controller url. If used
 * as controller type, Struts Action functionality are not availables.
 *
 * Tiles input attributes :
 * <ul>
 *   <li>title : menu title</li>
 *   <li>items : Menu entries used as default when user settings is created</li>
 *   <li>defaultChoice : Menus or menu entries porposed as choice to user</li>
 *   <li>storeUnderName : Store user settings under provided name in session context [optional]</li>
 *   <li></li>
 * </ul>
 * Tiles output attributes :
 * <ul>
 *   <li>title : menu title</li>
 *   <li>items : Menu items to display</li>
 *   <li></li>
 * </ul>
 *
 * @version $Rev$ $Date$
 */
public final class UserMenuAction extends TilesAction implements ViewPreparer {

	/** 
	 * Commons Logging instance.
	 */
	private static Log log = LogFactory.getLog(UserMenuAction.class);

	/** 
	 * Tile attribute containing name used to store user settings in session 
	 * context.
	 */
	public static String USER_SETTINGS_NAME_ATTRIBUTE = "userSettingsName";

	/** 
	 * Default name used to store settings in session context. 
	 */
	public static String DEFAULT_USER_SETTINGS_NAME =
		"tiles.examples.portal.USER_MENU_SETTINGS";

	/** 
	 * Default name used to store menu catalog in application scope.
	 */
	public static String DEFAULT_MENU_CATALOG_NAME =
		"tiles.examples.portal.MenuCatalog";

	/** 
	 * Tile attribute containing name used to store menu catalog in application 
	 * scope.
	 */
	public static String MENU_CATALOG_NAME_ATTRIBUTE = "catalogName";

	/** 
	 * Tile attribute containing name of the settings definition used to 
	 * initialize catalog. 
	 */
	public static final String CATALOG_SETTING_ATTRIBUTE = "catalogSettings";

	/** 
	 * Tile attribute containing items to render.
	 */
	public static String USER_ITEMS_ATTRIBUTE = "items";

	/**
	 * Struts' action perform().
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 *
	 * @param context The current Tile context, containing Tile attributes.
	 * @param mapping The ActionMapping used to select this instance.
	 * @param form The optional ActionForm bean for this request (if any).
	 * @param request The HTTP request we are processing.
	 * @param response The HTTP response we are creating.
	 *
	 * @exception Exception if the application business logic throws
	 *  an exception
	 * @since Struts 1.1
	 */
	public ActionForward execute(
		ComponentContext context,
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
        
        TilesRequestContext tilesContext = new ServletTilesRequestContext(
                request.getSession().getServletContext(), request, response);

		this.execute(tilesContext, context);

		return null;
	}

	/**
	 * Method associated to a tile and called immediately before tile is included.
	 * @param tileContext Current tile context.
	 * @param request Current request
	 * @param response Current response
	 * @param servletContext Current servlet context
	 */
	public void perform(
		ComponentContext context,
		HttpServletRequest request,
		HttpServletResponse response,
		ServletContext servletContext)
		throws ServletException, IOException {

		log.debug("Enter action UserMenuAction");

		// Load user settings from user context
        
        TilesRequestContext tilesContext = new ServletTilesRequestContext(
                request.getSession().getServletContext(), request, response);
        
		MenuSettings settings = getUserSettings(tilesContext, context);

		// Set parameters for rendering page
		context.putAttribute(USER_ITEMS_ATTRIBUTE, new ComponentAttribute(
                settings.getItems()));

		log.debug("settings=" + settings);
		log.debug("Exit action UserMenuAction");

	}

	/**
	 * Load user setting.
	 * This implementation load setting from user context.
	 * If settings are not found, initialized them from default items defined
	 * in Tile's context.
	 * If settings are not found, initialized them.
	 */
	public static MenuSettings getUserSettings(
		TilesRequestContext tilesContext,
		ComponentContext context)
		throws ServletException {

		// Retrieve attribute name used to store settings.
		String userSettingsName =
			(String) context.getAttribute(USER_SETTINGS_NAME_ATTRIBUTE)
            .getValue();

		if (userSettingsName == null) {
			userSettingsName = DEFAULT_USER_SETTINGS_NAME;
		}

		// Get user list from user context
		MenuSettings settings =
			(MenuSettings) tilesContext.getSessionScope().get(userSettingsName);

		// If settings don't exist, create and initialize them
		// Initialization is done from context attribute denoted by ITEMS
		if (settings == null) {
			// List doesn't exist, create it and initialize it from Tiles parameters
			settings = new MenuSettings();
			try {
				settings.addItems(
					(List) context.getAttribute(USER_ITEMS_ATTRIBUTE));
			} catch (ClassCastException ex) {
				throw new ServletException("Can't initialize user menu : default items must be a list of items");
			}

			// Save user settings in session
			tilesContext.getSessionScope().put(userSettingsName, settings);
		}

		return settings;
	}

	/**
	 * Get catalog of available menu entries.
	 * This implementation creates catalog list from the provided menu bar 
	 * entries.
	 */
	public static List getCatalog(
		ComponentContext context,
		TilesRequestContext tilesContext,
        TilesApplicationContext applicationContext)
		throws ServletException {

		// Retrieve name used to store catalog in application context.
		// If not found, use default name
		String catalogName =
			(String) context.getAttribute(MENU_CATALOG_NAME_ATTRIBUTE)
            .getValue();

		if (catalogName == null) {
			catalogName = DEFAULT_MENU_CATALOG_NAME;
		}

		// Get catalog from context
		List catalog = (List) applicationContext.getApplicationScope()
                .get(catalogName);

		// If not found, initialize it from provided default menu
		if (catalog == null) {
			Object menuBar = context.getAttribute(CATALOG_SETTING_ATTRIBUTE);
			if (menuBar == null) {
				throw new ServletException(
					"Attribute '"
						+ CATALOG_SETTING_ATTRIBUTE
						+ "' must be set. It define entries used in catalog");
			}

			catalog = new ArrayList();
			extractItems(catalog, menuBar, context, tilesContext,
                    applicationContext);
			if (catalog.size() == 0) {
				throw new ServletException("Can't initialize menu items catalog");
			}

			// save it for future use
			applicationContext.getApplicationScope().put(catalogName, catalog);
		}

		return catalog;
	}

	/**
	 * Extract menu items from passed object. Items are stored in 
	 * <code>result</code> parameter. 
	 * This method allows to create a list of menu entries from existing menus.
	 * Check object type class :
	 * <li>
	 *   <ul>MenuItems : add it</ul>
	 *   <ul>ComponentDefinition : get attribute items, or list if not found.
	 *       Call ExtractItems with resulting attribute.
	 *   </ul>
	 *   <ul>List : iterate on list, and call ExtractItems for each element.
	 * </li>
	 * @param result result list (should be initialized)
	 * @param object object to add (MenuItems, Definition, ...)
	 * @param request current request
	 * @param servletContext current servlet context.
	 */
	private static void extractItems(
		List result,
		Object object,
        ComponentContext context,
		TilesRequestContext tilesContext,
		TilesApplicationContext applicationContext) {

		log.debug("Extract menu item from '" + object + "'");
        
        // FIXME This method should be completely rewritten. For the moment
        // I am commenting it.
/*
		if (object instanceof String) { // definition name
			try {

				extractItems(result, def, request, servletContext);

			} catch (Exception ex) { // silently fail
			}

		} else if (object instanceof List) {
			List list = (List) object;
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				extractItems(result, iter.next(), request, servletContext);
			}

		} else if (object instanceof ComponentDefinition) {
			ComponentDefinition definition = (ComponentDefinition) object;
			Object attribute = definition.getAttribute("items");
			if (attribute == null) {
				attribute = definition.getAttribute("list");
			}

			if (attribute == null) {
				return;
			}

			extractItems(result, attribute, request, servletContext);

		} else if (object instanceof MenuItem) {
			result.add(object);
		} */
	}

	/**
	 * @see org.apache.tiles.Controller#execute(org.apache.tiles.ComponentContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
	 */
	public void execute(TilesRequestContext tilesContext,
		ComponentContext tileContext)
		throws Exception {
            
		log.debug("Enter action UserMenuAction");

		// Load user settings from user context
		MenuSettings settings = getUserSettings(tilesContext, tileContext);

		// Set parameters for rendering page
		tileContext.putAttribute(USER_ITEMS_ATTRIBUTE, new ComponentAttribute(
                settings.getItems()));

		log.debug("settings=" + settings);
		log.debug("Exit action UserMenuAction");

	}
}
