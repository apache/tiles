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

import static org.easymock.EasyMock.*;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link InsertTemplateModel}.
 *
 * @version $Rev$ $Date$
 */
public class InsertTemplateModelTest {

    /**
     * The model to test.
     */
    private InsertTemplateModel model;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        model = new InsertTemplateModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel
     * #start(org.apache.tiles.TilesContainer, Request)}.
     */
    @Test
    public void testStart() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(container.startContext(request)).andReturn(attributeContext);

        replay(container, attributeContext, request);
        model.start(container, request);
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel
     * #end(TilesContainer, String, String, String, String, String, Request)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        container.endContext(request);
        attributeContext.setPreparer("myPreparer");
        attributeContext.setTemplateAttribute((Attribute) notNull());
        container.renderContext(request);

        replay(container, attributeContext, request);
        model.end(container, "myTemplate", "myTemplateType",
                "myTemplateExpression", "myRole", "myPreparer",
                request);
        verify(container, attributeContext, request);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel
     * #execute(TilesContainer, String, String, String, String, String, Request)}.
     */
    @Test
    public void testExecute() {
        TilesContainer container = createMock(TilesContainer.class);
        Request request = createMock(Request.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(container.startContext(request)).andReturn(attributeContext);
        expect(container.getAttributeContext(request)).andReturn(attributeContext);
        container.endContext(request);
        attributeContext.setPreparer("myPreparer");
        attributeContext.setTemplateAttribute((Attribute) notNull());
        container.renderContext(request);

        replay(container, attributeContext, request);
        model.execute(container, "myTemplate", "myTemplateType",
                "myTemplateExpression", "myRole",
                "myPreparer", request);
        verify(container, attributeContext, request);
    }

}
