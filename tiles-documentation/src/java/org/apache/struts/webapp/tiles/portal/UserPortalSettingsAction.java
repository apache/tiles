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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

/**
 * Implementation of <strong>Action</strong> that populates an instance of
 * <code>SubscriptionForm</code> from the currently specified subscription.
 *
 * This action is used as controller for portal settings editor.
 * It does folowing :
 * <ul>
 * <li>Load or create user portal settings</li>
 * <li>Read web form, and set user portal setting accordingly</li>
 * <li>Prepare portal editor needed attributes</li>
 * <li></li>
 * </ul>
 *
 * @version $Rev$ $Date$
 */
public final class UserPortalSettingsAction extends TilesAction {

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

        PortalSettingsForm prefsForm = (PortalSettingsForm) form;

        // Get user portal settings from user context
        PortalSettings settings = UserPortalAction.getSettings(request, context);
        PortalCatalog catalog =
            UserPortalAction.getPortalCatalog(
                context,
                getServlet().getServletContext());

        if (prefsForm.isSubmitted()) { // read arrays

            // Set settings cols according to user choice
            for (int i = 0; i < prefsForm.getNumCol(); i++) {
                settings.setListAt(i, catalog.getTiles(prefsForm.getNewCol(i)));
            }

            prefsForm.reset();

        }

        // Set lists values to be shown
        for (int i = 0; i < settings.getNumCols(); i++) {
            prefsForm.addCol(settings.getListAt(i));
            prefsForm.addColLabels(catalog.getTileLabels(settings.getListAt(i)));
        }

        prefsForm.setChoices(catalog.getTiles());
        prefsForm.setChoiceLabels(catalog.getTilesLabels());

        return null;
    }

}
