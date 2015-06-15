/*
 * ExpressionAware.java    Jun 12 2015, 07:42
 *
 * Copyright 2015 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tiles.awareness;

import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.request.Request;


/**
 * Provides the ability for extended attributes a mechanism for evaluating
 * expressions.
 *
 * Objects will have {@link #evaluateExpressions(AttributeEvaluator, Request) evaluateExpressions}
 * called which leaves the evaluation up to the object in question.
 *
 * @author  Brett Ryan
 * @since   3.0.6
 */
public interface ExpressionAware {

    /**
     * Evaluate supported expressions on object.
     *
     * Implementors may return any type for view rendering.
     *
     * @param   eval
     *          Evaluator instance used to evaluate expressions.
     * @param   request
     *          Request object to evaluate expressions with.
     * @return  Model instance with attributes evaluated.
     */
    Object evaluateExpressions(AttributeEvaluator eval, Request request);

}
