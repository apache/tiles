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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.generate.TemplateGenerator;
import org.apache.tiles.autotag.generate.TemplateGeneratorFactory;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.app.VelocityEngine;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 * Abstract class to generate boilerplate code starting from template model classes.
 *
 * @version $Rev$ $Date$
 */
public abstract class AbstractGenerateMojo extends AbstractMojo {
    /**
     * The position of the template suite XML descriptor.
     */
    static final String META_INF_TEMPLATE_SUITE_XML = "META-INF/template-suite.xml";

    /**
     * The classpath elements.
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    List<String> classpathElements;

    /**
     * Location of the generated classes.
     *
     * @parameter expression="${project.build.directory}/autotag-classes"
     * @required
     */
    File classesOutputDirectory;

    /**
     * Location of the generated resources.
     *
     * @parameter expression="${project.build.directory}/autotag-resources"
     * @required
     */
    File resourcesOutputDirectory;

    /**
     * Name of the package.
     * @parameter expression="sample"
     * @required
     */
    String packageName;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    /** {@inheritDoc} */
    public void execute() throws MojoExecutionException {
        try {
            InputStream stream = findTemplateSuiteDescriptor();
            XStream xstream = new XStream(new Sun14ReflectionProvider());
            TemplateSuite suite = (TemplateSuite) xstream.fromXML(stream);
            stream.close();

            Properties props = new Properties();
            InputStream propsStream = getClass().getResourceAsStream("/org/apache/tiles/autotag/velocity.properties");
            props.load(propsStream);
            propsStream.close();
            TemplateGenerator generator = createTemplateGeneratorFactory(
                    new VelocityEngine(props)).createTemplateGenerator();
            generator.generate(packageName, suite, getParameters());
            if (generator.isGeneratingResources()) {
                Resource resource = new Resource();
                resource.setDirectory(resourcesOutputDirectory.getAbsolutePath());
                project.addResource(resource);
            }
            if (generator.isGeneratingClasses()) {
                project.addCompileSourceRoot(classesOutputDirectory.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new MojoExecutionException("error", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("error", e);
        }
    }

    /**
     * Creates a template generator factory.
     *
     * @param velocityEngine The Velocity engine.
     * @return The template generator factory.
     */
    protected abstract TemplateGeneratorFactory createTemplateGeneratorFactory(VelocityEngine velocityEngine);

    /**
     * Returns the map of parameters.
     *
     * @return The parameters.
     */
    protected abstract Map<String, String> getParameters();

    /**
     * Searches for the template suite descriptor in all dependencies and sources.
     *
     * @return The inputstream of the identified descriptor.
     * @throws IOException If something goes wrong.
     */
    private InputStream findTemplateSuiteDescriptor() throws IOException {
        InputStream retValue = null;

        for (String path : classpathElements) {
            File file = new File(path);
            if (file.isDirectory()) {
                File candidate = new File(file, META_INF_TEMPLATE_SUITE_XML);
                if (candidate.exists()) {
                    return new FileInputStream(candidate);
                }
            } else if (file.getPath().endsWith(".jar")) {
                JarFile jar = new JarFile(file);
                ZipEntry entry = jar.getEntry(META_INF_TEMPLATE_SUITE_XML);
                if (entry != null) {
                    return jar.getInputStream(entry);
                }
            }
        }

        return retValue;
    }

}
