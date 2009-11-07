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

import org.apache.tiles.Attribute;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.renderer.TypeDetectingAttributeRenderer;
import org.apache.tiles.request.Request;

/**
 * Abstract implementation of {@link TypeDetectingAttributeRenderer} that
 * implements {@link #isRenderable(Attribute, Request)} to delegate
 * to {@link #isRenderable(Object, Attribute, Request)}.
 *
 * @version $Rev$ $Date$
 * @since 2.2.1
 */
public abstract class AbstractTypeDetectingAttributeRenderer extends
        AbstractBaseAttributeRenderer implements TypeDetectingAttributeRenderer {

    /** {@inheritDoc} */
    public boolean isRenderable(Attribute attribute, Request request) {
        AttributeEvaluator evaluator = attributeEvaluatorFactory
                .getAttributeEvaluator(attribute);
        Object value = evaluator.evaluate(attribute, request);
        return isRenderable(value, attribute, request);
    }
}
