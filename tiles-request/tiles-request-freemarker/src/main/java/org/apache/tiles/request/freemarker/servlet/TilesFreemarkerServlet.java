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

package org.apache.tiles.request.freemarker.servlet;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.tiles.request.reflect.ClassUtil;

import freemarker.cache.TemplateLoader;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;

/**
 * Extends FreemarkerServlet to load Tiles directives as a shared variable.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesFreemarkerServlet extends FreemarkerServlet {

    private static final long serialVersionUID = 4301098067909854507L;

    public static String CUSTOM_SHARED_VARIABLE_FACTORIES_INIT_PARAM =
        "org.apache.tiles.request.freemarker.CUSTOM_SHARED_VARIABLE_FACTORIES";

    private Map<String, SharedVariableFactory> name2variableFactory = new LinkedHashMap<String, SharedVariableFactory>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        String param = config.getInitParameter(CUSTOM_SHARED_VARIABLE_FACTORIES_INIT_PARAM);
        if (param != null) {
            String[] couples = param.split("\\s*;\\s*");
            for (int i=0; i < couples.length; i++) {
                String[] couple = couples[i].split("\\s*,\\s*");
                if (couple == null || couple.length != 2) {
                    throw new ServletException(
                            "Cannot parse custom shared variable partial init param: '"
                                    + couples[i] + "'");
                }
                name2variableFactory.put(couple[0],
                        (SharedVariableFactory) ClassUtil.instantiate(couple[1]));
            }
        }
        super.init(new ExcludingParameterServletConfig(config));
    }

    public void addSharedVariableFactory(String variableName, SharedVariableFactory factory) {
        name2variableFactory.put(variableName, factory);
    }

    /** {@inheritDoc} */
    @Override
    protected Configuration createConfiguration() {
        Configuration configuration = super.createConfiguration();

        for (Map.Entry<String, SharedVariableFactory> entry : name2variableFactory.entrySet()) {
            configuration.setSharedVariable(entry.getKey(), entry.getValue().create());
        }
        return configuration;
    }

    /** {@inheritDoc} */

    @Override
    protected TemplateLoader createTemplateLoader(String templatePath) {
        return new WebappClassTemplateLoader(getServletContext());
    }

    private class ExcludingParameterServletConfig implements ServletConfig {

        private ServletConfig config;

        public ExcludingParameterServletConfig(ServletConfig config) {
            this.config = config;
        }

        @Override
        public String getServletName() {
            return config.getServletName();
        }

        @Override
        public ServletContext getServletContext() {
            return config.getServletContext();
        }

        @Override
        public String getInitParameter(String name) {
            if (CUSTOM_SHARED_VARIABLE_FACTORIES_INIT_PARAM.equals(name)) {
                return null;
            }
            return config.getInitParameter(name);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Enumeration getInitParameterNames() {
            return new SkippingEnumeration(config.getInitParameterNames());
        }

    }

    private static class SkippingEnumeration implements Enumeration<String> {

        private Enumeration<String> enumeration;

        private String next = null;

        public SkippingEnumeration(Enumeration<String> enumeration) {
            this.enumeration = enumeration;
            updateNextElement();
        }

        @Override
        public boolean hasMoreElements() {
            return next != null;
        }

        @Override
        public String nextElement() {
            String retValue = next;
            updateNextElement();
            return retValue;
        }

        private void updateNextElement() {
            String value = null;
            while (this.enumeration.hasMoreElements()
                    && (value = this.enumeration.nextElement())
                            .equals(CUSTOM_SHARED_VARIABLE_FACTORIES_INIT_PARAM));
            next = value;
        }

    }
}
