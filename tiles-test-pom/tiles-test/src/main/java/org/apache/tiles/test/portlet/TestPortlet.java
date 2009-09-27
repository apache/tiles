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
package org.apache.tiles.test.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.portlet.context.PortletUtil;

/**
 * Test Portlet.
 *
 * @version $Rev$ $Date$
 */
public class TestPortlet extends GenericPortlet {

    /** {@inheritDoc} */
    @Override
    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        PortletSession portletSession = request.getPortletSession();
        String definition = (String) portletSession.getAttribute("definition");
        if (definition != null) {
            portletSession.removeAttribute("definition");
            TilesContainer container = PortletUtil.getCurrentContainer(request,
                    getPortletContext());
            if (container.isValidDefinition(definition, request, response,
                    getPortletContext())) {
                container.render(definition, request, response,
                        getPortletContext());
                addBackLink(response);
            } else {
                PortletRequestDispatcher dispatcher = getPortletContext()
                        .getRequestDispatcher(
                                "/WEB-INF/jsp/nosuchdefinition.jsp");
                dispatcher.forward(request, response);
                addBackLink(response);
            }
        } else {
            PortletRequestDispatcher dispatcher = getPortletContext()
                    .getRequestDispatcher("/WEB-INF/jsp/index.jsp");
            dispatcher.forward(request, response);
        }
    }


    /**
     * Puts the definition name in a session attribue.
     *
     * @param request The portlet request.
     * @param response The portlet response.
     */
    @ProcessAction(name = "showDefinition")
    public void showDefinition(ActionRequest request, ActionResponse response) {
        request.getPortletSession().setAttribute("definition",
                request.getParameter("definition"));
    }

    /**
     * Adds a link to the response to go back.
     *
     * @param response The portlet response.
     * @throws IOException If something goes wrong.
     */
    private void addBackLink(RenderResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.append("<a href=\"");
        PortletURL url = response.createRenderURL();
        writer.append(url.toString());
        writer.append("\"> Back to definition selection</a>");
    }
}
