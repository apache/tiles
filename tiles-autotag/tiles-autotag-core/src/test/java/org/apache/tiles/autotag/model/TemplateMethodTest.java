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
package org.apache.tiles.autotag.model;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * Tests {@link TemplateMethod}.
 *
 * @version $Rev$ $Date$
 */
public class TemplateMethodTest {

    /**
     * Tests {@link TemplateMethod#TemplateMethod(String, Iterable)}.
     */
    @Test
    public void testTemplateMethod() {
        TemplateParameter param1 = createMock(TemplateParameter.class);
        TemplateParameter param2 = createMock(TemplateParameter.class);

        expect(param1.getName()).andReturn("param1");
        expect(param2.getName()).andReturn("param2");

        replay(param1, param2);
        List<TemplateParameter> parameters = new ArrayList<TemplateParameter>();
        parameters.add(param1);
        parameters.add(param2);

        TemplateMethod method = new TemplateMethod("method", parameters);
        assertEquals("method", method.getName());
        Iterator<TemplateParameter> params = method.getParameters().iterator();
        assertSame(param1, params.next());
        assertSame(param2, params.next());
        assertFalse(params.hasNext());
        assertSame(param1, method.getParameterByName("param1"));
        assertSame(param2, method.getParameterByName("param2"));
        verify(param1, param2);
    }

    /**
     * Tests {@link TemplateMethod#setDocumentation(String)}.
     */
    @Test
    public void testSetDocumentation() {
        TemplateMethod method = new TemplateMethod("method", new ArrayList<TemplateParameter>());
        method.setDocumentation("docs");
        assertEquals("docs", method.getDocumentation());
    }

    /**
     * Tests {@link TemplateMethod#hasBody()}.
     */
    @Test
    public void testHasBody() {
        TemplateParameter param1 = createMock(TemplateParameter.class);
        TemplateParameter param2 = createMock(TemplateParameter.class);

        expect(param1.getName()).andReturn("param1");
        expect(param2.getName()).andReturn("param2");
        expect(param1.isBody()).andReturn(true);

        replay(param1, param2);
        List<TemplateParameter> parameters = new ArrayList<TemplateParameter>();
        parameters.add(param1);
        parameters.add(param2);

        TemplateMethod method = new TemplateMethod("method", parameters);
        assertTrue(method.hasBody());
        verify(param1, param2);
    }

    /**
     * Tests {@link TemplateMethod#hasBody()}.
     */
    @Test
    public void testHasBody2() {
        TemplateParameter param1 = createMock(TemplateParameter.class);
        TemplateParameter param2 = createMock(TemplateParameter.class);

        expect(param1.getName()).andReturn("param1");
        expect(param2.getName()).andReturn("param2");
        expect(param1.isBody()).andReturn(false);
        expect(param2.isBody()).andReturn(false);

        replay(param1, param2);
        List<TemplateParameter> parameters = new ArrayList<TemplateParameter>();
        parameters.add(param1);
        parameters.add(param2);

        TemplateMethod method = new TemplateMethod("method", parameters);
        assertFalse(method.hasBody());
        verify(param1, param2);
    }

    /**
     * Tests {@link TemplateMethod#toString()}.
     */
    @Test
    public void testToString() {
        TemplateMethod method = new TemplateMethod("method", new ArrayList<TemplateParameter>());
        assertEquals("TemplateMethod [name=method, documentation=null, parameters={}]", method.toString());
    }

}
