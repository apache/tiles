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
 */

package org.apache.tiles;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tiles controller including a local URL.
 * @author Cedric Dumoulin
 */

public class UrlController implements Controller {

    /** Url associated with this controller. */
  protected String url;

    /**
     * Constructor.
     * @param url URL.
     */
  public UrlController( String url ) {
     this.url=url;
  }

   /**
    * Method associated to a tile and called immediately before the tile is included.
    * This implementation calls a Struts Action. No servlet is set by this method.
    *
    * @param tileContext Current tile context.
    * @param request Current request.
    * @param response Current response.
    * @param servletContext Current servlet context.
    */
   public void perform(ComponentContext tileContext,
                       HttpServletRequest request, HttpServletResponse response,
                       ServletContext servletContext)
     throws ServletException, IOException {
      RequestDispatcher rd = servletContext.getRequestDispatcher( url );
      if( rd == null )
        throw new ServletException( "Controller can't find url '" + url + "'." );

      rd.include( request, response );
   }
}
