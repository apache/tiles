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

package org.apache.tiles.velocity.template;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.Renderable;

/**
 * It represents an object that can be executed under a Velocity+Servlet context.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 * @deprecated Use Velocity directives.
 */
@Deprecated
public interface Executable {

    /**
     * Executes the command.
     *
     * @param request The HTTP request.
     * @param response The HTTP response-
     * @param velocityContext The Velocity context.
     * @param params The map of the parameters.
     * @return A renderable object. It does not necessary render anything.
     * @since 2.2.0
     */
    Renderable execute(HttpServletRequest request,
            HttpServletResponse response, Context velocityContext,
            Map<String, Object> params);
}
