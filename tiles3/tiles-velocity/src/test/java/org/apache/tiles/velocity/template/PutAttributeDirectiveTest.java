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

package org.apache.tiles.velocity.template;

import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.request.Request;
import org.apache.tiles.template.PutAttributeModel;
import org.apache.tiles.template.body.ModelBody;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link PutAttributeDirective}.
 */
public class PutAttributeDirectiveTest {

    /**
     * The model to test.
     */
    private PutAttributeDirective model;

    /**
     * The template model.
     */
    private PutAttributeModel tModel;

    /**
     * Sets up the model to test.
     */
    @Before
    public void setUp() {
        tModel = createMock(PutAttributeModel.class);
    }

    /**
     * Test method for
     * {@link PutAttributeDirective#execute(Map, org.apache.tiles.request.Request, org.apache.tiles.template.body.ModelBody)}
     * .
     * @throws IOException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException {
        Request request = createMock(Request.class);
        Map<String, Object> params = createParams();
        ModelBody modelBody = createMock(ModelBody.class);

        tModel.execute("myName", "myValue", "myExpression", "myRole", "myType",
                false, request, modelBody);

        replay(tModel, request, modelBody);
        initializeModel();
        model.execute(params, request, modelBody);
        verify(tModel, request, modelBody);
    }

    /**
     * Creates the parameters to work with the model.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "myName");
        params.put("value", "myValue");
        params.put("expression", "myExpression");
        params.put("role", "myRole");
        params.put("type", "myType");
        params.put("cascade", false);
        return params;
    }

    /**
     * Initializes the model.
     */
    private void initializeModel() {
        model = new PutAttributeDirective(tModel);
    }
}
