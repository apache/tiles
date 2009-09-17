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
package org.apache.tiles.evaluator.mvel;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.tiles.Attribute;
import org.apache.tiles.Expression;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextHolder;
import org.easymock.EasyMock;
import org.mvel2.integration.VariableResolverFactory;

/**
 * Tests {@link MVELAttributeEvaluator}.
 *
 * @version $Rev$ $Date$$
 */
public class MVELAttributeEvaluatorTest extends TestCase {

    /**
     * The evaluator to test.
     */
    private MVELAttributeEvaluator evaluator;

    /**
     * The request object to use.
     */
    private TilesRequestContext request;

    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
        TilesRequestContextHolder requestHolder = new TilesRequestContextHolder();
        VariableResolverFactory variableResolverFactory = new TilesContextVariableResolverFactory(
                requestHolder);
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
        request = EasyMock.createMock(TilesRequestContext.class);
        EasyMock.expect(request.getRequestScope()).andReturn(requestScope)
                .anyTimes();
        EasyMock.expect(request.getSessionScope()).andReturn(sessionScope)
                .anyTimes();
        TilesApplicationContext applicationContext = EasyMock
                .createMock(TilesApplicationContext.class);
        EasyMock.expect(request.getApplicationContext()).andReturn(
                applicationContext).anyTimes();
        EasyMock.expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope).anyTimes();
        EasyMock.replay(request, applicationContext);
    }

    /**
     * Tests
     * {@link MVELAttributeEvaluator#evaluate(Attribute, TilesRequestContext)}.
     */
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
    }

    /**
     * Tests {@link MVELAttributeEvaluator#evaluate(String, TilesRequestContext)}.
     */
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
