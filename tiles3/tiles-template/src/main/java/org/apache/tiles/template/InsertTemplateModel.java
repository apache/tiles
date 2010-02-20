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

package org.apache.tiles.template;

import java.io.IOException;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.Request;
import org.apache.tiles.template.body.ModelBody;

/**
 * <p>
 * <strong>Insert a template.</strong>
 * </p>
 * <p>
 * Insert a template with the possibility to pass parameters (called
 * attributes). A template can be seen as a procedure that can take parameters
 * or attributes. <code>&lt;tiles:insertTemplate&gt;</code> allows to define
 * these attributes and pass them to the inserted jsp page, called template.
 * Attributes are defined using nested tag
 * <code>&lt;tiles:putAttribute&gt;</code> or
 * <code>&lt;tiles:putListAttribute&gt;</code>.
 * </p>
 * <p>
 * You must specify <code>template</code> attribute, for inserting a template
 * </p>
 *
 * <p>
 * <strong>Example : </strong>
 * </p>
 *
 * <pre>
 * &lt;code&gt;
 *           &lt;tiles:insertTemplate template=&quot;/basic/myLayout.jsp&quot; flush=&quot;true&quot;&gt;
 *              &lt;tiles:putAttribute name=&quot;title&quot; value=&quot;My first page&quot; /&gt;
 *              &lt;tiles:putAttribute name=&quot;header&quot; value=&quot;/common/header.jsp&quot; /&gt;
 *              &lt;tiles:putAttribute name=&quot;footer&quot; value=&quot;/common/footer.jsp&quot; /&gt;
 *              &lt;tiles:putAttribute name=&quot;menu&quot; value=&quot;/basic/menu.jsp&quot; /&gt;
 *              &lt;tiles:putAttribute name=&quot;body&quot; value=&quot;/basic/helloBody.jsp&quot; /&gt;
 *           &lt;/tiles:insertTemplate&gt;
 *         &lt;/code&gt;
 * </pre>
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class InsertTemplateModel {

    /**
     * Starts the operation.
     * @param request TODO
     * @param container The Tiles container.
     *
     * @since 2.2.0
     */
    public void start(Request request) {
        TilesContainer container = TilesAccess.getCurrentContainer(request);
        container.startContext(request);
    }

    /**
     * Ends the operation.
     * @param template The template to render.
     * @param templateType The type of the template attribute.
     * @param templateExpression The expression to evaluate to get the value of the template.
     * @param role A comma-separated list of roles. If present, the template
     * will be rendered only if the current user belongs to one of the roles.
     * @param preparer The preparer to use to invoke before the definition is
     * rendered. If specified, it overrides the preparer specified in the
     * definition itself.
     * @param request TODO
     * @param container The Tiles container.
     *
     * @since 2.2.0
     */
    public void end(String template, String templateType, String templateExpression,
            String role, String preparer, Request request) {
        TilesContainer container = TilesAccess.getCurrentContainer(request);
        renderTemplate(container, template, templateType, templateExpression,
                role, preparer, request);
    }

    /**
     * Executes the operation.
     * @param template The template to render.
     * @param templateType The type of the template attribute.
     * @param templateExpression The expression to evaluate to get the value of the template.
     * @param role A comma-separated list of roles. If present, the template
     * will be rendered only if the current user belongs to one of the roles.
     * @param preparer The preparer to use to invoke before the definition is
     * rendered. If specified, it overrides the preparer specified in the
     * definition itself.
     * @param request TODO
     * @param modelBody TODO
     * @param container The Tiles container.
     * @throws IOException If something goes wrong.
     * @since 2.2.0
     */
    public void execute(String template, String templateType, String templateExpression,
            String role, String preparer, Request request, ModelBody modelBody) throws IOException {
        TilesContainer container = TilesAccess.getCurrentContainer(request);
        container.startContext(request);
        modelBody.evaluateWithoutWriting();
        container = TilesAccess.getCurrentContainer(request);
        renderTemplate(container, template, templateType, templateExpression,
                role, preparer, request);
    }

    private void renderTemplate(TilesContainer container, String template,
            String templateType, String templateExpression, String role,
            String preparer, Request request) {
        try {
            AttributeContext attributeContext = container
                    .getAttributeContext(request);
            Attribute templateAttribute = Attribute.createTemplateAttribute(template,
                    templateExpression, templateType, role);
            attributeContext.setPreparer(preparer);
            attributeContext.setTemplateAttribute(templateAttribute);
            container.renderContext(request);
        } finally {
            container.endContext(request);
        }
    }
}
