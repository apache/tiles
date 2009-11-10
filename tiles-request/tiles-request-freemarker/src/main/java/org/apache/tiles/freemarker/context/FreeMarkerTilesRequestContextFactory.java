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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.awareness.TilesRequestContextFactoryAware;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.NotAvailableFeatureException;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.TilesRequestContextFactory;
import org.apache.tiles.request.servlet.ServletTilesRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;

/**
 * The FreeMarker-specific request context factory.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class FreeMarkerTilesRequestContextFactory implements
        TilesRequestContextFactory, TilesRequestContextFactoryAware {

    /**
     * The logging object.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Parent Tiles context factory.
     */
    private TilesRequestContextFactory parent;

    /** {@inheritDoc} */
    public void setRequestContextFactory(
            TilesRequestContextFactory contextFactory) {
        parent = contextFactory;
    }

    /** {@inheritDoc} */
    public Request createRequestContext(
            ApplicationContext context, Object... requestItems) {
        if (requestItems.length == 1 && requestItems[0] instanceof Environment) {
            Environment env = (Environment) requestItems[0];
            HttpRequestHashModel requestModel;
            try {
                requestModel = FreeMarkerRequestUtil.getRequestHashModel(env);
            } catch (NotAvailableFeatureException e) {
                log.warn("Cannot evaluate as a FreeMarker in Servlet Environment, skipping", e);
                return null;
            }
            HttpServletRequest request = requestModel.getRequest();
            HttpServletResponse response = requestModel.getResponse();
            Request enclosedRequest;
            if (parent != null) {
                enclosedRequest = parent.createRequestContext(context, request,
                        response);
            } else {
                enclosedRequest = new ServletTilesRequestContext(context,
                        (HttpServletRequest) request,
                        (HttpServletResponse) response);
            }
            return new FreeMarkerTilesRequestContext(enclosedRequest, env);
        }
        return null;
    }
}
