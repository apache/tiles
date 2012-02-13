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

package org.apache.tiles.request;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sampullara.mustache.Scope;
import org.apache.tiles.request.collection.ScopeMap;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.extractor.ApplicationScopeExtractor;
import org.apache.tiles.request.servlet.extractor.RequestScopeExtractor;
import org.apache.tiles.request.servlet.extractor.SessionScopeExtractor;


/**
 * The Mustache-specific request context.
 *
 * @version $Rev: 1215006 $ $Date: 2011-12-16 01:30:41 +0100 (Fri, 16 Dec 2011) $
 * @since 3.0
 */
public class MustacheRequest extends AbstractViewRequest {

    /**
     * The natively available scopes. In fact, only "page".
     */
    private static final String[] SCOPES = {"page"};

    private MustacheScopeMap pageScope = null;

    /**
     * Creates a new Mustache request.
     *
     * @param applicationContext The application context.
     * @param request The request.
     * @param response The response.
     * @return A new request.
     */
    public static MustacheRequest createServletMustacheRequest(
            ApplicationContext applicationContext,
            HttpServletRequest request,
            HttpServletResponse response) {

        DispatchRequest enclosedRequest = new ServletRequest(applicationContext, request, response);

        return new MustacheRequest(
                enclosedRequest,
                new Scope(new ScopeMap(new RequestScopeExtractor(request)),
                new Scope(new ScopeMap(new SessionScopeExtractor(request)),
                new Scope(new ScopeMap(new ApplicationScopeExtractor(request.getSession().getServletContext()))))));
    }

    public MustacheRequest(DispatchRequest enclosedRequest, Scope scope) {
        super(enclosedRequest);
        this.pageScope = new MustacheScopeMap(scope);
    }

    MustacheRequest(DispatchRequest enclosedRequest, MustacheScopeMap pageScope) {
        super(enclosedRequest);
        this.pageScope = pageScope;
    }

    /**
     * Returns the page scope.
     *
     * @return The page scope.
     */
    public Map<String, Object> getPageScope() {
        return pageScope;
    }

    @Override
    public String[] getNativeScopes() {
        return SCOPES;
    }
}
