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
import org.apache.tiles.awareness.ExpressionAware;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A MenuItem implementation.
 * Used to read menu items in definitions.
 *
 * @version $Rev$ $Date$
 */
public class SimpleMenuItem implements MenuItem, Serializable, ExpressionAware {

    private static final Logger log = LoggerFactory.getLogger(SimpleMenuItem.class);

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
            buff.append("value=").append(getValue());
        }

        if (getLink() != null) {
            if (buff.length() > 0) {
                buff.append(", ");
            }
            buff.append("link=").append(getLink());
        }

        if (getTooltip() != null) {
            if (buff.length() > 0) {
                buff.append(", ");
            }
            buff.append("tooltip=").append(getTooltip());
        }

        if (getIcon() != null) {
            if (buff.length() > 0) {
                buff.append(", ");
            }
            buff.append("icon=").append(getIcon());
        }

        buff.append("]");
        return buff.toString();
    }

    /**
     * Evaluates all values for expressions replacing their contents with the
     * result of the evaluation.
     *
     * In the event of a {@link ClassCastException} the original value is used.
     *
     * @param   eval
     *          Evaluator instance used to evaluate expressions.
     * @param   request
     *          Request object to evaluate expressions with.
     */
    @Override
    public void evaluateExpressions(AttributeEvaluator eval, Request request) {
        value = safeEval(eval, request, value);
        link = safeEval(eval, request, link);
        icon = safeEval(eval, request, icon);
        tooltip = safeEval(eval, request, tooltip);
    }

    private String safeEval(AttributeEvaluator eval, Request request, String val) {
        if (val == null || val.length() == 0) {
            return val;
        }
        try {
            Object res = eval.evaluate(val, request);
            return res == null ? null : res.toString();
        } catch (Exception ex) {
            log.warn("Could not evaluate expressions for SimpleMenuItem: {}", ex.getMessage(), ex);
        }
        return val;
    }

}
