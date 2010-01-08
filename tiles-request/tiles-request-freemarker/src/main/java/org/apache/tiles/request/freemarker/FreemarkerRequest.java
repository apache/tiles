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

package org.apache.tiles.request.freemarker;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.request.AbstractViewRequest;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletRequest;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;

/**
 * The FreeMarker-specific request context.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class FreemarkerRequest extends AbstractViewRequest {

    private static final String[] SCOPES = {"page"};

    /**
     * The FreeMarker current environment.
     */
    private Environment env;

    private Map<String, Object> pageScope;

    /**
     * The request objects.
     */
    private transient Object[] requestObjects;

    public static FreemarkerRequest createServletFreemarkerRequest(
            ApplicationContext applicationContext, Environment env) {
        HttpRequestHashModel requestModel = FreemarkerRequestUtil
                .getRequestHashModel(env);
        HttpServletRequest request = requestModel.getRequest();
        HttpServletResponse response = requestModel.getResponse();
        Request enclosedRequest = new ServletRequest(
                applicationContext, request, response);
        return new FreemarkerRequest(enclosedRequest, env);
    }

    /**
     * Constructor.
     *
     * @param enclosedRequest
     *            The request that exposes non-FreeMarker specific properties
     * @param env
     *            The FreeMarker environment.
     */
    public FreemarkerRequest(Request enclosedRequest,
            Environment env) {
        super(enclosedRequest);
        this.env = env;
    }

    /**
     * Returns the environment object.
     *
     * @return The environment.
     * @since 3.0.0
     */
    public Environment getEnvironment() {
        return env;
    }

    /** {@inheritDoc} */
    @Override
    public Locale getRequestLocale() {
        return env.getLocale();
    }

    public Map<String, Object> getPageScope() {
        if (pageScope == null) {
            pageScope = new EnvironmentScopeMap(env);
        }
        return pageScope;
    }

    @Override
    public String[] getNativeScopes() {
        return SCOPES;
    }

    /** {@inheritDoc} */
    @Override
    public PrintWriter getPrintWriter() {
        Writer writer = env.getOut();
        if (writer instanceof PrintWriter) {
            return (PrintWriter) writer;
        }
        return new PrintWriter(writer);
    }

    /** {@inheritDoc} */
    @Override
    public Writer getWriter() {
        return env.getOut();
    }

    /** {@inheritDoc} */
    @Override
    public Object[] getRequestObjects() {
        if (requestObjects == null) {
            requestObjects = new Object[1];
            requestObjects[0] = env;
        }
        return requestObjects;
    }
}
