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
package org.apache.tiles.evaluator;

import org.apache.tiles.Attribute;
import org.apache.tiles.Expression;
import org.apache.tiles.awareness.ExpressionAware;
import org.apache.tiles.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class to link a correct evaluation of an attribute, by evaluating
 * {@link Attribute#getValue()} and then {@link Attribute#getExpressionObject()}.
 *
 * @version $Rev$ $Date$
 * @since 2.1.2
 */
public abstract class AbstractAttributeEvaluator implements AttributeEvaluator {

    private static final Logger log = LoggerFactory.getLogger(AbstractAttributeEvaluator.class);

    /** {@inheritDoc} */
    public Object evaluate(Attribute attribute, Request request) {
        if (attribute == null) {
            throw new IllegalArgumentException("The attribute cannot be null");
        }

        Object retValue = attribute.getValue();

        log.debug("Evaluating expression: [attribute={},retValue={}]", attribute, retValue);
        if (retValue == null) {
            Expression expression = attribute.getExpressionObject();
            if (expression != null) {
                retValue = evaluate(attribute.getExpressionObject()
                        .getExpression(), request);
            }
        } else if (retValue instanceof Iterable) {
            log.debug("Instance is list, evaluating for expression awareness.");
            Iterable list = (Iterable)retValue;
            for (Object n : list) {
                log.debug("list instance {} ({})", n, n.getClass());
                if (n instanceof Attribute) {
                    log.debug("list instance is an attribute");
                    evaluate((Attribute) n, request);
                } else if (n instanceof ExpressionAware) {
                    log.debug("List instance is expression-aware");
                    ((ExpressionAware) n).evaluateExpressions(this, request);
                }
            }
        } else if (retValue instanceof ExpressionAware) {
            log.debug("Instance is expression-aware");
            ((ExpressionAware) retValue).evaluateExpressions(this, request);
        }

        return retValue;
    }
}
