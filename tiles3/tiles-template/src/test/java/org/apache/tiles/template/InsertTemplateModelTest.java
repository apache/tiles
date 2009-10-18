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
     * #start(org.apache.tiles.TilesContainer, java.lang.Object[])}.
     */
    @Test
    public void testStart() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(container.startContext(requestItem)).andReturn(attributeContext);

        replay(container, attributeContext);
        model.start(container, requestItem);
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel
     * #end(TilesContainer, String, String, String, String, String, Object...)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        container.endContext(requestItem);
        attributeContext.setPreparer("myPreparer");
        attributeContext.setTemplateAttribute((Attribute) notNull());
        container.renderContext(requestItem);

        replay(container, attributeContext);
        model.end(container, "myTemplate", "myTemplateType",
                "myTemplateExpression", "myRole", "myPreparer",
                requestItem);
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel
     * #execute(TilesContainer, String, String, String, String, String, Object...)}.
     */
    @Test
    public void testExecute() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);

        expect(container.startContext(requestItem)).andReturn(attributeContext);
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        container.endContext(requestItem);
        attributeContext.setPreparer("myPreparer");
        attributeContext.setTemplateAttribute((Attribute) notNull());
        container.renderContext(requestItem);

        replay(container, attributeContext);
        model.execute(container, "myTemplate", "myTemplateType",
                "myTemplateExpression", "myRole",
                "myPreparer", requestItem);
        verify(container, attributeContext);
    }

}
