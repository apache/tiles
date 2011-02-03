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
package org.apache.tiles.request.freemarker.render;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.freemarker.FreemarkerRequestException;

/**
 * Builds instances of {@link FreemarkerRenderer}.
 *
 * @version $Rev$ $Date$
 */
public final class FreemarkerRendererBuilder {

    /**
     * The initialization parameters.
     */
    private Map<String, String> params = new HashMap<String, String>();

    /**
     * The application context.
     *
     * @since 3.0.0
     */
    private ApplicationContext applicationContext;

    /**
     * Constructor.
     */
    private FreemarkerRendererBuilder() {
    }

    /**
     * Creates a new instance of this class.
     *
     * @return A new instance of the builder.
     */
    public static FreemarkerRendererBuilder createInstance() {
        return new FreemarkerRendererBuilder();
    }

    /**
     * Sets a parameter for the internal servlet.
     *
     * @param key The name of the parameter.
     * @param value The value of the parameter.
     * @return This object.
     */
    public FreemarkerRendererBuilder setParameter(String key, String value) {
        params.put(key, value);
        return this;
    }

    /**
     * Sets the application context.
     *
     * @param applicationContext The application context.
     * @return This object.
     */
    public FreemarkerRendererBuilder setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return this;
    }

    /**
     * Creates a new {@link FreemarkerRenderer} with the given configuration.
     *
     * @return A new Freemarker renderer.
     */
    public FreemarkerRenderer build() {
        AttributeValueFreemarkerServlet servlet = new AttributeValueFreemarkerServlet();
        try {
            servlet.init(new InitParamsServletConfig(params, applicationContext));
            return new FreemarkerRenderer(servlet);
        } catch (ServletException e) {
            throw new FreemarkerRequestException(
                    "Cannot initialize internal servlet", e);
        }

    }

}
