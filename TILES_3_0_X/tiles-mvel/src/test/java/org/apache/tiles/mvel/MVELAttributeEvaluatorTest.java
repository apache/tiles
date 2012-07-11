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

import java.util.Arrays;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Expression;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.integration.VariableResolverFactory;

/**
 * Tests {@link MVELAttributeEvaluator}.
 *
 * @version $Rev$ $Date$$
 */
public class MVELAttributeEvaluatorTest {

    /**
     * The evaluator to test.
     */
    private MVELAttributeEvaluator evaluator;

    /**
     * The request object to use.
     */
    private Request request;

    /**
     * The application context.
     */
    private ApplicationContext applicationContext;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        TilesRequestContextHolder requestHolder = new TilesRequestContextHolder();
        VariableResolverFactory variableResolverFactory = new ScopeVariableResolverFactory(
                requestHolder);
        variableResolverFactory
                .setNextFactory(new TilesContextVariableResolverFactory(
                        requestHolder));
        variableResolverFactory
                .setNextFactory(new TilesContextBeanVariableResolverFactory(
                        requestHolder));
        evaluator = new MVELAttributeEvaluator(requestHolder,
                variableResolverFactory);
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put("object1", "value");
        sessionScope.put("object2", new Integer(1));
        applicationScope.put("object3", new Float(2.0));
        requestScope.put("paulaBean", new PaulaBean());
        request = createMock(Request.class);
        expect(request.getContext("request")).andReturn(requestScope)
                .anyTimes();
        expect(request.getContext("session")).andReturn(sessionScope)
                .anyTimes();
        expect(request.getContext("application")).andReturn(applicationScope)
                .anyTimes();
        expect(request.getAvailableScopes()).andReturn(
                Arrays.asList(new String[] { "request", "session", "application" })).anyTimes();
        applicationContext = createMock(ApplicationContext.class);
        expect(request.getApplicationContext()).andReturn(
                applicationContext).anyTimes();
        replay(request, applicationContext);
    }

    /**
     * Tests {@link MVELAttributeEvaluator#evaluate(String, Request)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNull() {
        evaluator.evaluate((String) null, request);
        verify(request, applicationContext);
    }

    /**
     * Tests
     * {@link MVELAttributeEvaluator#evaluate(Attribute, Request)}.
     */
    @Test
    public void testEvaluate() {
        Attribute attribute = new Attribute();
        attribute.setExpressionObject(new Expression("requestScope.object1"));
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                attribute, request));
        attribute.setExpressionObject(new Expression("sessionScope.object2"));
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(attribute, request));
        attribute.setExpressionObject(new Expression("applicationScope.object3"));
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(attribute, request));
        attribute.setExpressionObject(new Expression("object1"));
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                attribute, request));
        attribute.setExpressionObject(new Expression("object2"));
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(attribute, request));
        attribute.setExpressionObject(new Expression("object3"));
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(attribute, request));
        attribute.setExpressionObject(new Expression("paulaBean.paula"));
        assertEquals("The value is not correct", "Brillant", evaluator
                .evaluate(attribute, request));
        attribute.setExpressionObject(new Expression("'String literal'"));
        assertEquals("The value is not correct", "String literal", evaluator
                .evaluate(attribute, request));
        attribute.setValue(new Integer(2));
        assertEquals("The value is not correct", new Integer(2), evaluator
                .evaluate(attribute, request));
        attribute.setValue("object1");
        assertEquals("The value has been evaluated", "object1", evaluator
                .evaluate(attribute, request));
        verify(request, applicationContext);
    }

    /**
     * Tests {@link MVELAttributeEvaluator#evaluate(String, Request)}.
     */
    @Test
    public void testEvaluateString() {
        String expression = "requestScope.object1";
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                expression, request));
        expression = "sessionScope.object2";
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(expression, request));
        expression = "applicationScope.object3";
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(expression, request));
        expression = "object1";
        assertEquals("The value is not correct", "value", evaluator.evaluate(
                expression, request));
        expression = "object2";
        assertEquals("The value is not correct", new Integer(1), evaluator
                .evaluate(expression, request));
        expression = "object3";
        assertEquals("The value is not correct", new Float(2.0), evaluator
                .evaluate(expression, request));
        expression = "paulaBean.paula";
        assertEquals("The value is not correct", "Brillant", evaluator
                .evaluate(expression, request));
        expression = "'String literal'";
        assertEquals("The value is not correct", "String literal", evaluator
                .evaluate(expression, request));
        verify(request, applicationContext);
    }

    /**
     * This is The Brillant Paula Bean (sic) just like it was posted to:
     * http://thedailywtf.com/Articles/The_Brillant_Paula_Bean.aspx I hope that
     * there is no copyright on it.
     */
    public static class PaulaBean {

        /**
         * Paula is brillant, really.
         */
        private String paula = "Brillant";

        /**
         * Returns brillant.
         *
         * @return "Brillant".
         */
        public String getPaula() {
            return paula;
        }
    }
}
