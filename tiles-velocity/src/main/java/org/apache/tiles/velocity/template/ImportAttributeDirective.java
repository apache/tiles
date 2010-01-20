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

import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.tiles.velocity.context.VelocityUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.tools.view.ViewContext;

/**
 * Wraps {@link ImportAttributeModel} to be used in Velocity. For the list of
 * parameters, see
 * {@link ImportAttributeModel#getImportedAttributes(org.apache.tiles.TilesContainer,
 * String, String, boolean, Object...)}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.2
 */
public class ImportAttributeDirective extends Directive {

    /**
     * The template model.
     */
    private ImportAttributeModel model = new ImportAttributeModel();

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "tiles_importAttribute";
    }

    /** {@inheritDoc} */
    @Override
    public int getType() {
        return LINE;
    }

    /** {@inheritDoc} */
    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) {
        ViewContext viewContext = (ViewContext) context
                .getInternalUserContext();
        Map<String, Object> params = VelocityUtil.getParameters(context, node);
        HttpServletRequest request = viewContext.getRequest();
        HttpServletResponse response = viewContext.getResponse();
        ServletContext servletContext = viewContext.getServletContext();
        Map<String, Object> attributes = model.getImportedAttributes(
                ServletUtil
                        .getCurrentContainer(request, servletContext),
                (String) params.get("name"), (String) params
                        .get("toName"), VelocityUtil.toSimpleBoolean(
                        (Boolean) params.get("ignore"), false),
                context, request, response);
        String scope = (String) params.get("scope");
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            VelocityUtil.setAttribute(context, request,
                    servletContext, entry.getKey(), entry.getValue(),
                    scope);
        }
        return true;
    }

}
