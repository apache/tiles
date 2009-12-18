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

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.Renderable;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Tiles2Tool}.
 */
public class Tiles2ToolTest {

    /**
     * The key of the attribute that will be used to store the repository of "models".
     */
    private static final String TILES_VELOCITY_REPOSITORY_KEY = "org.apache.tiles.velocity.TilesVelocityRepository";

    /**
     * The tool to test.
     */
    private Tiles2Tool tool;

    /**
     * The request object.
     */
    private HttpServletRequest request;

    /**
     * The response object.
     */
    private HttpServletResponse response;

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * The current velocity context.
     */
    private Context velocityContext;

    /**
     * Sets up the model.
     */
    @Before
    public void setUp() {
        tool = new Tiles2Tool();
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        velocityContext = createMock(Context.class);
        servletContext = createMock(ServletContext.class);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#addAttribute(java.util.Map)}.
     */
    @Test
    public void testAddAttributeMapOfStringObject() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        AddAttributeVModel model = createMock(AddAttributeVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getAddAttribute()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(tool, tool.addAttribute(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#addAttribute()}.
     */
    @Test
    public void testAddAttribute() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        AddAttributeVModel model = createMock(AddAttributeVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getAddAttribute()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.addAttribute());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#addListAttribute()}.
     */
    @Test
    public void testAddListAttribute() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        AddListAttributeVModel model = createMock(AddListAttributeVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getAddListAttribute()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.addListAttribute());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#definition(java.util.Map)}.
     */
    @Test
    public void testDefinitionMapOfStringObject() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        DefinitionVModel model = createMock(DefinitionVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getDefinition()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(tool, tool.definition(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#definition()}.
     */
    @Test
    public void testDefinition() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        DefinitionVModel model = createMock(DefinitionVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getDefinition()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.definition());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#getAsString(java.util.Map)}.
     */
    @Test
    public void testGetAsStringMapOfStringObject() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        GetAsStringVModel model = createMock(GetAsStringVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getGetAsString()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(renderable, tool.getAsString(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#getAsString()}.
     */
    @Test
    public void testGetAsString() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        GetAsStringVModel model = createMock(GetAsStringVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getGetAsString()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.getAsString());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#importAttribute(java.util.Map)}.
     */
    @Test
    public void testImportAttribute() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        ImportAttributeVModel model = createMock(ImportAttributeVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getImportAttribute()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(renderable, tool.importAttribute(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#insertAttribute(java.util.Map)}.
     */
    @Test
    public void testInsertAttributeMapOfStringObject() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        InsertAttributeVModel model = createMock(InsertAttributeVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getInsertAttribute()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(renderable, tool.insertAttribute(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#insertAttribute()}.
     */
    @Test
    public void testInsertAttribute() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        InsertAttributeVModel model = createMock(InsertAttributeVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getInsertAttribute()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.insertAttribute());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#insertDefinition(java.util.Map)}.
     */
    @Test
    public void testInsertDefinitionMapOfStringObject() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        InsertDefinitionVModel model = createMock(InsertDefinitionVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getInsertDefinition()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(renderable, tool.insertDefinition(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#insertDefinition()}.
     */
    @Test
    public void testInsertDefinition() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        InsertDefinitionVModel model = createMock(InsertDefinitionVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getInsertDefinition()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.insertDefinition());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#insertTemplate(java.util.Map)}.
     */
    @Test
    public void testInsertTemplateMapOfStringObject() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        InsertTemplateVModel model = createMock(InsertTemplateVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getInsertTemplate()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(renderable, tool.insertTemplate(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#insertTemplate()}.
     */
    @Test
    public void testInsertTemplate() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        InsertTemplateVModel model = createMock(InsertTemplateVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getInsertTemplate()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.insertTemplate());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#putAttribute(java.util.Map)}.
     */
    @Test
    public void testPutAttributeMapOfStringObject() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        PutAttributeVModel model = createMock(PutAttributeVModel.class);
        Renderable renderable = createMock(Renderable.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getPutAttribute()).andReturn(model);
        expect(model.execute(request, response, velocityContext, params)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(tool, tool.putAttribute(params));
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#putAttribute()}.
     */
    @Test
    public void testPutAttribute() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        PutAttributeVModel model = createMock(PutAttributeVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getPutAttribute()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.putAttribute());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#putListAttribute()}.
     */
    @Test
    public void testPutListAttribute() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        PutListAttributeVModel model = createMock(PutListAttributeVModel.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getPutListAttribute()).andReturn(model);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.putListAttribute());
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#start(java.util.Map)}.
     */
    @Test
    public void testStart() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        PutAttributeVModel model = createMock(PutAttributeVModel.class);
        Map<String, Object> params = createParams();

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getPutAttribute()).andReturn(model);
        model.start(request, response, velocityContext, params);

        replay(velocityContext, request, response, servletContext, repository, model);
        initializeTool();
        assertEquals(tool, tool.putAttribute().start(params));
        verify(velocityContext, request, response, servletContext, repository, model);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#end()}.
     */
    @Test
    public void testEnd() {
        TilesVelocityRepository repository = createMock(TilesVelocityRepository.class);
        PutAttributeVModel model = createMock(PutAttributeVModel.class);
        Renderable renderable = createMock(Renderable.class);

        expect(servletContext.getAttribute(TILES_VELOCITY_REPOSITORY_KEY)).andReturn(repository);
        expect(repository.getPutAttribute()).andReturn(model);
        expect(model.end(request, response, velocityContext)).andReturn(renderable);

        replay(velocityContext, request, response, servletContext, repository, model, renderable);
        initializeTool();
        assertEquals(renderable, tool.putAttribute().end());
        verify(velocityContext, request, response, servletContext, repository, model, renderable);
    }

    /**
     * Test method for {@link org.apache.tiles.velocity.template.Tiles2Tool#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("", tool.toString());
    }

    /**
     * Initializes the tool for the test.
     */
    private void initializeTool() {
        tool.setRequest(request);
        tool.setResponse(response);
        tool.setServletContext(servletContext);
        tool.setVelocityContext(velocityContext);
    }

    /**
     * Creates some mock params.
     *
     * @return The parameters.
     */
    private Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("one", "value1");
        params.put("two", "value2");
        return params;
    }
}
