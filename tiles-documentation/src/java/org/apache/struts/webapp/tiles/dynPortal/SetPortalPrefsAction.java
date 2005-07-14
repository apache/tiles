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
 * @version $Rev$ $Date$
 */

public final class SetPortalPrefsAction extends Action {


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
	throws Exception
    {
    System.out.println("Enter action SetPortalPrefsAction");
	HttpSession session = request.getSession();
  PortalPrefsForm prefsForm = (PortalPrefsForm)form;

          // Try to retrieve tile context
    ComponentContext context = ComponentContext.getContext( request );
    if( context == null )
      {
      throw new ServletException( "This action must be called by a Tile, not directly" );
      }

      // Get user portal list from user context
    PortalSettings settings = RetrievePortalAction.getSettings( context, session );

  if( prefsForm.isSubmitted() )
    {  // read arrays
    System.out.println("form submitted");

      // Set settings cols according to user choice
    for( int i=0;i<prefsForm.getNumCol(); i++)
      {
      settings.resetListAt( i, prefsForm.getNewCol(i));
      } // end loop


    //settings.resetListAt( 0, prefsForm.getL0());
    //settings.resetListAt( 1, prefsForm.getL1());
    prefsForm.reset();
	  return (mapping.findForward("portal"));
    }

      // Set lists values to be shown
    for( int i=0;i<settings.getNumCols(); i++ )
      {
      prefsForm.addCol(settings.getListAt(i) );
      prefsForm.addColLabels(settings.getListLabelAt(i) );
      } // end loop

    prefsForm.setChoices(settings.getChoices() );
    prefsForm.setChoiceLabels(settings.getChoiceLabels() );

    System.out.println("Exit action SetPortalPrefsAction");
	  return (mapping.findForward("preferences"));
    }


}
