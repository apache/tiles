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
package com.anydoby.tiles2.velocity;

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.tiles.template.AttributeResolver;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.GetAsStringModel;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ImportSupport;

/**
 * 
 * @author SergeyZ
 * 
 */
public class Tiles2Tool extends ImportSupport {

    private Context velocityContext;
    
    private GetAsStringModel getAsStringModel;
    
    public Tiles2Tool() {
        AttributeResolver attributeResolver = new DefaultAttributeResolver();
        getAsStringModel = new GetAsStringModel(attributeResolver);
    }

    /**
     * Initializes this tool.
     *
     * @param context the current {@link Context}
     * @throws IllegalArgumentException if the param is not a {@link Context}
     */
    public void setVelocityContext(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("velocity context should not be null");
        }
        this.velocityContext = context;
    }
    
    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * 
     * @throws IOException If something goes wrong.
     */
    public void getAsString_start(Map<String, Object> params) throws IOException {
        getAsStringModel.start(ServletUtil.getComposeStack(request),
                ServletUtil.getCurrentContainer(request, application),
                VelocityUtil.toSimpleBoolean((Boolean) params.get("ignore"),
                        false), (String) params.get("preparer"),
                (String) params.get("role"), params.get("defaultValue"),
                (String) params.get("defaultValueRole"), (String) params
                        .get("defaultValueType"), (String) params.get("name"),
                (Attribute) params.get("value"), velocityContext, request,
                response);
    }
    
    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * 
     * @throws IOException If something goes wrong.
     */
    public void getAsString(Map<String, Object> params) throws IOException {
        TilesContainer container = ServletUtil.getCurrentContainer(request, application);
        getAsStringModel.execute(container, response.getWriter(), VelocityUtil
                .toSimpleBoolean((Boolean) params.get("ignore"), false),
                (String) params.get("preparer"), (String) params.get("role"),
                params.get("defaultValue"), (String) params
                        .get("defaultValueRole"), (String) params
                        .get("defaultValueType"), (String) params.get("name"),
                (Attribute) params.get("value"), velocityContext, request,
                response);
    }

    public Attribute getAttribute(String key) {
        TilesContainer container = ServletUtil.getCurrentContainer(request,
                application);
        AttributeContext attributeContext = container.getAttributeContext(
                velocityContext, request, response);
        Attribute attribute = attributeContext.getAttribute(key);
        return attribute;
    }
}
