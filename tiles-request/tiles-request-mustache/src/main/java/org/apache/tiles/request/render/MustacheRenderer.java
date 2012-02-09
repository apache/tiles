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

package org.apache.tiles.request.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.sampullara.mustache.MustacheBuilder;
import com.sampullara.mustache.MustacheException;
import org.apache.tiles.request.Request;

/**
 * The Mustache-specific renderer.
 *
 * @version $Rev: 1215006 $ $Date: 2011-12-16 01:30:41 +0100 (Fri, 16 Dec 2011) $
 * @since 3.0
 */
public final class MustacheRenderer implements Renderer {

    private final ResourceLoader loader;
    private FileFilter fileFilter;

    public MustacheRenderer(ResourceLoader loader){
        this.loader = loader;
    }

    @Override
    public void render(String path, Request request) throws IOException {
        if (path == null) {
            throw new CannotRenderException("Cannot dispatch a null path");
        }

        try{
            new MustacheBuilder()
                    .build(new BufferedReader(new InputStreamReader(loader.getResourceAsStream(path))), path)
                    .execute(request.getWriter(), request.getContext("page"));

        }catch(MustacheException ex){
            throw new IOException("failed to MustacheRenderer.render(" + path + ",request)", ex);
        }
    }

    //@Override
    public boolean isRenderable(String path, Request request) {
        return path != null && (fileFilter == null || fileFilter.accept(new File(path)));
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public interface ResourceLoader{
        InputStream getResourceAsStream(String path);
    }
}