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


/**
 * Evaluates string expression with typical EL syntax.<br>
 * You can use normal EL syntax, knowing that the root objects are
 * {@link org.apache.tiles.TilesRequestContext},
 * {@link org.apache.tiles.TilesApplicationContext} and beans contained in
 * request, session and application scope.
 *
 * @version $Rev$ $Date$
 * @since 2.1.0
 * @deprecated Use {@link org.apache.tiles.el.ELAttributeEvaluator}.
 */
@Deprecated
public class ELAttributeEvaluator extends org.apache.tiles.el.ELAttributeEvaluator {
}
