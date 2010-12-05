package org.apache.tiles.autotag.freemarker;

import java.io.File;

import org.apache.tiles.autotag.generate.TemplateGenerator;
import org.apache.tiles.autotag.generate.TemplateGeneratorBuilder;
import org.apache.tiles.autotag.generate.TemplateGeneratorFactory;
import org.apache.velocity.app.VelocityEngine;

public class FMTemplateGeneratorFactory implements TemplateGeneratorFactory {

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/autotag-jsp-classes"
     * @required
     */
    private File classesOutputDirectory;

    private VelocityEngine velocityEngine;

    private TemplateGeneratorBuilder templateGeneratorBuilder;

    public FMTemplateGeneratorFactory(File classesOutputDirectory,
            VelocityEngine velocityEngine, TemplateGeneratorBuilder templateGeneratorBuilder) {
        this.classesOutputDirectory = classesOutputDirectory;
        this.velocityEngine = velocityEngine;
        this.templateGeneratorBuilder = templateGeneratorBuilder;
    }

    @Override
    public TemplateGenerator createTemplateGenerator() {
        return templateGeneratorBuilder
                .setClassesOutputDirectory(classesOutputDirectory)
                .addClassesTemplateSuiteGenerator(
                        new FMModelRepositoryGenerator(velocityEngine))
                .addClassesTemplateClassGenerator(
                        new FMModelGenerator(velocityEngine)).build();
    }

}
