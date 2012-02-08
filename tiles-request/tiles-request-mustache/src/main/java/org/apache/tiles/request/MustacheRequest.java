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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

    /**
     * The page scope map.
     */
    private final Scope pageScope;
    private Map<String, Object> pageScopeMap = null;

    /**
     * Creates a new Mustache request.
     *
     * @param applicationContext The application context.
     * @param request The request.
     * @param response The response.
     * @return A new request.
     */
    public static MustacheRequest createMustacheRequest(
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

    /**
     * Constructor.
     *
     * @param enclosedRequest
     *            The request that exposes non-Mustache specific properties
     * @param pageScope
     *            The page scope.
     */
    public MustacheRequest(DispatchRequest enclosedRequest, Scope pageScope) {
        super(enclosedRequest);
        this.pageScope = pageScope;
    }

    /**
     * Returns the page scope.
     *
     * @return The page scope.
     */
    public Map<String, Object> getPageScope() {
        if(null == pageScopeMap){
            Map<String,Object> map = new HashMap<String,Object>();
            for(Entry<Object,Object> entry : pageScope.entrySet()){
                map.put(entry.getKey().toString(), entry.getValue());
            }
            pageScopeMap = Collections.unmodifiableMap(map);
        }
        return pageScopeMap;
    }

    @Override
    public String[] getNativeScopes() {
        return SCOPES;
    }
}
