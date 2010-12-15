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
package org.apache.tiles.mvel;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.tiles.mvel.ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ReadOnlyVariableResolver}.
 *
 * @version $Rev$ $Date$
 */
public class ReadOnlyVariableResolverTest {

    /**
     * The resolver to test.
     */
    private ReadOnlyVariableResolver resolver;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        resolver = createMockBuilder(ReadOnlyVariableResolver.class).withConstructor("name").createMock();
    }

    /**
     * Test method for {@link ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#getFlags()}.
     */
    @Test
    public void testGetFlags() {
        replay(resolver);
        assertEquals(0, resolver.getFlags());
        verify(resolver);
    }

    /**
     * Test method for {@link ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#getName()}.
     */
    @Test
    public void testGetName() {
        replay(resolver);
        assertEquals("name", resolver.getName());
        verify(resolver);
    }

    /**
     * Test method for {@link ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#setStaticType(java.lang.Class)}.
     */
    @Test
    public void testSetStaticType() {
        replay(resolver);
        resolver.setStaticType(Object.class);
        verify(resolver);
    }

    /**
     * Test method for {@link ReadOnlyVariableResolverFactory.ReadOnlyVariableResolver#setValue(java.lang.Object)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetValue() {
        replay(resolver);
        resolver.setValue("whatever");
        verify(resolver);
    }

}
