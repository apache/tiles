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

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.velocity.runtime.Renderable;

/**
 * 
 * @author SergeyZ
 * 
 */
public class Tiles2Tool extends ContextHolder {

    private static final String TILES_VELOCITY_REPOSITORY_KEY = "org.apache.tiles.velocity.TilesVelocityRepository";
    
    private BodyExecutable currentExecutable;
    
    private TilesVelocityRepository repository;
    
    public Tiles2Tool() {
        System.out.println("Hello");
    }
    
    public Tiles2Tool addAttribute(Map<String, Object> params) {
        execute(getRepository().getAddAttribute(), params);
        return this;
    }
    
    public Tiles2Tool addAttribute() {
        currentExecutable = getRepository().getAddAttribute();
        return this;
    }
    
    public Tiles2Tool addListAttribute() {
        currentExecutable = getRepository().getAddListAttribute();
        return this;
    }
    
    public Tiles2Tool definition(Map<String, Object> params) {
        execute(getRepository().getDefinition(), params);
        return this;
    }
    
    public Tiles2Tool definition() {
        currentExecutable = getRepository().getDefinition();
        return this;
    }

    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * 
     * @throws IOException If something goes wrong.
     */
    public Renderable getAsString(Map<String, Object> params)
            throws IOException {
        return execute(getRepository().getGetAsString(), params);
    }

    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * 
     * @throws IOException If something goes wrong.
     */
    public Tiles2Tool getAsString() {
        currentExecutable = getRepository().getGetAsString();
        return this;
    }
    
    public Renderable importAttribute(Map<String, Object> params) {
        return execute(getRepository().getImportAttribute(), params);
    }
    
    public Renderable insertAttribute(Map<String, Object> params) {
        return execute(getRepository().getInsertAttribute(), params);
    }
    
    public Tiles2Tool insertAttribute() {
        currentExecutable = getRepository().getInsertAttribute();
        return this;
    }
    
    public Renderable insertDefinition(Map<String, Object> params) {
        return execute(getRepository().getInsertDefinition(), params);
    }
    
    public Tiles2Tool insertDefinition() {
        currentExecutable = getRepository().getInsertDefinition();
        return this;
    }
    
    public Renderable insertTemplate(Map<String, Object> params) {
        return execute(getRepository().getInsertTemplate(), params);
    }
    
    public Tiles2Tool insertTemplate() {
        currentExecutable = getRepository().getInsertTemplate();
        return this;
    }
    
    public Tiles2Tool putAttribute(Map<String, Object> params) {
        execute(getRepository().getPutAttribute(), params);
        return this;
    }
    
    public Tiles2Tool putAttribute() {
        currentExecutable = getRepository().getPutAttribute();
        return this;
    }
    
    public Tiles2Tool putListAttribute() {
        currentExecutable = getRepository().getPutListAttribute();
        return this;
    }
    
    public Tiles2Tool setCurrentContainer(String containerKey) {
        ServletUtil.setCurrentContainer(getRequest(), getServletContext(),
                containerKey);
        return this;
    }
    
    public Tiles2Tool start(Map<String, Object> params) {
        if (currentExecutable == null) {
            throw new NullPointerException("The current model to start has not been set");
        }
        currentExecutable.start(getRequest(), getResponse(), getVelocityContext(), params);
        return this;
    }
    
    public Renderable end() {
        if (currentExecutable == null) {
            throw new NullPointerException("The current model to start has not been set");
        }
        Renderable retValue = currentExecutable.end(getRequest(),
                getResponse(), getVelocityContext());
        currentExecutable = null;
        return retValue;
    }

    public Attribute getAttribute(String key) {
        TilesContainer container = ServletUtil.getCurrentContainer(
                getRequest(), getServletContext());
        AttributeContext attributeContext = container.getAttributeContext(
                getVelocityContext(), getRequest(), getResponse());
        Attribute attribute = attributeContext.getAttribute(key);
        return attribute;
    }

    @Override
    public String toString() {
        return "";
    }

    private TilesVelocityRepository getRepository() {
        if (repository != null) {
            return repository;
        }

        repository = (TilesVelocityRepository) getServletContext()
                .getAttribute(TILES_VELOCITY_REPOSITORY_KEY);
        if (repository == null) {
            repository = new TilesVelocityRepository(getServletContext());
            getServletContext().setAttribute(TILES_VELOCITY_REPOSITORY_KEY,
                    repository);
        }
        return repository;
    }
    
    private Renderable execute(Executable executable, Map<String, Object> params) {
        return executable.execute(getRequest(), getResponse(), getVelocityContext(), params);
    }
}
