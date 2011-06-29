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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateMethod;
import org.apache.tiles.autotag.model.TemplateParameter;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.tiles.autotag.plugin.internal.AnnotatedExampleModel;
import org.apache.tiles.autotag.plugin.internal.ExampleExecutableModel;
import org.apache.tiles.autotag.plugin.internal.ExampleModel;
import org.apache.tiles.autotag.plugin.internal.NotFeasibleExampleModel;
import org.apache.tiles.request.Request;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

/**
 * Tests {@link CreateDescriptorMojo}.
 *
 * @version $Rev$ $Date$
 */
public class CreateDescriptorMojoTest {

    /**
     * Test method for {@link org.apache.tiles.autotag.plugin.CreateDescriptorMojo#execute()}.
     * @throws IOException If something goes wrong.
     * @throws MojoExecutionException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException, MojoExecutionException {
        MavenProject mavenProject = createMock(MavenProject.class);

        CreateDescriptorMojo mojo = new CreateDescriptorMojo();
        mojo.sourceDirectory = new File(System.getProperty("basedir"), "src/test/java");
        File temp = File.createTempFile("autotagmojo", ".tmp");
        temp.delete();
        temp.mkdirs();
        mojo.outputDirectory = temp;
        mojo.name = "test";
        mojo.documentation = "This are the docs";
        mojo.project = mavenProject;

        mavenProject.addResource(isA(Resource.class));

        replay(mavenProject);
        mojo.execute();
        InputStream sis = new FileInputStream(new File(temp, "META-INF/template-suite.xml"));
        XStream xstream = new XStream();
        TemplateSuite suite = (TemplateSuite) xstream.fromXML(sis);
        sis.close();
        assertEquals("test", suite.getName());
        assertEquals("This are the docs", suite.getDocumentation());
        assertEquals(3, suite.getTemplateClasses().size());

        TemplateClass templateClass = suite.getTemplateClassByName(ExampleModel.class.getName());
        assertNotNull(templateClass);
        assertEquals(ExampleModel.class.getName(), templateClass.getName());
        assertEquals("Example start/stop template.", templateClass.getDocumentation());
        TemplateMethod templateMethod = templateClass.getExecuteMethod();
        assertNotNull(templateMethod);
        assertTrue(templateMethod.hasBody());
        assertTrue(templateClass.hasBody());
        assertEquals("execute", templateMethod.getName());
        assertEquals("It starts.", templateMethod.getDocumentation());
        List<TemplateParameter> parameters = new ArrayList<TemplateParameter>(templateMethod.getParameters());
        assertEquals(4, parameters.size());
        TemplateParameter parameter = parameters.get(0);
        assertEquals("one", parameter.getName());
        assertEquals("java.lang.String", parameter.getType());
        assertEquals("Parameter one.", parameter.getDocumentation());
        parameter = parameters.get(1);
        assertEquals("two", parameter.getName());
        assertEquals("int", parameter.getType());
        assertEquals("Parameter two.", parameter.getDocumentation());
        parameter = parameters.get(2);
        assertEquals("request", parameter.getName());
        assertEquals(Request.class.getName(), parameter.getType());
        assertEquals("The request.", parameter.getDocumentation());
        parameter = parameters.get(3);
        assertEquals("modelBody", parameter.getName());
        assertEquals(ModelBody.class.getName(), parameter.getType());
        assertEquals("The model body.", parameter.getDocumentation());

        templateClass = suite.getTemplateClassByName(AnnotatedExampleModel.class.getName());
        assertNotNull(templateClass);
        assertEquals(AnnotatedExampleModel.class.getName(), templateClass.getName());
        templateMethod = templateClass.getExecuteMethod();
        assertNotNull(templateMethod);
        assertEquals("execute", templateMethod.getName());
        parameters = new ArrayList<TemplateParameter>(templateMethod.getParameters());
        assertEquals(4, parameters.size());
        parameter = parameters.get(0);
        assertEquals("one", parameter.getName());
        assertEquals("alternateOne", parameter.getExportedName());
        assertEquals("java.lang.String", parameter.getType());
        assertEquals("Parameter one.", parameter.getDocumentation());
        assertEquals("hello", parameter.getDefaultValue());
        assertTrue(parameter.isRequired());

        templateClass = suite.getTemplateClassByName(ExampleExecutableModel.class.getName());
        assertNotNull(templateClass);
        assertEquals(ExampleExecutableModel.class.getName(), templateClass.getName());
        assertEquals("Example executable template.", templateClass.getDocumentation());
        templateMethod = templateClass.getExecuteMethod();
        assertNotNull(templateMethod);
        assertEquals("execute", templateMethod.getName());
        assertEquals("It executes.", templateMethod.getDocumentation());
        parameters = new ArrayList<TemplateParameter>(templateMethod.getParameters());
        assertEquals(3, parameters.size());
        parameter = parameters.get(0);
        assertEquals("one", parameter.getName());
        assertEquals("java.lang.String", parameter.getType());
        assertEquals("Parameter one.", parameter.getDocumentation());
        parameter = parameters.get(1);
        assertEquals("two", parameter.getName());
        assertEquals("int", parameter.getType());
        assertEquals("Parameter two.", parameter.getDocumentation());
        parameter = parameters.get(2);
        assertEquals("request", parameter.getName());
        assertEquals(Request.class.getName(), parameter.getType());
        assertEquals("The request.", parameter.getDocumentation());

        assertNull(suite.getTemplateClassByName(NotFeasibleExampleModel.class.getName()));
        FileUtils.deleteDirectory(temp);
        verify(mavenProject);
    }

}
