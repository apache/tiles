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

import javax.el.ArrayELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;

import org.apache.tiles.Attribute;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.awareness.TilesContextFactoryAware;
import org.apache.tiles.context.TilesContextFactory;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.evaluator.AttributeEvaluator;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * Evaluates string expression with typical EL syntax.<br>
 * You can use normal EL syntax, knowing that the root objects are
 * {@link TilesRequestContext}, {@link TilesApplicationContext} and beans
 * contained in request, session and application scope.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 */
public class ELAttributeEvaluator implements AttributeEvaluator,
        TilesContextFactoryAware {

    /**
     * The Tiles application context.
     *
     * @since 2.1.0
     */
    protected TilesApplicationContext applicationContext;

    /**
     * The Tiles context factory.
     *
     * @since 2.1.0
     */
    protected TilesContextFactory contextFactory;

    /**
     * The EL expression factory.
     *
     * @since 2.1.0
     */
    protected ExpressionFactory expressionFactory;

    /**
     * The EL resolver to use.
     *
     * @since 2.1.0
     */
    protected ELResolver defaultResolver;

    /**
     * Constructor.
     *
     * @since 2.1.0
     */
    public ELAttributeEvaluator() {
        // FIXME Take a different strategy to hold the expression factory.
        expressionFactory = new ExpressionFactoryImpl();
        defaultResolver = new CompositeELResolver() {
            {
                add(new TilesContextELResolver());
                add(new TilesContextBeanELResolver());
                add(new ArrayELResolver(false));
                add(new ListELResolver(false));
                add(new MapELResolver(false));
                add(new ResourceBundleELResolver());
            }
        };
    }

    /** {@inheritDoc} */
    public void setApplicationContext(TilesApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /** {@inheritDoc} */
    public void setContextFactory(TilesContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    /** {@inheritDoc} */
    public Object evaluate(Attribute attribute, TilesRequestContext request) {
        Object retValue = attribute.getValue();

        if (retValue instanceof String) {
            SimpleContext context = new SimpleContext(defaultResolver);
            context.putContext(TilesRequestContext.class, request);
            context.putContext(TilesApplicationContext.class,
                    applicationContext);
            ValueExpression expression = expressionFactory
                    .createValueExpression(context, attribute.getValue()
                            .toString(), Object.class);

            retValue = expression.getValue(context);
        }

        return retValue;
    }
}
