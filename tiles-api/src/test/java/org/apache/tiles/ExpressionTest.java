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

package org.apache.tiles;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link Expression}.
 *
 * @version $Rev$ $Date$
 */
public class ExpressionTest {

    /**
     * Test method for {@link org.apache.tiles.Expression#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Expression expression = new Expression("hello", "there");
        assertEquals("hello".hashCode() + "there".hashCode(), expression.hashCode());
    }

    /**
     * Test method for {@link org.apache.tiles.Expression#Expression(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testExpressionStringString() {
        Expression expression = new Expression("hello", "there");
        assertEquals("hello", expression.getExpression());
        assertEquals("there", expression.getLanguage());
    }

    /**
     * Test method for {@link org.apache.tiles.Expression#Expression(java.lang.String)}.
     */
    @Test
    public void testExpressionString() {
        Expression expression = new Expression("hello");
        assertEquals("hello", expression.getExpression());
        assertNull(expression.getLanguage());
    }

    /**
     * Test method for {@link org.apache.tiles.Expression#Expression(org.apache.tiles.Expression)}.
     */
    @Test
    public void testExpressionExpression() {
        Expression expression = new Expression("hello", "there");
        Expression expression2 = new Expression(expression);
        assertEquals("hello", expression2.getExpression());
        assertEquals("there", expression2.getLanguage());
    }

    /**
     * Test method for {@link org.apache.tiles.Expression#createExpressionFromDescribedExpression(java.lang.String)}.
     */
    @Test
    public void testCreateExpressionFromDescribedExpression() {
        Expression expression = Expression.createExpressionFromDescribedExpression("hello");
        assertEquals("hello", expression.getExpression());
        assertNull(expression.getLanguage());
        expression = Expression.createExpressionFromDescribedExpression("there:hello");
        assertEquals("hello", expression.getExpression());
        assertEquals("there", expression.getLanguage());
        expression = Expression.createExpressionFromDescribedExpression("there_:hello");
        assertEquals("there_:hello", expression.getExpression());
        assertNull(expression.getLanguage());
        assertNull(Expression.createExpressionFromDescribedExpression(null));
    }

    /**
     * Test method for {@link org.apache.tiles.Expression#createExpression(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testCreateExpression() {
        Expression expression = Expression.createExpression("hello", "there");
        assertEquals("hello", expression.getExpression());
        assertEquals("there", expression.getLanguage());
        expression = Expression.createExpression("hello", null);
        assertEquals("hello", expression.getExpression());
        assertNull(expression.getLanguage());
        expression = Expression.createExpression(null, "there");
        assertNull(expression);
    }

    /**
     * Test method for {@link org.apache.tiles.Expression#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        Expression expression = new Expression("hello", "there");
        Expression expression2 = new Expression("hello", "there");
        assertEquals(expression, expression2);
        expression2 = new Expression("hello", "there2");
        assertFalse(expression.equals(expression2));
        expression2 = new Expression("hello");
        assertFalse(expression.equals(expression2));
        expression = new Expression("hello");
        assertEquals(expression, expression2);
    }

    /**
     * Test method for {@link org.apache.tiles.Expression#toString()}.
     */
    @Test
    public void testToString() {
        Expression expression = new Expression("hello", "there");
        assertEquals("there:hello", expression.toString());
        expression = new Expression("hello");
        assertEquals("DEFAULT:hello", expression.toString());
    }

}
