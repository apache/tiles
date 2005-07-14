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

package org.apache.struts.webapp.tiles.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.DefinitionsUtil;
import org.apache.struts.tiles.FactoryNotFoundException;
import org.apache.struts.tiles.NoSuchDefinitionException;


/**
 * Implementation of <strong>Action</strong> that populates an instance of
 * <code>SubscriptionForm</code> from the currently specified subscription.
 *
 * @version $Rev$ $Date$
 */

public final class TestActionTileAction extends Action {


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

      // Try to retrieve tile context
    ComponentContext context = ComponentContext.getContext( request );
    if( context == null )
      {
      request.setAttribute( "actionError", "Can't get component context.");
      return (mapping.findForward("failure"));
      }
      // Get requested test from tile parameter
    String param;

      // Set a definition in this action
    param = (String)context.getAttribute( "set-definition-name" );
    if( param != null )
      {
      try
        {
          // Read definition from factory, but we can create it here.
        ComponentDefinition definition = DefinitionsUtil.getDefinition( param, request, getServlet().getServletContext() );
        //definition.putAttribute( "attributeName", "aValue" );
        DefinitionsUtil.setActionDefinition( request, definition);
        }
       catch( FactoryNotFoundException ex )
        {
        request.setAttribute( "actionError", "Can't get definition factory.");
        return (mapping.findForward("failure"));
        }
       catch( NoSuchDefinitionException ex )
        {
        request.setAttribute( "actionError", "Can't get definition '" + param +"'.");
        return (mapping.findForward("failure"));
        }
       catch( DefinitionsFactoryException ex )
        {
        request.setAttribute( "actionError", "General error '" + ex.getMessage() +"'.");
        return (mapping.findForward("failure"));
        }
      }

      // Overload a parameter
    param = (String)context.getAttribute( "set-attribute" );
    if( param != null )
      {
      context.putAttribute( param, context.getAttribute( "set-attribute-value" ));
      } // end if

	  return (mapping.findForward("success"));

    }


}
