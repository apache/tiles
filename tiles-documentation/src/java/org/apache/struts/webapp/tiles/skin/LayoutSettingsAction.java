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

package org.apache.struts.webapp.tiles.skin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

/**
 * Action used to set user skin.
 */
public class LayoutSettingsAction extends TilesAction
{
      /** debug flag */
    public static boolean debug = true;

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     * This method should be implemented by subclasses.
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
        throws Exception
   {
    if(debug)
      System.out.println("Enter action LayoutSettingAction");

    LayoutSettingsForm actionForm = (LayoutSettingsForm)form;

        // Load user menu settings and available list of choices
    String  selected = LayoutSwitchAction.getUserSetting( context, request );
    if(selected==null)
      selected = "default";
    System.out.println("user setting retrieved");
    DefinitionCatalog catalog = LayoutSwitchAction.getCatalog( context, request, getServlet().getServletContext() );
    System.out.println("catalog retrieved");

      // Check if form is submitted
      // If true, read, check and store new values submitted by user.
    if( actionForm.isSubmitted() )
      {  // read arrays
      if(debug)
        System.out.println("form submitted");
      selected = catalog.getKey(actionForm.getSelected());
      if(debug)
        System.out.println( "key : " + selected );
      LayoutSwitchAction.setUserSetting(context, request, selected );
      if(debug)
        System.out.println( "settings : " + selected );
      actionForm.reset();
      } // end if

      // Prepare data for view tile
    context.putAttribute( "selected", selected );
    context.putAttribute( "catalog", catalog );

    if(debug)
      System.out.println("Exit action LayoutSettingAction");
    return null;
   }
}
