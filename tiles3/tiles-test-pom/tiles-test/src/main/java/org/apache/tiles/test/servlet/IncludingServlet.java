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
package org.apache.tiles.test.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sample servlet that includes a page specified by the <code>include</code>
 * init parameter.
 *
 * @version $Rev$ $Date$
 */
public class IncludingServlet extends HttpServlet {

    /**
     * Init parameter value, that indicates the path to include.
     */
    private String include;

    /**
     * Initializes the servlet, reading the <code>include</code> init
     * parameter.
     *
     * @param config The servlet configuration object to use.
     * @throws ServletException Thrown by
     * {@link HttpServlet#init(ServletConfig)}
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        include = config.getInitParameter("include");
    }

    /**
     * Processes the request, including the specified page.
     *
     * @param request The request object.
     * @param response The response object.
     * @throws ServletException Thrown by the {@link #include} method.
     * @throws IOException Thrown by the {@link #include} method.
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(include).include(request, response);
    }
}
