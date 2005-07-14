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

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;


/**
 */

public final class PortalPrefsForm extends ActionForm  {

    /** Validate value */
  protected String validate;
    /** empty list used by reset */
  protected String[] empty = {};
    /** list of "remaining" choices */
  protected String[] remaining = empty;
    /** list of user columns */
  protected List columns = new ArrayList();
    /** list of user columns labels */
  protected List columnLabels = new ArrayList();
    /** list of columns selected by user */
  protected List newCols = new ArrayList();

    /** Choice list */
  protected List choice;
    /** Choice list labels */
  protected List choiceLabels;
    /** Is initialized ? */
  protected boolean initialized = false;


    /**
     * Set col
     */
  public void setCol( int index, List col )
    {
    columns.set( index, col);
    }

    /**
     * Add col
     */
  public void addCol( List col )
    {
    columns.add( col);
    }

    /**
     * Get col Labels
     */
  public List getColLabels(int index)
    {
    return (List)columnLabels.get(index);
    }

    /**
     * Set col Labels
     */
  public void setColLabels( int index, List col )
    {
    columnLabels.set( index, col);
    }

    /**
     * Add col Labels
     */
  public void addColLabels( List col )
    {
    columnLabels.add( col);
    }

    /**
     * Get col
     */
  public List getCol(int index)
    {
    return (List)columns.get(index);
    }

    /**
     * Set col Labels
     */
  public void setNewCol( int index, String list[] )
    {
      // ensure capacity
    while( index>=newCols.size())newCols.add(null);
    newCols.set( index, list);
    }

    /**
     * Get col
     */
  public String[] getNewCol(int index)
    {
    if(newCols==null || index>=newCols.size())
      return null;
    return (String[])newCols.get(index);
    }

    /**
     * Get number of columns
     */
  public int getNumCol()
    {
    return newCols.size();
    }

    /**
     * Set list1
     */
  public void setL1( String array[] )
    {
    setNewCol(1, array);
    }
    /**
     * Set list1
     */
  public String[] getL1()
    {
    return getNewCol(1);
    }

    /**
     * Set list1
     */
  public void setL0( String array[] )
    {
    setNewCol(0, array);
    }
    /**
     * Set list1
     */
  public String[] getL0()
    {
    return getNewCol(0);
    }
    /**
     * Set list1
     */
  public void setRemaining( String array[] )
    {
    remaining = array;
    }
    /**
     * Set list1
     */
  public String[] getRemaining()
    {
    return remaining;
    }


    /**
     * Set list1
     */
  public void setChoices( List list )
    {
    choice = list;
    }
    /**
     * Set list1
     */
  public void setChoiceLabels( List list )
    {
    choiceLabels = list;
    }
    /**
     * Set list1
     */
  public List getChoices()
    {
    return choice;
    }
    /**
     * Set list1
     */
  public List getChoiceLabels()
    {
    return choiceLabels;
    }

   /**
    * Is this form submitted ?
    */
  public boolean isSubmitted()
    {
    return validate != null;
    }

   /**
    * Is this form submitted ?
    */
  public void setValidate( String value)
    {
    this.validate = value;
    }

    /**
     * Reset properties
     */
  public void reset()
    {
    remaining =  empty;
    validate = null;
    columns.clear();
    columnLabels.clear();
    newCols.clear();
    }
    /**
     * Initialized flag
     */
  public boolean isInitialized()
    {
    return initialized;
    }
    /**
     * Initialized flag
     */
  public void setInitialized( boolean isInitialized)
    {
    initialized = isInitialized;
    }
}

