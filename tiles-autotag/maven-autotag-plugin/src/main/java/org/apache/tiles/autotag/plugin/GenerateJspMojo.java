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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tiles.autotag.generate.BasicTemplateGenerator;
import org.apache.tiles.autotag.jsp.TLDGenerator;
import org.apache.tiles.autotag.jsp.TagClassGenerator;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.app.Velocity;

import com.thoughtworks.xstream.XStream;

/**
 * Goal which touches a timestamp file.
 *
 * @goal generate-jsp
 *
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateJspMojo extends AbstractMojo {
    private static final String META_INF_TEMPLATE_SUITE_XML = "META-INF/template-suite.xml";

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-jsp"
     * @required
     */
    private File outputDirectory;

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
     * The project
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List<String> classpathElements;

    public void execute() throws MojoExecutionException {
        try {
            InputStream stream = findTemplateSuiteDescriptor();
            XStream xstream = new XStream();
            TemplateSuite suite = (TemplateSuite) xstream.fromXML(stream);
            stream.close();
            BasicTemplateGenerator generator = new BasicTemplateGenerator();
            generator.addTemplateSuiteGenerator(new TLDGenerator());
            generator.addTemplateClassGenerator(new TagClassGenerator());
            suite.getCustomVariables().put("taglibURI", taglibURI);

            Properties props = new Properties();
            InputStream propsStream = getClass().getResourceAsStream("/org/apache/tiles/autotag/jsp/velocity.properties");
            props.load(propsStream);
            propsStream.close();
            Velocity.init(props);

            generator.generate(outputDirectory, packageName, suite);
        } catch (IOException e) {
            throw new MojoExecutionException("error", e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("error", e);
        }
    }

    private InputStream findTemplateSuiteDescriptor() throws IOException {
        InputStream retValue = null;

        for (String path: classpathElements) {
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
