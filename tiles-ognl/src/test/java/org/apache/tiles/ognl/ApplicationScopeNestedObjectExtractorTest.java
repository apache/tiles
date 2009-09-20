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

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.ognl.ApplicationScopeNestedObjectExtractor;
import org.apache.tiles.ognl.NestedObjectExtractor;
import org.junit.Test;

/**
 * Tests {@link ApplicationScopeNestedObjectExtractor}.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationScopeNestedObjectExtractorTest {

    /**
     * Tests {@link ApplicationScopeNestedObjectExtractor#getNestedObject(TilesRequestContext)}.
     */
    @Test
    public void testGetNestedObject() {
        TilesRequestContext request = createMock(TilesRequestContext.class);
        TilesApplicationContext applicationContext = createMock(TilesApplicationContext.class);
        expect(request.getApplicationContext()).andReturn(applicationContext);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attribute1", "value1");
        expect(applicationContext.getApplicationScope()).andReturn(map);

        replay(request, applicationContext);
        NestedObjectExtractor<TilesRequestContext> extractor = new ApplicationScopeNestedObjectExtractor();
        assertEquals(map, extractor.getNestedObject(request));
        verify(request, applicationContext);
    }

}
