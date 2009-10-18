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

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link TilesVelocityRepository}.
 */
public class TilesVelocityRepositoryTest {

    /**
     * The repository to test.
     */
    private TilesVelocityRepository repository;

    /**
     * The servlet context (mocked).
     */
    private ServletContext servletContext;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        servletContext = createMock(ServletContext.class);

        replay(servletContext);
        repository = new TilesVelocityRepository(servletContext);
    }

    /**
     * Terminates the test.
     */
    @After
    public void tearDown() {
        verify(servletContext);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getAddAttribute()}.
     */
    @Test
    public void testGetAddAttribute() {
        assertNotNull(repository.getAddAttribute());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getAddListAttribute()}.
     */
    @Test
    public void testGetAddListAttribute() {
        assertNotNull(repository.getAddListAttribute());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getDefinition()}.
     */
    @Test
    public void testGetDefinition() {
        assertNotNull(repository.getDefinition());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getGetAsString()}.
     */
    @Test
    public void testGetGetAsString() {
        assertNotNull(repository.getGetAsString());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getImportAttribute()}.
     */
    @Test
    public void testGetImportAttribute() {
        assertNotNull(repository.getImportAttribute());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getInsertAttribute()}.
     */
    @Test
    public void testGetInsertAttribute() {
        assertNotNull(repository.getInsertAttribute());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getInsertDefinition()}.
     */
    @Test
    public void testGetInsertDefinition() {
        assertNotNull(repository.getInsertDefinition());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getInsertTemplate()}.
     */
    @Test
    public void testGetInsertTemplate() {
        assertNotNull(repository.getInsertTemplate());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getPutAttribute()}.
     */
    @Test
    public void testGetPutAttribute() {
        assertNotNull(repository.getPutAttribute());
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.TilesVelocityRepository#getPutListAttribute()}.
     */
    @Test
    public void testGetPutListAttribute() {
        assertNotNull(repository.getPutListAttribute());
    }
}
