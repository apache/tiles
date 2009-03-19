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

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.apache.velocity.runtime.Renderable;

/**
 * The Tiles tool to be used in Velocity templates. Most of the methods can be used in two ways:
 * <ul>
 * <li>calling methods that accept a map of parameters: executes immediately the required model;</li>
 * <li>calling methods without parameters: useful to include composition code inside a block.
 * You need to call then {@link #start(Map)}, then your code in the block, and then {@link #end()}.</li>
 * </ul>
 * 
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class Tiles2Tool extends ContextHolder {

    /**
     * The key of the attribute that will be used to store the repository of "models".
     */
    private static final String TILES_VELOCITY_REPOSITORY_KEY = "org.apache.tiles.velocity.TilesVelocityRepository";
    
    /**
     * The current executable object to use. Set in {@link #start(Map)} and used in {@link #end()}.
     */
    private BodyExecutable currentExecutable;
    
    /**
     * The repository of Tiles+Velocity models.
     */
    private TilesVelocityRepository repository;
    
    /**
     * Executes the {@link AddAttributeVModel}.
     * 
     * @param params The map of parameters.
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.AddAttributeModel
     */
    public Tiles2Tool addAttribute(Map<String, Object> params) {
        execute(getRepository().getAddAttribute(), params);
        return this;
    }

    /**
     * Prepares the {@link AddAttributeVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.AddAttributeModel
     */
    public Tiles2Tool addAttribute() {
        currentExecutable = getRepository().getAddAttribute();
        return this;
    }
    
    /**
     * Prepares the {@link AddListAttributeVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.AddListAttributeModel
     */
    public Tiles2Tool addListAttribute() {
        currentExecutable = getRepository().getAddListAttribute();
        return this;
    }
    
    /**
     * Executes the {@link DefinitionVModel}.
     * 
     * @param params The map of parameters.
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.DefinitionModel
     */
    public Tiles2Tool definition(Map<String, Object> params) {
        execute(getRepository().getDefinition(), params);
        return this;
    }
    
    /**
     * Prepares the {@link DefinitionVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.DefinitionModel
     */
    public Tiles2Tool definition() {
        currentExecutable = getRepository().getDefinition();
        return this;
    }

    /**
     * Executes the {@link GetAsStringVModel}.
     * 
     * @param params The map of parameters.
     * @return A renderable object that renders an attribute as a string.
     * @since 2.2.0
     * @see org.apache.tiles.template.GetAsStringModel
     */
    public Renderable getAsString(Map<String, Object> params) {
        return execute(getRepository().getGetAsString(), params);
    }

    /**
     * Prepares the {@link GetAsStringVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.GetAsStringModel
     */
    public Tiles2Tool getAsString() {
        currentExecutable = getRepository().getGetAsString();
        return this;
    }
    
    /**
     * Executes the {@link ImportAttributeVModel}.
     * 
     * @param params The map of parameters.
     * @return A renderable object that does not write anything, but imports attribute values when invoked.
     * @since 2.2.0
     * @see org.apache.tiles.template.ImportAttributeModel
     */
    public Renderable importAttribute(Map<String, Object> params) {
        return execute(getRepository().getImportAttribute(), params);
    }
    
    /**
     * Executes the {@link InsertAttributeVModel}.
     * 
     * @param params The map of parameters.
     * @return A renderable object that renders an attribute.
     * @since 2.2.0
     * @see org.apache.tiles.template.InsertAttributeModel
     */
    public Renderable insertAttribute(Map<String, Object> params) {
        return execute(getRepository().getInsertAttribute(), params);
    }
    
    /**
     * Prepares the {@link InsertAttributeVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.InsertAttributeModel
     */
    public Tiles2Tool insertAttribute() {
        currentExecutable = getRepository().getInsertAttribute();
        return this;
    }
    
    /**
     * Executes the {@link InsertDefinitionVModel}.
     * 
     * @param params The map of parameters.
     * @return A renderable object that renders a definition.
     * @since 2.2.0
     * @see org.apache.tiles.template.InsertDefinitionModel
     */
    public Renderable insertDefinition(Map<String, Object> params) {
        return execute(getRepository().getInsertDefinition(), params);
    }
    
    /**
     * Prepares the {@link InsertDefinitionVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.InsertDefinitionModel
     */
    public Tiles2Tool insertDefinition() {
        currentExecutable = getRepository().getInsertDefinition();
        return this;
    }
    
    /**
     * Executes the {@link InsertTemplateVModel}.
     * 
     * @param params The map of parameters.
     * @return A renderable object that renders a template.
     * @since 2.2.0
     * @see org.apache.tiles.template.InsertTemplateModel
     */
    public Renderable insertTemplate(Map<String, Object> params) {
        return execute(getRepository().getInsertTemplate(), params);
    }
    
    /**
     * Prepares the {@link InsertTemplateVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.InsertTemplateModel
     */
    public Tiles2Tool insertTemplate() {
        currentExecutable = getRepository().getInsertTemplate();
        return this;
    }
    
    /**
     * Executes the {@link PutAttributeVModel}.
     * 
     * @param params The map of parameters.
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.PutAttributeModel
     */
    public Tiles2Tool putAttribute(Map<String, Object> params) {
        execute(getRepository().getPutAttribute(), params);
        return this;
    }
    
    /**
     * Prepares the {@link PutAttributeVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.PutAttributeModel
     */
    public Tiles2Tool putAttribute() {
        currentExecutable = getRepository().getPutAttribute();
        return this;
    }
    
    /**
     * Prepares the {@link PutListAttributeVModel} for the execution with a block
     * inside {@link #start(Map)} and {@link #end()} calls.
     * 
     * @return The tool itself.
     * @since 2.2.0
     * @see org.apache.tiles.template.PutListAttributeModel
     */
    public Tiles2Tool putListAttribute() {
        currentExecutable = getRepository().getPutListAttribute();
        return this;
    }
    
    /**
     * Sets the current container for the current request.
     * 
     * @param containerKey The key of the container to set as "current" for the current request.
     * @return The tool itself.
     * @since 2.2.0
     */
    public Tiles2Tool setCurrentContainer(String containerKey) {
        ServletUtil.setCurrentContainer(getRequest(), getServletContext(),
                containerKey);
        return this;
    }
    
    /**
     * Starts a "model" for the execution in a block.
     * 
     * @param params The map of parameters.
     * @return The tool itself.
     * @since 2.2.0
     */
    public Tiles2Tool start(Map<String, Object> params) {
        if (currentExecutable == null) {
            throw new NullPointerException("The current model to start has not been set");
        }
        currentExecutable.start(getRequest(), getResponse(), getVelocityContext(), params);
        return this;
    }

    /**
     * Ends a "model" after the execution of a block.
     * 
     * @return A renderable object. It can render actually something, or execute
     * code needed to the execution of parent models.
     * @since 2.2.0
     */
    public Renderable end() {
        if (currentExecutable == null) {
            throw new NullPointerException("The current model to start has not been set");
        }
        Renderable retValue = currentExecutable.end(getRequest(),
                getResponse(), getVelocityContext());
        currentExecutable = null;
        return retValue;
    }

    /**
     * Returns an attribute.
     * 
     * @param key The name of the attribute to get.
     * @return The Attribute.
     * @since 2.2.0
     */
    public Attribute getAttribute(String key) {
        TilesContainer container = ServletUtil.getCurrentContainer(
                getRequest(), getServletContext());
        AttributeContext attributeContext = container.getAttributeContext(
                getVelocityContext(), getRequest(), getResponse());
        Attribute attribute = attributeContext.getAttribute(key);
        return attribute;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "";
    }

    /**
     * Gets or creates the Tiles+Velocity model repository from the servlet context.
     * 
     * @return The model repository.
     */
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
    
    /**
     * Executes an "executable" model.
     * 
     * @param executable The object to execute.
     * @param params The parameters map.
     * @return A renderable object. It can render actually something, or execute
     * code needed to the execution of parent models.
     */
    private Renderable execute(Executable executable, Map<String, Object> params) {
        return executable.execute(getRequest(), getResponse(), getVelocityContext(), params);
    }
}
