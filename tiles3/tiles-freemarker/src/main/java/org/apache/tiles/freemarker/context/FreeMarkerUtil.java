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

package org.apache.tiles.freemarker.context;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.io.NullWriter;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * Utilities for FreeMarker usage in Tiles.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public final class FreeMarkerUtil {

    /**
     * The name of the attribute that holds the compose stack.
     */
    public static final String COMPOSE_STACK_ATTRIBUTE_NAME = "org.apache.tiles.template.COMPOSE_STACK";

    /**
     * Private constructor to avoid instantiation.
     */
    private FreeMarkerUtil() {
    }

    /**
     * Returns true if forced include of the result is needed.
     *
     * @param env The current FreeMarker environment.
     * @return If <code>true</code> the include operation must be forced.
     * @since 2.2.0
     */
    public static boolean isForceInclude(Environment env) {
        return org.apache.tiles.request.servlet.ServletUtil
                .isForceInclude(FreeMarkerRequestUtil.getRequestHashModel(env).getRequest());
    }

    /**
     * Sets the option that enables the forced include of the response.
     *
     * @param env The current FreeMarker environment.
     * @param forceInclude If <code>true</code> the include operation must be
     * forced.
     * @since 2.2.0
     */
    public static void setForceInclude(Environment env, boolean forceInclude) {
        org.apache.tiles.request.servlet.ServletUtil.setForceInclude(
                FreeMarkerRequestUtil.getRequestHashModel(env).getRequest(), forceInclude);
    }

    /**
     * Unwraps a TemplateModel to extract a string.
     *
     * @param model The TemplateModel to unwrap.
     * @return The unwrapped string.
     * @since 2.2.0
     */
    public static String getAsString(TemplateModel model) {
        try {
            return (String) DeepUnwrap.unwrap(model);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException("Cannot unwrap a model", e);
        }
    }

    /**
     * Unwraps a TemplateModel to extract a boolean.
     *
     * @param model The TemplateModel to unwrap.
     * @param defaultValue If the value is null, this value will be returned.
     * @return The unwrapped boolean.
     * @since 2.2.0
     */
    public static boolean getAsBoolean(TemplateModel model, boolean defaultValue) {
        try {
            Boolean retValue = (Boolean) DeepUnwrap.unwrap(model);
            return retValue != null ? retValue : defaultValue;
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException("Cannot unwrap a model", e);
        }
    }

    /**
     * Unwraps a TemplateModel to extract an object.
     *
     * @param model The TemplateModel to unwrap.
     * @return The unwrapped object.
     * @since 2.2.0
     */
    public static Object getAsObject(TemplateModel model) {
        try {
            return DeepUnwrap.unwrap(model);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException("Cannot unwrap a model", e);
        }
    }

    /**
     * Sets an attribute in the desired scope.
     *
     * @param env The FreeMarker current environment.
     * @param name The name of the attribute.
     * @param obj The value of the attribute.
     * @param scope The scope. It can be <code>page</code>, <code>request</code>
     * , <code>session</code>, <code>application</code>.
     * @since 2.2.0
     */
    public static void setAttribute(Environment env, String name, Object obj,
            String scope) {
        if (scope == null) {
            scope = "page";
        }
        if ("page".equals(scope)) {
            try {
                TemplateModel model = env.getObjectWrapper().wrap(obj);
                env.setVariable(name, model);
            } catch (TemplateModelException e) {
                throw new FreeMarkerTilesException(
                        "Error when wrapping an object", e);
            }
        } else if ("request".equals(scope)) {
            FreeMarkerRequestUtil.getRequestHashModel(env).getRequest().setAttribute(name, obj);
        } else if ("session".equals(scope)) {
            FreeMarkerRequestUtil.getRequestHashModel(env).getRequest().getSession().setAttribute(
                    name, obj);
        } else if ("application".equals(scope)) {
            FreeMarkerRequestUtil.getServletContextHashModel(env).getServlet().getServletContext()
                    .setAttribute(name, obj);
        }
    }

    /**
     * Evaluates the body without rendering it.
     *
     * @param body The body to evaluate.
     * @throws TemplateException If something goes wrong during evaluation.
     * @throws IOException If something goes wrong during writing the result.
     * @since 2.2.0
     */
    public static void evaluateBody(TemplateDirectiveBody body)
            throws TemplateException, IOException {
        if (body != null) {
            NullWriter writer = new NullWriter();
            try {
                body.render(writer);
            } finally {
                writer.close();
            }
        }
    }

    /**
     * Renders the body as a string.
     *
     * @param body The body to render.
     * @return The rendered string.
     * @throws TemplateException If something goes wrong during evaluation.
     * @throws IOException If something goes wrong during writing the result.
     * @since 2.2.0
     */
    public static String renderAsString(TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String bodyString = null;
        if (body != null) {
            StringWriter stringWriter = new StringWriter();
            try {
                body.render(stringWriter);
            } finally {
                stringWriter.close();
            }
            bodyString = stringWriter.toString();
        }
        return bodyString;
    }
}
