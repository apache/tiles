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

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;


  /**
   * Controller example.
   * This controller modify title by adding "ok".
   */
public class TestTileController extends ControllerSupport
{

  public TestTileController()
    {
    }

   /**
    * Method associated to a tile and called when immediately before tile is included.
    * @param tileContext Current tile context.
    * @param request Current request
    * @param response Current response
    * @param servletContext Current servlet context
    */
   public void perform(ComponentContext tileContext,
                       HttpServletRequest request, HttpServletResponse response,
                       ServletContext servletContext)
        throws ServletException, IOException
   {
   System.out.println( "Controller called" );


   String title = (String)tileContext.getAttribute("title");
   title += "- controller called";
   tileContext.putAttribute( "title", title);
   }


}
