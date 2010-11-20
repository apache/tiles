package org.apache.tiles.autotag.plugin;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.freemarker.FMModelGenerator;
import org.apache.tiles.autotag.freemarker.FMModelRepositoryGenerator;
import org.apache.tiles.autotag.generate.BasicTemplateGenerator;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.app.VelocityEngine;

/**
 * Generates Freemarker code.
 *
 * @goal generate-freemarker
 *
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GenerateFreemarkerMojo extends AbstractGenerateMojo {

    /**
     * Name of the package.
     * @parameter expression="sample"
     * @required
     */
    private String packageName;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-freemarker-classes"
     * @required
     */
    private File classesOutputDirectory;

    @Override
    protected void generate(TemplateSuite suite, VelocityEngine velocityEngine) {
        BasicTemplateGenerator generator = new BasicTemplateGenerator();
        generator.addTemplateSuiteGenerator(classesOutputDirectory, new FMModelRepositoryGenerator(velocityEngine));
        generator.addTemplateClassGenerator(classesOutputDirectory, new FMModelGenerator(velocityEngine));
        generator.generate(packageName, suite);

        project.addCompileSourceRoot(classesOutputDirectory.getAbsolutePath());
    }
}
