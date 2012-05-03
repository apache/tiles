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
package org.apache.tiles.request.freemarker.autotag;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.Writer;
import java.util.HashMap;

import org.apache.tiles.request.freemarker.autotag.FreemarkerAutotagException;
import org.apache.tiles.request.freemarker.autotag.FreemarkerUtil;
import org.junit.Test;

import freemarker.core.Environment;
import freemarker.core.Macro;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

/**
 * Tests {@link FreemarkerUtil}.
 *
 * @version $Rev$ $Date$
 */
public class FreemarkerUtilTest {

    /**
     * Test method for {@link FreemarkerUtil#getAsObject(TemplateModel, Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetAsObject() throws TemplateModelException {
        TemplateNumberModel model = createMock(TemplateNumberModel.class);
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);

        expect(model.getAsNumber()).andReturn(new Integer(42));
        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());

        replay(template, rootDataModel, out);
        new Environment(template, rootDataModel, out);

        replay(model);
        assertEquals(new Integer(42), FreemarkerUtil.getAsObject(model, new Integer(1)));
        verify(template, rootDataModel, out, model);
    }

    /**
     * Test method for {@link FreemarkerUtil#getAsObject(TemplateModel, Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test
    public void testGetAsObjectDefault() throws TemplateModelException {
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);

        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());

        replay(template, rootDataModel, out);
        new Environment(template, rootDataModel, out);

        assertEquals(new Integer(1), FreemarkerUtil.getAsObject(null, new Integer(1)));
        verify(template, rootDataModel, out);
    }

    /**
     * Test method for {@link FreemarkerUtil#getAsObject(TemplateModel, Object)}.
     * @throws TemplateModelException If something goes wrong.
     */
    @Test(expected = FreemarkerAutotagException.class)
    public void testGetAsObjectException() throws TemplateModelException {
        TemplateNumberModel model = createMock(TemplateNumberModel.class);
        Template template = createMock(Template.class);
        TemplateHashModel rootDataModel = createMock(TemplateHashModel.class);
        Writer out = createMock(Writer.class);

        expect(model.getAsNumber()).andThrow(new TemplateModelException());
        expect(template.getMacros()).andReturn(new HashMap<String, Macro>());

        replay(template, rootDataModel, out);
        new Environment(template, rootDataModel, out);

        replay(model);
        try {
            assertEquals(new Integer(42), FreemarkerUtil.getAsObject(model, new Integer(1)));
        } finally {
            verify(template, rootDataModel, out, model);
        }
    }

}
