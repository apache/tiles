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

package org.apache.tiles.eval;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of {@link AttributeEvaluatorFactory}. It supports a
 * default attribute evaluator, in case the language is not recognized.
 *
 * @version $Rev$ $Date$
 */
public class BasicEvaluatorRegistry implements EvaluatorRegistry {

    /**
     * The default evaluator to return if it is not found in the map of known
     * languages.
     */
    private Evaluator defaultEvaluator;

    /**
     * Maps names of expression languages to their attribute evaluator.
     */
    private Map<String, Evaluator> language2evaluator;

    /**
     * Constructor.
     *
     * @param defaultEvaluator The default evaluator to return if it is not
     * found in the map of known languages.
     * @since 2.2.0
     */
    public BasicEvaluatorRegistry(Evaluator defaultEvaluator) {
        this.defaultEvaluator = defaultEvaluator;
        language2evaluator = new HashMap<String, Evaluator>();
    }

    /**
     * Registers a known expression language with its attribute evaluator.
     *
     * @param language The name of the expression language.
     * @param evaluator The associated attribute evaluator.
     */
    public void registerAttributeEvaluator(String language, Evaluator evaluator) {
        language2evaluator.put(language, evaluator);
    }

    /** {@inheritDoc} */
    public Evaluator getAttributeEvaluator(String language) {
        Evaluator retValue = language2evaluator.get(language);
        if (retValue == null) {
            retValue = defaultEvaluator;
        }
        return retValue;
    }
}
