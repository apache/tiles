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
   * Objects of this class hold portal settings for one user.
   */
   public class PortalSettings
   {
       /** Number of columms*/
     protected int numCols;
       /** List of lists (one per column) */
     protected List lists = new ArrayList();

       /**
        * Get number of columns.
        */
     public int getNumCols()
       {
       return numCols;
       }
       /**
        * Set number of columns
        */
     public void setNumCols( String numCols )
       {
       setNumCols( Integer.parseInt(numCols) );
       }
       /**
        * Set number of columns.
        * Ensure capacity for internal list.
        */
     public void setNumCols( int numCols )
       {
       this.numCols = numCols;
       }
       /**
        * Get list at specified index
        */
     public List getListAt( int index )
       {
       return (List)lists.get(index);
       }

       /**
        * Add a list without checking
        */
     public void addList( List list )
       {
       lists.add( list);
       }

       /**
        * Set list at specified index. Previous list is disguarded.
        * Add empty list if necessary.
        * Indexes go from 0 to numCols-1
        * @param index index of the list to add.
        * @param list list to set.
        */
     public void setListAt( int index, List list )
       {
         // First, ensure capacity
       while( index>lists.size() ) lists.add(new ArrayList());
       lists.add( index, list);
       }

       /**
        * Reset settings
        */
     public void reset()
       {
       numCols = 0;
       lists.clear();
       }

     public String toString()
       {
       return "colCount=" + numCols
              + " , lists=" + lists;
       }
   }
