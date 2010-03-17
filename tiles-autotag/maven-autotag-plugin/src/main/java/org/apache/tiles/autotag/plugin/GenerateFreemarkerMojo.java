package org.apache.tiles.autotag.plugin;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.apache.tiles.autotag.freemarker.FMModelGenerator;
import org.apache.tiles.autotag.freemarker.FMModelRepositoryGenerator;
import org.apache.tiles.autotag.generate.BasicTemplateGenerator;
import org.apache.tiles.autotag.model.TemplateSuite;

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
    protected void generate(TemplateSuite suite) {
        BasicTemplateGenerator generator = new BasicTemplateGenerator();
        generator.addTemplateSuiteGenerator(classesOutputDirectory, new FMModelRepositoryGenerator());
        generator.addTemplateClassGenerator(classesOutputDirectory, new FMModelGenerator());
        generator.generate(packageName, suite);

        project.addCompileSourceRoot(classesOutputDirectory.getAbsolutePath());
    }
}
