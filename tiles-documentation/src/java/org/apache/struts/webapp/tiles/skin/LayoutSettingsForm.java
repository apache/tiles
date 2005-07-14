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

package org.apache.struts.webapp.tiles.skin;

import org.apache.struts.action.ActionForm;

/**
 * Struts form
 */
public class LayoutSettingsForm extends ActionForm
{

    /** Validate value */
  protected String validate;

   /**
    * User selected key value
    */
   private String selected;

   /**
    * Access method for the selectedKey property.
    *
    * @return   the current value of the selectedKey property
    */
   public String getSelected()
   {
      return selected;
   }

   /**
    * Sets the value of the selectedKey property.
    *
    * @param aSelectedKey the new value of the selectedKey property
    */
   public void setSelected(String aSelectedKey)
   {
      selected = aSelectedKey;
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
    selected =  null;
    validate = null;
    }

}
