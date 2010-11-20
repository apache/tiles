package org.apache.tiles.autotag.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

import java.io.File;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.generate.BasicTemplateGenerator;
import org.apache.tiles.autotag.jsp.TLDGenerator;
import org.apache.tiles.autotag.jsp.TagClassGenerator;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.app.VelocityEngine;


/**
 * Goal which touches a timestamp file.
 *
 * @goal generate-jsp
 *
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateJspMojo extends AbstractGenerateMojo {

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-jsp-classes"
     * @required
     */
    private File classesOutputDirectory;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-jsp-resources"
     * @required
     */
    private File resourcesOutputDirectory;

    /**
     * Name of the package.
     * @parameter expression="sample"
     * @required
     */
    private String packageName;

    /**
     * URI of the tag library.
     *
     * @parameter expression="http://www.example.com/tags/example"
     */
    private String taglibURI;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    protected void generate(TemplateSuite suite, VelocityEngine velocityEngine) {
        BasicTemplateGenerator generator = new BasicTemplateGenerator();
        generator.addTemplateSuiteGenerator(resourcesOutputDirectory, new TLDGenerator(velocityEngine));
        generator.addTemplateClassGenerator(classesOutputDirectory, new TagClassGenerator(velocityEngine));
        suite.getCustomVariables().put("taglibURI", taglibURI);
        generator.generate(packageName, suite);

        Resource resource = new Resource();
        resource.setDirectory(resourcesOutputDirectory.getAbsolutePath());
        project.addResource(resource);
        project.addCompileSourceRoot(classesOutputDirectory.getAbsolutePath());
    }
}
