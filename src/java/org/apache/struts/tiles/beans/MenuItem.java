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

package org.apache.struts.tiles.beans;

import java.io.Serializable;

/**
 * Interface for MenuItems.
 * @see SimpleMenuItem
 */
public interface MenuItem extends Serializable {
    
    /**
     * Set value property.
     */
    public void setValue(String value);

    /**
     * Get value property.
     */
    public String getValue();

    /**
     * Set link property.
     */
    public void setLink(String link);

    /**
     * Get link property.
     */
    public String getLink();

    /**
     * Set icon property.
     */
    public void setIcon(String link);

    /**
     * Get icon property.
     */
    public String getIcon();

    /**
     * Set tooltip property.
     */
    public void setTooltip(String link);

    /**
     * Get tooltip property.
     */
    public String getTooltip();
}
