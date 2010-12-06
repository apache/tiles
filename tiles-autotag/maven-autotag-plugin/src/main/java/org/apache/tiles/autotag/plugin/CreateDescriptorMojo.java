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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.core.QDoxTemplateSuiteFactory;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;

import com.thoughtworks.xstream.XStream;

/**
 * Goal which touches a timestamp file.
 *
 * @goal create-descriptor
 *
 * @phase generate-resources
 */
public class CreateDescriptorMojo extends AbstractMojo {
    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-template-suite"
     * @required
     */
    File outputDirectory;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     */
    File sourceDirectory;

    /**
     * @parameter
     */
    Set<String> includes;

    /**
     * The name of the template.
     *
     * @parameter
     * @required
     */
    String name;

    /**
     * The documentation of the suite
     *
     * @parameter
     */
    String documentation;

    /**
     * @parameter
     */
    Set<String> excludes;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException {
        try {
            Set<File> filesSet = getSourceInclusionScanner().getIncludedSources(
                    sourceDirectory, outputDirectory);
            File[] files = new File[filesSet.size()];
            QDoxTemplateSuiteFactory factory = new QDoxTemplateSuiteFactory(filesSet.toArray(files));
            factory.setSuiteName(name);
            factory.setSuiteDocumentation(documentation);
            TemplateSuite suite = factory.createTemplateSuite();
            XStream xstream = new XStream();
            File dir = new File(outputDirectory, "META-INF");
            dir.mkdirs();
            File outputFile = new File(dir, "template-suite.xml");
            outputFile.createNewFile();
            Writer writer = new FileWriter(outputFile);
            xstream.toXML(suite, writer);
            writer.close();
            Resource resource = new Resource();
            resource.setDirectory(outputDirectory.getAbsolutePath());
            project.addResource(resource);
        } catch (InclusionScanException e) {
            throw new MojoExecutionException("error", e);
        } catch (IOException e) {
            throw new MojoExecutionException("error", e);
        }
    }

    private SourceInclusionScanner getSourceInclusionScanner() {
        SourceInclusionScanner scanner = null;
        if (includes == null) {
            includes = new HashSet<String>();
        }
        if (excludes == null) {
            excludes = new HashSet<String>();
        }

        if (includes.isEmpty() && excludes.isEmpty()) {
            includes = Collections.singleton("**/*Model.java");
            scanner = new SimpleSourceInclusionScanner(includes, excludes);
        } else {
            if (includes.isEmpty()) {
                includes = Collections.singleton("**/*Model.java");
            }
            scanner = new SimpleSourceInclusionScanner(includes, excludes);
        }
        scanner.addSourceMapping(new SourceMapping() {

            @SuppressWarnings("rawtypes")
            @Override
            public Set getTargetFiles(File targetDir, String source) {
                return null;
            }
        });

        return scanner;
    }
}
