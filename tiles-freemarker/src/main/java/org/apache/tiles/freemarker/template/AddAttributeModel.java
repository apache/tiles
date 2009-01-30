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

package org.apache.tiles.freemarker.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.freemarker.context.FreeMarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <p><strong>Adds an attribute in enclosing attribute container tag.</strong></p>
 * <p>Enclosing attribute container tag can be :
 * <ul>
 * <li>&lt;putListAttribute&gt;</li>
 * <li>&lt;putAttribute&gt;</li>
 * </ul>
 * (or any other tag which implements the <code>{@link AddAttributeModelParent}</code> interface.
 * Exception is thrown if no appropriate tag can be found.</p>
 * <p>Put tag can have following atributes :
 * <ul>
 * <li>name : Name of the attribute</li>
 * <li>value : value to put as attribute</li>
 * <li>type : value type. Only valid if value is a String and is set by
 * value="something" or by a bean.
 * Possible type are : string (value is used as direct string),
 * template (value is used as a page url to insert),
 * definition (value is used as a definition name to insert)</li>
 * <li>role : Role to check when 'insert' will be called. If enclosing tag is
 * &lt;insert&gt;, role is checked immediately. If enclosing tag is
 * &lt;definition&gt;, role will be checked when this definition will be
 * inserted.</li>
 * </ul></p>
 * <p>Value can also come from tag body. Tag body is taken into account only if
 * value is not set by one of the tag attributes. In this case Attribute type is
 * "string", unless tag body define another type.</p>
 *
 * @version $Rev$ $Date$
 */
public class AddAttributeModel extends NestableTemplateDirectiveModel implements DefinitionModelParent {

    protected Map<String, TemplateModel> currentParams;
    
    protected Object value;
    
    private String type;
    
    /**
     * Returns the role to check. If the user is in the specified role, the tag is
     * taken into account; otherwise, the tag is ignored (skipped).
     *
     * @return The role to check.
     */
    public String getRole() {
        return FreeMarkerUtil.getAsString(currentParams.get("role"));
    }

    /**
     * Returns the attribute value.
     *
     * @return Attribute value. Can be a String or Object.
     */
    public Object getValue() {
        return value;
    }

    /**
     * <p>
     * Returns content type: string, template or definition.
     * </p>
     * <ul>
     * <li>String : Content is printed directly.</li>
     * <li>template : Content is included from specified URL. Value is used as
     * an URL.</li>
     * <li>definition : Value denote a definition defined in factory (xml
     * file). Definition will be searched in the inserted tile, in a
     * <code>&lt;insert attribute="attributeName"&gt;</code> tag, where
     * 'attributeName' is the name used for this tag.</li>
     * </ul>
     *
     * @return The attribute type.
     */
    public String getType() {
        return type;
    }
    
    @Override
    protected void evaluateBody(Environment env,
            Map<String, TemplateModel> params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        if (value == null && body != null) {
            StringWriter writer = new StringWriter();
            try {
                body.render(writer);
                writer.close();
            } catch (TemplateException e) {
                throw new FreeMarkerTilesException(
                        "Exception during rendition of the body", e);
            } catch (IOException e) {
                throw new FreeMarkerTilesException(
                        "I/O Exception during rendition of the body", e);
            }
            value = writer.toString();
            type = "string";
        }
    }

    @Override
    protected void doEnd(Environment env, Map<String, TemplateModel> params,
            TemplateModel[] loopVars, TemplateDirectiveBody body) {
        if (isAccessAllowed(env)) {
            execute(env);
        }
    }

    @Override
    protected void doStart(Environment env, Map<String, TemplateModel> params,
            TemplateModel[] loopVars, TemplateDirectiveBody body) {
        currentParams = params;
        value = FreeMarkerUtil.getAsObject(params.get("value"));
        type = FreeMarkerUtil.getAsString(currentParams.get("type"));
    }

    /** {@inheritDoc} */
    public void processNestedDefinitionName(String definitionName) {
        value = definitionName;
        if (type == null) {
            type = "definition";
        }
    }

    /**
     * Executes the processing of this tag, calling its parent tag.
     *
     * @throws TilesJspException If something goes wrong during execution.
     */
    protected void execute(Environment env) {
        AddAttributeModelParent parent = (AddAttributeModelParent) findAncestorWithClass(
                env, AddAttributeModelParent.class);
        if (parent == null) {
            throw new FreeMarkerTilesException("Error: cannot find an AddAttributeModelParent ancestor'");
        }

        parent.processNestedModel(this);
    }

    /**
     * Checks if the user is inside the specified role.
     *
     * @return <code>true</code> if the user is allowed to have the tag
     * rendered.
     */
    protected boolean isAccessAllowed(Environment env) {
        HttpServletRequest req = FreeMarkerUtil.getRequestHashModel(env)
                .getRequest();
        String role = getRole();
        return (role == null || req.isUserInRole(role));
    }
}
