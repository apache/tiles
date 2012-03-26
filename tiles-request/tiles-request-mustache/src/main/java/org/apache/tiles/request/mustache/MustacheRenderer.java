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

package org.apache.tiles.request.mustache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sampullara.mustache.MustacheBuilder;
import com.sampullara.mustache.MustacheException;
import com.sampullara.mustache.Scope;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.CannotRenderException;
import org.apache.tiles.request.render.Renderer;

/**
 * The Mustache-specific renderer.
 *
 * @version $Rev: 1215006 $ $Date: 2011-12-16 01:30:41 +0100 (Fri, 16 Dec 2011) $
 * @since 3.0
 */
public final class MustacheRenderer implements Renderer {

    private Pattern acceptPattern;

    @Override
    public void render(String path, Request request) throws IOException {
        if (path == null) {
            throw new CannotRenderException("Cannot dispatch a null path");
        }

        try{
            new MustacheBuilder()
                    .build(new BufferedReader(new InputStreamReader(getResourceStream(request, path))), path)
                    .execute(request.getWriter(), buildScope(request));

        }catch(MustacheException ex){
            throw new IOException("failed to MustacheRenderer.render(" + path + ",request)", ex);
        }
    }

    private static InputStream getResourceStream(Request request, String path) throws IOException {
        final ApplicationContext applicationContext = request.getApplicationContext();
        final ApplicationResource resource = applicationContext.getResource(path);
        return resource.getInputStream();
    }

    private static Scope buildScope(Request request){
        Scope scope = null;
        List<String> availableScopes = request.getAvailableScopes();
        for(int i = availableScopes.size() -1; i >= 0; --i){
            scope = null == scope
                    ? new Scope(request.getContext(availableScopes.get(i)))
                    : new Scope(request.getContext(availableScopes.get(i)), scope);
        }
        return scope;
    }

    //@Override
    public boolean isRenderable(String path, Request request) {
        if (path == null) {
            return false;
        }
        if (acceptPattern != null) {
            final Matcher matcher = acceptPattern.matcher(path);
            return matcher.matches();
        }
        return true;
    }

    public void setAcceptPattern(Pattern acceptPattern) {
        this.acceptPattern = acceptPattern;
    }
}
