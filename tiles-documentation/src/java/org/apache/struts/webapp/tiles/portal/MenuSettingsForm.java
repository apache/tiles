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

import org.apache.struts.action.ActionForm;


/**
 * Action form used to read data from web page form.
 */
public final class MenuSettingsForm extends ActionForm  {

    /** Validate value */
  protected String validate;
    /** empty list used by reset */
  protected String[] empty = {};
    /** list of items selected by user */
  protected String[] selected;

    /**
     * Set selected items
     */
  public void setSelected( String array[] )
    {
    selected = array;
    }

    /**
     * Get selected items
     */
  public String[] getSelected()
    {
    return selected;
    }

    /**
     * Get selected items
     */
  public String[] getSelectedChoices()
    {
    return empty;
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
    selected =  empty;
    validate = null;
    }
}

