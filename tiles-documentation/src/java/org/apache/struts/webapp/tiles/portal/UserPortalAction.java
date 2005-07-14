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

package org.apache.struts.webapp.tiles.portal;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

/**
 * This controller load user portal settings and put them in tile context.
 * If portal settings are not defined for user, defined them based on tiles
 * attributes used as default.
 *
 * @version $Rev$ $Date$
 */
public final class UserPortalAction extends TilesAction {

    /** 
     * Default name used to store settings in session context.
     */
    public static String DEFAULT_USER_SETTINGS_NAME =
        "tiles.examples.portal.USER_PORTAL_SETTINGS";

    /** 
     * Tile attribute containing number of columns in portal.
     */
    public static String NUMCOLS_ATTRIBUTE = "numCols";

    /** 
     * Tile attribute containing list prefix name.
     */
    public static String LIST_ATTRIBUTE = "list";

    /** 
     * Tile attribute containing list of labels prefix name.
     */
    public static String LIST_LABELS_ATTRIBUTE = "labels";

    /** 
     * Tile attribute containing name used to store user settings in session 
     * context. 
     */
    public static String USER_SETTINGS_NAME_ATTRIBUTE = "userSettingsName";

    /** 
     * Name used to store portal catalog in application scope. 
     */
    public static String PORTAL_CATALOG_NAME = "tiles.examples.portal.PortalCatalog";

    /**
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

        // Get user portal list from user context
        PortalSettings settings = getSettings(request, context);

        // Set parameters for tiles
        context.putAttribute("numCols", Integer.toString(settings.getNumCols()));

        for (int i = 0; i < settings.getNumCols(); i++) {
            context.putAttribute("list" + i, settings.getListAt(i));
        }

        return null;
    }

    /**
     * Retrieve user setting from session.
     * If settings are not found, initialized them.
     */
    public static PortalSettings getSettings(
        HttpServletRequest request,
        ComponentContext context) {

        // Get current session.
        HttpSession session = request.getSession();

        // Retrieve user context id used to store settings
        String userSettingsId =
            (String) context.getAttribute(USER_SETTINGS_NAME_ATTRIBUTE);

        if (userSettingsId == null) {
            userSettingsId = DEFAULT_USER_SETTINGS_NAME;
        }

        // Get user portal list from user context
        PortalSettings settings =
            (PortalSettings) session.getAttribute(userSettingsId);

        if (settings == null) {
            // List doesn't exist, create it and initialize it from Tiles parameters
            settings = new PortalSettings();
            settings.setNumCols((String) context.getAttribute(NUMCOLS_ATTRIBUTE));

            for (int i = 0; i < settings.getNumCols(); i++) {
                List tiles =
                    (List) context.getAttribute(((String) LIST_ATTRIBUTE + i));

                settings.setListAt(i, tiles);
            }

            // Save user settings in session
            session.setAttribute(userSettingsId, settings);
        }

        return settings;
    }

    /**
     * Retrieve portal choices object.
     * Create it from default values if needed.
     */
    static public PortalCatalog getPortalCatalog(
        ComponentContext context,
        ServletContext servletContext) {

        PortalCatalog catalog =
            (PortalCatalog) servletContext.getAttribute(PORTAL_CATALOG_NAME);

        if (catalog == null) { // Initialize catalog
            catalog = new PortalCatalog();
            int numCols =
                Integer.parseInt((String) context.getAttribute(NUMCOLS_ATTRIBUTE));

            for (int i = 0; i < numCols; i++) {
                List tiles =
                    (List) context.getAttribute(((String) LIST_ATTRIBUTE + i));

                List labels =
                    (List) context.getAttribute(
                        ((String) LIST_LABELS_ATTRIBUTE + i));

                catalog.addTiles(tiles, labels);
            }
            servletContext.setAttribute(PORTAL_CATALOG_NAME, catalog);
        }

        return catalog;
    }

}
