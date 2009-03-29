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
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.io.NullWriter;
import org.apache.tiles.impl.NoSuchContainerException;
import org.apache.tiles.servlet.context.ServletUtil;

import freemarker.core.Environment;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
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
public class FreeMarkerUtil {

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
        return ServletUtil
                .isForceInclude(getRequestHashModel(env).getRequest());
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
        ServletUtil.setForceInclude(getRequestHashModel(env).getRequest(),
                forceInclude);
    }

    /**
     * Returns a specific Tiles container.
     *
     * @param env The current FreeMarker environment.
     * @param key The key under which the container is stored. If null, the
     * default container will be returned.
     * @return The requested Tiles container.
     * @since 2.2.0
     */
    public static TilesContainer getContainer(Environment env, String key) {
        if (key == null) {
            key = TilesAccess.CONTAINER_ATTRIBUTE;
        }
        return (TilesContainer) getServletContextHashModel(env).getServlet()
                .getServletContext().getAttribute(key);
    }

    /**
     * Sets the current container to use in web pages.
     * 
     * @param env The current FreeMarker environment.
     * @param key The key under which the container is stored.
     * @since 2.2.0
     */
    public static void setCurrentContainer(Environment env, String key) {
        TilesContainer container = getContainer(env, key);
        if (container != null) {
            getRequestHashModel(env).getRequest().setAttribute(
                    ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, container);
        } else {
            throw new NoSuchContainerException("The container with the key '"
                    + key + "' cannot be found");
        }
    }

    /**
     * Sets the current container to use in web pages.
     * 
     * @param env The current FreeMarker environment.
     * @param container The container to use as the current container.
     * @since 2.2.0
     */
    public static void setCurrentContainer(Environment env,
            TilesContainer container) {
        ServletUtil.setCurrentContainer(getRequestHashModel(env).getRequest(),
                getServletContextHashModel(env).getServlet()
                        .getServletContext(), container);
    }

    /**
     * Returns the current container that has been set, or the default one.
     * 
     * @param env The current FreeMarker environment.
     * @return The current Tiles container to use in web pages.
     * @since 2.2.0
     */
    public static TilesContainer getCurrentContainer(Environment env) {
        return ServletUtil.getCurrentContainer(getRequestHashModel(env)
                .getRequest(), getServletContextHashModel(env).getServlet()
                .getServletContext());
    }

    /**
     * Returns the HTTP request hash model.
     * 
     * @param env The current FreeMarker environment.
     * @return The request hash model.
     * @since 2.2.0
     */
    public static HttpRequestHashModel getRequestHashModel(Environment env) {
        try {
            return (HttpRequestHashModel) env.getDataModel().get(
                    FreemarkerServlet.KEY_REQUEST);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException(
                    "Exception got when obtaining the request hash model", e);
        }
    }

    /**
     * Returns the servlet context hash model.
     * 
     * @param env The current FreeMarker environment.
     * @return The servlet context hash model.
     * @since 2.2.0
     */
    public static ServletContextHashModel getServletContextHashModel(
            Environment env) {
        try {
            return (ServletContextHashModel) env.getDataModel().get(
                    FreemarkerServlet.KEY_APPLICATION);
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException(
                    "Exception got when obtaining the application hash model",
                    e);
        }
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
     * @param scope The scope. It can be <code>page</code>, <code>request</code>, <code>session</code>, <code>application</code>.
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
            getRequestHashModel(env).getRequest().setAttribute(name, obj);
        } else if ("session".equals(scope)) {
            getRequestHashModel(env).getRequest().getSession().setAttribute(
                    name, obj);
        } else if ("application".equals(scope)) {
            getServletContextHashModel(env).getServlet().getServletContext()
                    .setAttribute(name, obj);
        }
    }
    
    /**
     * Returns the current compose stack, or creates a new one if not present.
     * 
     * @param env The current FreeMarker environment.
     * @return The compose stack.
     * @since 2.2.0
     */
    @SuppressWarnings("unchecked")
    public static Stack<Object> getComposeStack(Environment env) {
        HttpServletRequest request = getRequestHashModel(env).getRequest();
        Stack<Object> composeStack = (Stack<Object>) request
                .getAttribute(COMPOSE_STACK_ATTRIBUTE_NAME);
        if (composeStack == null) {
            composeStack = new Stack<Object>();
            request.setAttribute(COMPOSE_STACK_ATTRIBUTE_NAME, composeStack);
        }
        return composeStack;
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
