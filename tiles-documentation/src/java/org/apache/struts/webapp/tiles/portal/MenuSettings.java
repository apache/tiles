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

import java.util.ArrayList;
import java.util.List;

  /**
   * Objects of this class hold menu settings for one user.
   */
   public class MenuSettings
   {
       /** List of items */
     protected List items = new ArrayList();

       /**
        * Get list of items
        */
     public List getItems( )
       {
       return items;
       }

       /**
        * Add an item to the list
        */
     public void addItem( Object item )
       {
       items.add( item );
       }

       /**
        * Add all items to the list.
        */
     public void addItems( List list )
       {
       items.addAll( list );
       }

       /**
        * Reset settings
        */
     public void reset()
       {
       items.clear();
       }

     public String toString()
       {
       return "items=" + items;
       }
   }
