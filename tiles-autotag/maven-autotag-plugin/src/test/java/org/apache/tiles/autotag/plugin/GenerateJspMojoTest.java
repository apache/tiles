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
package org.apache.tiles.autotag.plugin;

import static org.junit.Assert.*;

import org.apache.tiles.autotag.jsp.JspTemplateGeneratorFactory;
import org.junit.Test;

/**
 * Tests {@link GenerateJspMojo}.
 *
 * @version $Rev$ $Date$
 */
public class GenerateJspMojoTest {

    /**
     * Test method for {@link GenerateJspMojo#createTemplateGeneratorFactory(VelocityEngine)}.
     */
    @Test
    public void testCreateTemplateGeneratorFactory() {
        GenerateJspMojo mojo = new GenerateJspMojo();
        assertTrue(mojo.createTemplateGeneratorFactory(null) instanceof JspTemplateGeneratorFactory);
    }

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.GenerateJspMojo#getParameters()}.
     */
    @Test
    public void testGetParameters() {
        GenerateJspMojo mojo = new GenerateJspMojo();
        mojo.taglibURI = "http://www.test.org/taglib";
        assertEquals("http://www.test.org/taglib", mojo.getParameters().get("taglibURI"));
    }

}
