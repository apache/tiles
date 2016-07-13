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
 */
package org.apache.tiles.beans;

import java.io.Serializable;

/**
 * A MenuItem implementation.
 * Used to read menu items in definitions.
 *
 * @version $Rev$ $Date$
 */
public class SimpleMenuItem implements MenuItem, Serializable {

    /**
     * The value of the item, i.e. what is really visible to the user.
     */
    private String value = null;

    /**
     * The link where the menu item points to.
     */
    private String link = null;

    /**
     * The (optional) icon image URL.
     */
    private String icon = null;

    /**
     * The (optional) tooltip text.
     */
    private String tooltip = null;

    /**
     * Constructor.
     */
    public SimpleMenuItem() {
        super();
    }

    /**
     * Sets the value of the item, i.e. what is really visible to the user.
     *
     * @param value The value of the item.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the value of the item, i.e. what is really visible to the user.
     *
     * @return The value of the item.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the link where the menu item points to.
     *
     * @param link The link.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Returns the link where the menu item points to.
     *
     * @return The link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the (optional) icon image URL.
     *
     * @param icon The icon URL.
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the (optional) icon image URL.
     *
     * @return The icon URL.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the (optional) tooltip text.
     *
     * @param tooltip The tooltip text.
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Returns the (optional) tooltip text.
     *
     * @return The tooltip text.
     */
    public String getTooltip() {
        return tooltip;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder("SimpleMenuItem[");

        if (getValue() != null) {
            buff.append("value=").append(getValue()).append(", ");
        }

        if (getLink() != null) {
            buff.append("link=").append(getLink()).append(", ");
        }

        if (getTooltip() != null) {
            buff.append("tooltip=").append(getTooltip()).append(", ");
        }

        if (getIcon() != null) {
            buff.append("icon=").append(getIcon()).append(", ");
        }

        buff.append("]");
        return buff.toString();
    }

}
