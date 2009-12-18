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

package org.apache.tiles.renderer.impl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.tiles.Attribute;
import org.apache.tiles.Expression;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.request.Request;
import org.junit.Test;

/**
 * Tests {@link AbstractTypeDetectingAttributeRenderer}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractTypeDetectingAttributeRendererTest {

    /**
     * Tests
     * {@link AbstractTypeDetectingAttributeRenderer#isRenderable(Attribute, Request)}
     * .
     */
    @Test
    public void testIsRenderable() {
        AttributeEvaluatorFactory evaluatorFactory = createMock(AttributeEvaluatorFactory.class);
        AttributeEvaluator evaluator = createMock(AttributeEvaluator.class);
        Request request = createMock(Request.class);
        Attribute attribute = new Attribute("value", new Expression(
                "expression"), "role", "rendererName");

        expect(evaluatorFactory.getAttributeEvaluator(attribute)).andReturn(evaluator);
        expect(evaluator.evaluate(attribute, request)).andReturn("myValue");

        replay(evaluatorFactory, evaluator, request);

        AbstractTypeDetectingAttributeRenderer renderer = new AbstractTypeDetectingAttributeRenderer() {

            public boolean isRenderable(Object value, Attribute attribute,
                    Request request) {
                return "myValue".equals(value);
            }

            @Override
            public void write(Object value, Attribute attribute, Request request) {
                // Does nothing.
            }
        };

        renderer.setAttributeEvaluatorFactory(evaluatorFactory);
        assertTrue(renderer.isRenderable(attribute, request));

        verify(evaluatorFactory, evaluator, request);
    }
}
