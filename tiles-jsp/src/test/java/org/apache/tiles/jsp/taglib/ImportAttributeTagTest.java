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

package org.apache.tiles.jsp.taglib;

import static org.easymock.classextension.EasyMock.*;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.servlet.context.ServletUtil;
import org.junit.Test;

/**
 * Tests {@link ImportAttributeTag}.
 *
 * @version $Rev$ $Date$
 */
public class ImportAttributeTagTest {

    /**
     * Tests {@link ImportAttributeTag} with name specified and ignore false.
     *
     * @throws TilesJspException If something goes wrong.
     */
    @Test(expected = TilesJspException.class)
    public void testTagNullEvaluateException() throws TilesJspException {
        PageContext pageContext = createMock(PageContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute("myValue");
        expect(pageContext.getAttribute(
                ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE)).andReturn(container);
        expect(container.getAttributeContext(pageContext)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("attributeName")).andReturn(attribute);
        expect(container.evaluate(attribute, pageContext)).andReturn(null);

        replay(pageContext, container, attributeContext);
        ImportAttributeTag tag = new ImportAttributeTag();
        tag.setPageContext(pageContext);
        tag.setName("attributeName");
        tag.doStartTag();
        verify(pageContext, container, attributeContext);
    }

    /**
     * Tests {@link ImportAttributeTag} with name specified and ignore true.
     *
     * @throws TilesJspException If something goes wrong.
     */
    @Test
    public void testTagNullEvaluateIgnoreException() throws TilesJspException {
        PageContext pageContext = createMock(PageContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute("myValue");
        expect(pageContext.getAttribute(
                ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE)).andReturn(container);
        expect(container.getAttributeContext(pageContext)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("attributeName")).andReturn(attribute);
        expect(container.evaluate(attribute, pageContext)).andReturn(null);

        replay(pageContext, container, attributeContext);
        ImportAttributeTag tag = new ImportAttributeTag();
        tag.setPageContext(pageContext);
        tag.setName("attributeName");
        tag.setIgnore(true);
        tag.doStartTag();
        verify(pageContext, container, attributeContext);
    }


    /**
     * Tests {@link ImportAttributeTag} with name not specified and ignore false.
     *
     * @throws TilesJspException If something goes wrong.
     */
    @Test(expected = TilesJspException.class)
    public void testTagNullEvaluateAllException() throws TilesJspException {
        PageContext pageContext = createMock(PageContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute("myValue");
        expect(pageContext.getAttribute(
                ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE)).andReturn(container);
        expect(container.getAttributeContext(pageContext)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("attributeName")).andReturn(attribute);
        expect(container.evaluate(attribute, pageContext)).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        Set<String> names = new HashSet<String>();
        names.add("attributeName");
        expect(attributeContext.getLocalAttributeNames()).andReturn(names);

        replay(pageContext, container, attributeContext);
        ImportAttributeTag tag = new ImportAttributeTag();
        tag.setPageContext(pageContext);
        tag.doStartTag();
        verify(pageContext, container, attributeContext);
    }

    /**
     * Tests {@link ImportAttributeTag} with name not specified and ignore true.
     *
     * @throws TilesJspException If something goes wrong.
     */
    @Test
    public void testTagNullEvaluateAllIgnoreException() throws TilesJspException {
        PageContext pageContext = createMock(PageContext.class);
        TilesContainer container = createMock(TilesContainer.class);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        Attribute attribute = new Attribute("myValue");
        expect(pageContext.getAttribute(
                ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME,
                PageContext.REQUEST_SCOPE)).andReturn(container);
        expect(container.getAttributeContext(pageContext)).andReturn(attributeContext);
        expect(attributeContext.getAttribute("attributeName")).andReturn(attribute);
        expect(container.evaluate(attribute, pageContext)).andReturn(null);
        expect(attributeContext.getCascadedAttributeNames()).andReturn(null);
        Set<String> names = new HashSet<String>();
        names.add("attributeName");
        expect(attributeContext.getLocalAttributeNames()).andReturn(names);

        replay(pageContext, container, attributeContext);
        ImportAttributeTag tag = new ImportAttributeTag();
        tag.setPageContext(pageContext);
        tag.setIgnore(true);
        tag.doStartTag();
        verify(pageContext, container, attributeContext);
    }
}
