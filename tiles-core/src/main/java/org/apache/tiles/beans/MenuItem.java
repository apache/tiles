/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.tiles.beans;

import java.io.Serializable;

/**
 * Interface for MenuItems.
 *
 * @see SimpleMenuItem
 * @version $Rev$ $Date$
 */
public interface MenuItem extends Serializable {

    /**
     * Sets the value (i.e. the visible part) of this menu item.
     *
     * @param value The value of this item. 
     */
    public void setValue(String value);

    /**
     * Returns the value (i.e. the visible part) of this menu item.
     *
     * @return The value of this item. 
     */
    public String getValue();

    /**
     * Sets the URL of this menu item.
     *
     * @param link The URL of this item.
     */
    public void setLink(String link);

    /**
     * Returns the URL of this menu item.
     *
     * @return The URL of this item.
     */
    public String getLink();

    /**
     * Sets the icon URL of this menu item.
     *
     * @param link The icon URL.
     */
    public void setIcon(String link);

    /**
     * Returns the icon URL of this menu item.
     *
     * @return The icon URL.
     */
    public String getIcon();

    /**
     * Sets the tooltip text.
     * 
     * @param link The tooltip text.
     */
    public void setTooltip(String link);

    /**
     * Returns the tooltip text.
     * 
     * @return The tooltip text.
     */
    public String getTooltip();
}
