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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tiles.request.Request;

/**
 * Provides a Publisher-Subscriber implementation around the provided renderer to delegate to.
 *
 * @version $Rev: 1035784 $ $Date: 2010-11-16 20:24:12 +0000 (Tue, 16 Nov 2010) $
 * @since 3.0.0
 */
public class PublisherRenderer implements Renderer {

    public interface RendererListener{
        /** Called before the delegate's render method is called. */
        void start(String template, Request request) throws IOException;
        /** Called after the delegate's render method is called. */
        void end(String template, Request request) throws IOException;
        /** If the delegate render method throws an IOException it is passed through this. */
        void handleIOException(IOException ex, Request request) throws IOException;
    }

    private final Renderer renderer;
    private final List<RendererListener> listeners = new ArrayList<RendererListener>();
    private final List<RendererListener> listenersReversed = new ArrayList<RendererListener>();

    public PublisherRenderer(Renderer renderer){
        this.renderer = renderer;
    }

    @Override
    public void render(String path, Request request) throws IOException {
        if (path == null) {
            throw new CannotRenderException("Cannot dispatch a null path");
        }
        try{
            for(RendererListener listener : listeners){
                listener.start(path, request);
            }
            renderer.render(path, request);
        }catch(IOException ex){
            handleIOException(ex, request);
        }finally{
            for(RendererListener wrapper : listenersReversed){
                wrapper.end(path, request);
            }
        }
    }

    @Override
    public boolean isRenderable(String path, Request request) {
        return renderer.isRenderable(path, request);
    }

    public void addListener(RendererListener listener){
        listeners.add(listener);
        listenersReversed.clear();
        listenersReversed.addAll(listeners);
        Collections.reverse(listenersReversed);
    }

    private void handleIOException(IOException exception, Request request) throws IOException{
        IOException ex = exception;
        boolean throwIt = listeners.isEmpty();
        for(RendererListener listener : listenersReversed){
            try{
                listener.handleIOException(ex, request);
                throwIt = false;
            }catch(IOException newEx){
                ex = newEx;
                throwIt = true;
            }
        }
        if(throwIt){
            throw ex;
        }
    }
}
