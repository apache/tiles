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

import javax.servlet.ServletContext;

import org.apache.tiles.template.AddAttributeModel;
import org.apache.tiles.template.AddListAttributeModel;
import org.apache.tiles.template.AttributeResolver;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.DefinitionModel;
import org.apache.tiles.template.GetAsStringModel;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.tiles.template.InsertAttributeModel;
import org.apache.tiles.template.InsertDefinitionModel;
import org.apache.tiles.template.InsertTemplateModel;
import org.apache.tiles.template.PutAttributeModel;
import org.apache.tiles.template.PutListAttributeModel;

/**
 * Collects all Tiles+Velocity models.
 *
 * @version $Rev$ $Date$
 * @since 2.2.0
 */
public class TilesVelocityRepository {

    /**
     * The "addAttribute" model.
     */
    private AddAttributeVModel addAttribute;

    /**
     * The "addListAttribute" model.
     */
    private AddListAttributeVModel addListAttribute;

    /**
     * The "definition" model.
     */
    private DefinitionVModel definition;

    /**
     * The "getAsString" model.
     */
    private GetAsStringVModel getAsString;

    /**
     * The "importAttribute" model.
     */
    private ImportAttributeVModel importAttribute;

    /**
     * The "insertAttribute" model.
     */
    private InsertAttributeVModel insertAttribute;

    /**
     * The "insertDefinition" model.
     */
    private InsertDefinitionVModel insertDefinition;

    /**
     * The "insertTemplate" model.
     */
    private InsertTemplateVModel insertTemplate;

    /**
     * The "putAttribute" model.
     */
    private PutAttributeVModel putAttribute;

    /**
     * The "putListAttribute" model.
     */
    private PutListAttributeVModel putListAttribute;

    /**
     * Constructor.
     *
     * @param servletContext The servlet context.
     * @since 2.2.0
     */
    public TilesVelocityRepository(ServletContext servletContext) {
        AttributeResolver attributeResolver = new DefaultAttributeResolver();

        addAttribute = new AddAttributeVModel(new AddAttributeModel());
        addListAttribute = new AddListAttributeVModel(
                new AddListAttributeModel());
        definition = new DefinitionVModel(new DefinitionModel(), servletContext);
        getAsString = new GetAsStringVModel(new GetAsStringModel(
                attributeResolver), servletContext);
        importAttribute = new ImportAttributeVModel(new ImportAttributeModel(),
                servletContext);
        insertAttribute = new InsertAttributeVModel(new InsertAttributeModel(
                attributeResolver), servletContext);
        insertDefinition = new InsertDefinitionVModel(
                new InsertDefinitionModel(), servletContext);
        insertTemplate = new InsertTemplateVModel(new InsertTemplateModel(),
                servletContext);
        putAttribute = new PutAttributeVModel(new PutAttributeModel(),
                servletContext);
        putListAttribute = new PutListAttributeVModel(
                new PutListAttributeModel(), servletContext);
    }

    /**
     * Returns the "addAttribute" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public AddAttributeVModel getAddAttribute() {
        return addAttribute;
    }

    /**
     * Returns the "addListAttribute" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public AddListAttributeVModel getAddListAttribute() {
        return addListAttribute;
    }

    /**
     * Returns the "definition" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public DefinitionVModel getDefinition() {
        return definition;
    }

    /**
     * Returns the "getAsString" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public GetAsStringVModel getGetAsString() {
        return getAsString;
    }

    /**
     * Returns the "importAttribute" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public ImportAttributeVModel getImportAttribute() {
        return importAttribute;
    }

    /**
     * Returns the "insertAttribute" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public InsertAttributeVModel getInsertAttribute() {
        return insertAttribute;
    }

    /**
     * Returns the "insertDefinition" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public InsertDefinitionVModel getInsertDefinition() {
        return insertDefinition;
    }

    /**
     * Returns the "insertTemplate" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public InsertTemplateVModel getInsertTemplate() {
        return insertTemplate;
    }

    /**
     * Returns the "putAttribute" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public PutAttributeVModel getPutAttribute() {
        return putAttribute;
    }

    /**
     * Returns the "putListAttribute" model.
     *
     * @return The model.
     * @since 2.2.0
     */
    public PutListAttributeVModel getPutListAttribute() {
        return putListAttribute;
    }
}
