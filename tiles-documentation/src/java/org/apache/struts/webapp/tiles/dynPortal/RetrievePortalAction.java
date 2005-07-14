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

package org.apache.struts.webapp.tiles.dynPortal;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;


/**
 * Implementation of <strong>Action</strong> that populates an instance of
 * <code>SubscriptionForm</code> from the currently specified subscription.
 *
 */

public final class RetrievePortalAction extends Action {

      /** Name use to store settings in session context */
    public static String USER_PORTAL_SETTINGS = "DynamicPortal.USER_PORTAL_SETTINGS";
      /** Tile parameter name */
    public static String PARAM_NUMCOLS = "numCols";
      /** Tile parameter name */
    public static String PARAM_LIST = "list";
      /** Tile parameter name */
    public static String PARAM_LIST_LABELS = "listLabels";
    // --------------------------------------------------------- Public Methods


    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if the application business logic throws
     *  an exception
     */
    public ActionForward execute(
				 ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws Exception {
    System.out.println("Enter action RetrievePortalAction");
      // Get current session.
	  HttpSession session = request.getSession();

          // Try to retrieve tile context
    ComponentContext context = ComponentContext.getContext( request );
    if( context == null )
      {
      throw new ServletException( "This action must be called by a Tile, not directly" );
      }

      // Get user portal list from user context
    PortalSettings settings = getSettings( context, session );

      // Set parameters for tiles
    context.putAttribute( "numCols", Integer.toString(settings.getNumCols()) );
    for( int i=0; i<settings.getNumCols(); i++ )
      context.putAttribute( "list"+i, settings.getListAt(i) );

    System.out.println("Exit action RetrievePortalAction");
	  return (mapping.findForward("success"));
    }

    /**
     * Retrieve user setting from session.
     * If settings are not found, initialized them.
     */
  public static PortalSettings getSettings( ComponentContext context, HttpSession session )
  {
      // Get user portal list from user context
    PortalSettings settings = (PortalSettings)session.getAttribute( USER_PORTAL_SETTINGS );

    if( settings == null )
      { // List doesn't exist, create it and initialize it from Tiles parameters
      settings = new PortalSettings();
      settings.setNumCols( (String)context.getAttribute( PARAM_NUMCOLS ) );
      for( int i=0; i<settings.getNumCols(); i++ )
        {
        List col = (List)context.getAttribute( ((String)PARAM_LIST+i) );
        List labels = (List)context.getAttribute( ((String)PARAM_LIST_LABELS+i) );
        settings.addChoices( col, labels );
        settings.addList( col );
        } // end loop

        // Save user settings in session
      session.setAttribute( USER_PORTAL_SETTINGS, settings);
      } // end if

  return settings;
  }


}

