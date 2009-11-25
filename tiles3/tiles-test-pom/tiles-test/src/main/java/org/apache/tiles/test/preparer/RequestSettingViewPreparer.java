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
package org.apache.tiles.test.preparer;

import java.util.Map;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;

/**
 * A simple test <code>ViewPreparer</code> to put a request attribute, that
 * will be used with the EL evaluator.
 *
 * @version $Rev$ $Date$
 */
public class RequestSettingViewPreparer implements ViewPreparer {

    /** {@inheritDoc} */
    public void execute(Request tilesContext,
            AttributeContext attributeContext) {
        Map<String, Object> requestScope = tilesContext.getContext("request");
        requestScope.put("body", "test.inner.definition");
        requestScope.put("layout", "/layout.jsp");
        requestScope.put("doNotShow", "DO NOT SHOW!!!");
        requestScope.put("doNotShowBody", "${requestScope.doNotShow}");
    }
}
