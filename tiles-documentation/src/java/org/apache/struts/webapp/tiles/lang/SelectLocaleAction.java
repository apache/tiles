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

package org.apache.struts.webapp.tiles.lang;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.taglib.ComponentConstants;

public final class SelectLocaleAction extends Action {


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

	// Extract parameters we will need
    String requested = (String)request.getParameter( "language" );

    if( requested == null )
 	    return (mapping.findForward("failed"));
    if( requested.equalsIgnoreCase( "FR" ) )
      setLocale( request, Locale.FRANCE );
    if( requested.equalsIgnoreCase( "UK" ) )
      setLocale( request, Locale.UK );
    if( requested.equalsIgnoreCase( "DE" ) )
      setLocale( request, Locale.GERMAN );

    //System.out.println( "action perform called" );
      // Fill in nested classes
	  return (mapping.findForward("success"));
    }

    protected void setLocale( HttpServletRequest request, Locale locale )
      {
      HttpSession session = request.getSession(false);
      if (session != null)
        session.setAttribute(ComponentConstants.LOCALE_KEY, locale);

      }
}
