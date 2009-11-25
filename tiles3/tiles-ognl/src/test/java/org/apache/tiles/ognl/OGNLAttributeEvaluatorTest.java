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
package org.apache.tiles.ognl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.apache.tiles.Attribute;
import org.apache.tiles.Expression;
import org.apache.tiles.ognl.ApplicationScopeNestedObjectExtractor;
import org.apache.tiles.ognl.DelegatePropertyAccessor;
import org.apache.tiles.ognl.NestedObjectDelegatePropertyAccessor;
import org.apache.tiles.ognl.OGNLAttributeEvaluator;
import org.apache.tiles.ognl.PropertyAccessorDelegateFactory;
import org.apache.tiles.ognl.RequestScopeNestedObjectExtractor;
import org.apache.tiles.ognl.SessionScopeNestedObjectExtractor;
import org.apache.tiles.ognl.TilesApplicationContextNestedObjectExtractor;
import org.apache.tiles.ognl.TilesContextPropertyAccessorDelegateFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.easymock.EasyMock;

/**
 * Tests {@link OGNLAttributeEvaluator}.
 *
 * @version $Rev$ $Date$$
 */
public class OGNLAttributeEvaluatorTest extends TestCase {

    /**
     * The evaluator to test.
     */
    private OGNLAttributeEvaluator evaluator;

    /**
     * The request object to use.
     */
    private Request request;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PropertyAccessor objectPropertyAccessor = OgnlRuntime.getPropertyAccessor(Object.class);
        PropertyAccessor mapPropertyAccessor = OgnlRuntime.getPropertyAccessor(Map.class);
        PropertyAccessor applicationContextPropertyAccessor =
            new NestedObjectDelegatePropertyAccessor<Request>(
                new TilesApplicationContextNestedObjectExtractor(),
                objectPropertyAccessor);
        PropertyAccessor requestScopePropertyAccessor = new NestedObjectDelegatePropertyAccessor<Request>(
                new RequestScopeNestedObjectExtractor(), mapPropertyAccessor);
        PropertyAccessor sessionScopePropertyAccessor = new NestedObjectDelegatePropertyAccessor<Request>(
                new SessionScopeNestedObjectExtractor(), mapPropertyAccessor);
        PropertyAccessor applicationScopePropertyAccessor =
            new NestedObjectDelegatePropertyAccessor<Request>(
                new ApplicationScopeNestedObjectExtractor(), mapPropertyAccessor);
        PropertyAccessorDelegateFactory<Request> factory = new TilesContextPropertyAccessorDelegateFactory(
                objectPropertyAccessor, applicationContextPropertyAccessor,
                requestScopePropertyAccessor, sessionScopePropertyAccessor,
                applicationScopePropertyAccessor);
        PropertyAccessor tilesRequestAccessor = new DelegatePropertyAccessor<Request>(factory);
        OgnlRuntime.setPropertyAccessor(Request.class, tilesRequestAccessor);
        evaluator = new OGNLAttributeEvaluator();
        Map<String, Object> requestScope = new HashMap<String, Object>();
        Map<String, Object> sessionScope = new HashMap<String, Object>();
        Map<String, Object> applicationScope = new HashMap<String, Object>();
        requestScope.put("object1", "value");
        sessionScope.put("object2", new Integer(1));
        applicationScope.put("object3", new Float(2.0));
        requestScope.put("paulaBean", new PaulaBean());
        request = EasyMock.createMock(Request.class);
        EasyMock.expect(request.getContext("request")).andReturn(requestScope)
                .anyTimes();
        EasyMock.expect(request.getContext("session")).andReturn(sessionScope)
                .anyTimes();
        ApplicationContext applicationContext = EasyMock
                .createMock(ApplicationContext.class);
        EasyMock.expect(request.getApplicationContext()).andReturn(
                applicationContext).anyTimes();
        EasyMock.expect(applicationContext.getApplicationScope()).andReturn(
                applicationScope).anyTimes();
        EasyMock.replay(request, applicationContext);
    }

    /**
     * Tests
     * {@link OGNLAttributeEvaluator#evaluate(Attribute, Request)}.
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
     * Tests {@link OGNLAttributeEvaluator#evaluate(String, Request)}.
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
