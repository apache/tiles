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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.generate.TemplateGenerator;
import org.apache.tiles.autotag.generate.TemplateGeneratorFactory;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

/**
 * Tests {@link AbstractGenerateMojo}.
 *
 * @version $Rev$ $Date$
 */
public class AbstractGenerateMojoTest {

    /**
     * Tests {@link AbstractGenerateMojo#execute()}.
     * @throws IOException If something goes wrong.
     * @throws MojoExecutionException If something goes wrong.
     */
    @Test
    public void testExecute() throws IOException, MojoExecutionException {
        MavenProject mavenProject = createMock(MavenProject.class);
        TemplateGeneratorFactory factory = createMock(TemplateGeneratorFactory.class);
        TemplateGenerator generator = createMock(TemplateGenerator.class);
        @SuppressWarnings("unchecked")
        Map<String, String> params = createMock(Map.class);
        AbstractGenerateMojo mojo = createMockBuilder(AbstractGenerateMojo.class).createMock();
        List<String> classpathElements = new ArrayList<String>();
        File source = new File(System.getProperty("basedir"), "src/test/resources");
        classpathElements.add(source.getAbsolutePath());
        mojo.classpathElements = classpathElements;
        File temp = File.createTempFile("autotagmojogen", ".tmp");
        temp.delete();
        temp.mkdirs();
        File resourcesOutputDirectory = new File(temp, "res/");
        File classesOutputDirectory = new File(temp, "classes/");
        resourcesOutputDirectory.mkdir();
        classesOutputDirectory.mkdir();
        mojo.resourcesOutputDirectory = resourcesOutputDirectory;
        mojo.classesOutputDirectory = classesOutputDirectory;
        mojo.packageName = "my.package";
        mojo.project = mavenProject;

        expect(mojo.createTemplateGeneratorFactory(isA(VelocityEngine.class))).andReturn(factory);
        expect(factory.createTemplateGenerator()).andReturn(generator);
        expect(mojo.getParameters()).andReturn(params);
        expect(mojo.getRuntimeClass()).andReturn("my.package.Runtime");
        generator.generate(eq("my.package"), isA(TemplateSuite.class), eq(params), eq("my.package.Runtime"));
        expect(generator.isGeneratingClasses()).andReturn(true);
        expect(generator.isGeneratingResources()).andReturn(true);
        mavenProject.addResource(isA(Resource.class));
        mavenProject.addCompileSourceRoot(classesOutputDirectory.getAbsolutePath());

        replay(mavenProject, mojo, factory, generator, params);
        mojo.execute();
        FileUtils.deleteDirectory(temp);
        verify(mavenProject, mojo, factory, generator, params);
    }

}
