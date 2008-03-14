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
package org.apache.tiles.evaluator.el;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.easymock.EasyMock;

import junit.framework.TestCase;

/**
 * Tests {@link ELAttributeEvaluator}.
 *
 * @version $Rev$ $Date$
 */
public class ELAttributeEvaluatorTest extends TestCase {

    /**
     * The evaluator to test.
     */
    private ELAttributeEvaluator evaluator;

    /**
     * The request object to use.
     */
    private TilesRequestContext request;

    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
        evaluator = new ELAttributeEvaluator();
        TilesContextFactory factory = EasyMock
                .createMock(TilesContextFactory.class);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put("object1", "value");
        sessionScope.put("object2", new Integer(1));
        applicationScope.put("object3", new Float(2.0));
        request = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.expect(request.getRequestScope()).andReturn(requestScope)
                .anyTimes();
        EasyMock.expect(request.getSessionScope()).andReturn(sessionScope)
                .anyTimes();
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        EasyMock.expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope).anyTimes();
        EasyMock.replay(request, applicationContext);

        evaluator.setContextFactory(factory);
        evaluator.setApplicationContext(applicationContext);
    }

    /**
     * Tests
     * {@link ELAttributeEvaluator#evaluate(Attribute, TilesRequestContext)}.
     */
    public void testEvaluate() {
        Attribute attribute = new Attribute();
        attribute.setValue("${requestScope.object1}");
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                attribute, request));
        attribute.setValue("${sessionScope.object2}");
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(attribute, request));
        attribute.setValue("${applicationScope.object3}");
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(attribute, request));
        attribute.setValue("${object1}");
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                attribute, request));
        attribute.setValue("${object2}");
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(attribute, request));
        attribute.setValue("${object3}");
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(attribute, request));
        attribute.setValue("String literal");
        assertEquals("The value is not correct", "String literal", evaluator
                .evaluate(attribute, request));
        attribute.setValue(new Integer(2));
        assertEquals("The value is not correct", new Integer(2), evaluator
                .evaluate(attribute, request));
    }

    /**
     * Tests
     * {@link ELAttributeEvaluator#evaluate(String, TilesRequestContext)}.
     */
    public void testEvaluateString() {
        String expression = "${requestScope.object1}";
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                expression, request));
        expression = "${sessionScope.object2}";
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(expression, request));
        expression = "${applicationScope.object3}";
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(expression, request));
        expression = "${object1}";
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                expression, request));
        expression = "${object2}";
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(expression, request));
        expression = "${object3}";
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(expression, request));
        expression = "String literal";
        assertEquals("The value is not correct", expression, evaluator
                .evaluate(expression, request));
    }
}
