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
/**
 * This package defines the concept of "request" as the event causing a 
 * document to be generated from a template. The process is also called
 * "rendering the template". Typical examples are servlet requests or 
 * portlet requests. 
 * 
 * This API is independent of the underlying technology, allowing the user 
 * to deal with similarities in servlets and portlets, or various template 
 * technologies, in a uniform way.
 * 
 * It is based on 3 main interfaces:
 * <ul>
 * <li>{@link org.apache.tiles.request.Request} is the main abstraction, 
 * encapsulating the parameters of the template (as attributes and scopes)
 * and the target document (as java.io.OutputStream). 
 * {@link org.apache.tiles.request.DispatchRequest} holds some features common
 * to servlets and portlets that are unlikely to be found outside of a JavaEE
 * web environment.
 * <li>{@link org.apache.tiles.ApplicationContext} can be used to access
 * application-wide configuration and resources (typically the files containing
 * the templates).
 * <li>{@link org.apache.tiles.request.render.Renderer} is the interface supported
 * by the engine in charge of rendering the template.
 * <ul>
 */
package org.apache.tiles.request;
