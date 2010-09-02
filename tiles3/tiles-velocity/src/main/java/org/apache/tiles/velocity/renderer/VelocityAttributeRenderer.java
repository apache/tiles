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

package org.apache.tiles.velocity.renderer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.tiles.Attribute;
import org.apache.tiles.impl.InvalidTemplateException;
import org.apache.tiles.renderer.impl.AbstractTypeDetectingAttributeRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.collection.IteratorEnumeration;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.util.ApplicationContextAware;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.JeeConfig;
import org.apache.velocity.tools.view.VelocityView;

/**
 * Attribute renderer for rendering Velocity templates as attributes. <br>
 * It is available only to Servlet-based environment.<br>
 * It uses {@link VelocityView} to render the response.<br>
 * To initialize it correctly, call {@link #setParameter(String, String)} for
 * all the parameters that you want to set, and then call {@link #commit()}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class VelocityAttributeRenderer extends
        AbstractTypeDetectingAttributeRenderer implements
        ApplicationContextAware {

    /**
     * The VelocityView object to use.
     */
    private VelocityView velocityView;

    /**
     * The initialization parameters for VelocityView.
     */
    private Map<String, String> params = new HashMap<String, String>();

    /**
     * The application context.
     *
     * @since 3.0.0
     */
    private ApplicationContext applicationContext;

    /**
     * Sets a parameter for the internal servlet.
     *
     * @param key The name of the parameter.
     * @param value The value of the parameter.
     * @since 2.2.0
     */
    public void setParameter(String key, String value) {
        params.put(key, value);
    }

    /**
     * Sets the application context.
     *
     * @param applicationContext The application context.
     * @since 3.0.0
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Commits the parameters and makes this renderer ready for the use.
     *
     * @since 2.2.0
     */
    public void commit() {
        velocityView = new VelocityView(new TilesApplicationContextJeeConfig());
    }

    /** {@inheritDoc} */
    @Override
    public void write(Object value, Attribute attribute,
            Request request) throws IOException {
        if (value != null) {
            if (value instanceof String) {
                ServletRequest servletRequest = org.apache.tiles.request.servlet.ServletUtil.getServletRequest(request);
                // then get a context
                Context context = velocityView.createContext(servletRequest
                        .getRequest(), servletRequest.getResponse());

                // get the template
                Template template = velocityView.getTemplate((String) value);

                // merge the template and context into the writer
                velocityView.merge(template, context, request.getWriter());
            } else {
                throw new InvalidTemplateException(
                        "Cannot render a template that is not a string: "
                                + value.toString());
            }
        } else {
            throw new InvalidTemplateException("Cannot render a null template");
        }
    }

    /** {@inheritDoc} */
    public boolean isRenderable(Object value, Attribute attribute,
            Request request) {
        if (value instanceof String) {
            String string = (String) value;
            return string.startsWith("/") && string.endsWith(".vm");
        }
        return false;
    }

    /**
     * Implements JeeConfig to use parameters set through
     * {@link VelocityAttributeRenderer#setParameter(String, String)}.
     *
     * @version $Rev$ $Date$
     * @since 2.2.0
     */
    private class TilesApplicationContextJeeConfig implements JeeConfig {

        /** {@inheritDoc} */
        public String getInitParameter(String name) {
            return params.get(name);
        }

        /** {@inheritDoc} */
        public String findInitParameter(String key) {
            return params.get(key);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public Enumeration getInitParameterNames() {
            return new IteratorEnumeration(params.keySet().iterator());
        }

        /** {@inheritDoc} */
        public String getName() {
            return "Tiles Application Context JEE Config";
        }

        /** {@inheritDoc} */
        public ServletContext getServletContext() {
            return org.apache.tiles.request.servlet.ServletUtil.getServletContext(applicationContext);
        }
    }
}
