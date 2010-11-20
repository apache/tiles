package org.apache.tiles.autotag.freemarker;

import java.io.File;

import org.apache.tiles.autotag.generate.AbstractTemplateSuiteGenerator;
import org.apache.tiles.autotag.model.TemplateSuite;
import org.apache.velocity.app.VelocityEngine;

public class FMModelRepositoryGenerator extends AbstractTemplateSuiteGenerator {

    public FMModelRepositoryGenerator(VelocityEngine velocityEngine) {
        super(velocityEngine);
    }

    protected String getTemplatePath(File directory, String packageName, TemplateSuite suite) {
        return "/org/apache/tiles/autotag/freemarker/repository.vm";
    }

    protected String getFilename(File directory, String packageName, TemplateSuite suite) {
        String name = suite.getName();
        return name.substring(0, 1).toUpperCase() + name.substring(1) + "FMModelRepository.java";
    }

    protected String getDirectoryName(File directory, String packageName, TemplateSuite suite) {
        return packageName.replaceAll("\\.", "/");
    }

}
