/*
 * $Id$
 *
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.tiles.preparer;

import junit.framework.TestCase;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.apache.tiles.preparer.ViewPreparer;

public class BasicPreparerFactoryTest extends TestCase {

    private BasicPreparerFactory factory;

    public void setUp() {
        factory = new BasicPreparerFactory();
    }

    public void testGetPreparer() throws NoSuchPreparerException {
        String name = ViewPreparerSupport.class.getName();
        ViewPreparer p = factory.getPreparer(name, null);
        assertNotNull(p);
        assertTrue(p instanceof ViewPreparerSupport);

        name = "org.doesnotexist.Class";
        try {
            p = factory.getPreparer(name, null);
            assertNull(p);
            fail("Exception should have been thrown.");
        } catch(NoSuchPreparerException n) {
            //Good!
        }
    }
}
