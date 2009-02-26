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
package org.apache.tiles.velocity;

import java.io.IOException;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;

/**
 * 
 * @author SergeyZ
 * 
 */
public class Tiles2Tool extends ContextHolder {

    private static final String TILES_VELOCITY_REPOSITORY_KEY = "org.apache.tiles.velocity.TilesVelocityRepository";
    
    private BodyExecutable currentBodyExecutable;
    
    private TilesVelocityRepository repository;

    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * 
     * @throws IOException If something goes wrong.
     */
    public Tiles2Tool getAsString(Map<String, Object> params)
            throws IOException {
        execute(getRepository().getGetAsString(), params);
        return this;
    }

    /**
     * Returns a string representation of attribute value. If the attribute is
     * <code>null</code> or if the attribute value is <code>null</code>,
     * <code>null</code> is returned
     * 
     * @throws IOException If something goes wrong.
     */
    public Tiles2Tool getAsString() throws IOException {
        currentBodyExecutable = getRepository().getGetAsString();
        return this;
    }
    
    public Tiles2Tool start(Map<String, Object> params) {
        if (currentBodyExecutable == null) {
            throw new NullPointerException("The current model to start has not been set");
        }
        currentBodyExecutable.start(getRequest(), getResponse(), getVelocityContext(), params);
        return this;
    }
    
    public Tiles2Tool end() {
        if (currentBodyExecutable == null) {
            throw new NullPointerException("The current model to start has not been set");
        }
        currentBodyExecutable.end(getRequest(), getResponse(), getVelocityContext());
        return this;
    }

    public Attribute getAttribute(String key) {
        TilesContainer container = ServletUtil.getCurrentContainer(
                getRequest(), getServletContext());
        AttributeContext attributeContext = container.getAttributeContext(
                getVelocityContext(), getRequest(), getResponse());
        Attribute attribute = attributeContext.getAttribute(key);
        return attribute;
    }
    
    public Tiles2Tool setCurrentContainer(String containerKey) {
        ServletUtil.setCurrentContainer(getRequest(), getServletContext(),
                containerKey);
        return this;
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
    
    private void execute(Executable executable, Map<String, Object> params) {
        executable.execute(getRequest(), getResponse(), getVelocityContext(), params);
    }
}
