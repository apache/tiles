package org.apache.tiles.autotag.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.autotag.generate.BasicTemplateGenerator.TCGeneratorDirectoryPair;
import org.apache.tiles.autotag.generate.BasicTemplateGenerator.TSGeneratorDirectoryPair;

public class TemplateGeneratorBuilder {

    private List<TSGeneratorDirectoryPair> templateSuiteGenerators;

    private List<TCGeneratorDirectoryPair> templateClassGenerators;

    private boolean generatingResources = false;

    private boolean generatingClasses = false;

    /**
     * The classes output directory.
     */
    private File classesOutputDirectory;

    /**
     * The resources output directory.
     */
    private File resourcesOutputDirectory;

    private TemplateGeneratorBuilder() {
        templateSuiteGenerators = new ArrayList<BasicTemplateGenerator.TSGeneratorDirectoryPair>();
        templateClassGenerators = new ArrayList<BasicTemplateGenerator.TCGeneratorDirectoryPair>();
    }

    public static TemplateGeneratorBuilder createNewInstance() {
        return new TemplateGeneratorBuilder();
    }

    public TemplateGeneratorBuilder setClassesOutputDirectory(File classesOutputDirectory) {
        this.classesOutputDirectory = classesOutputDirectory;
        return this;
    }

    public TemplateGeneratorBuilder setResourcesOutputDirectory(File resourcesOutputDirectory) {
        this.resourcesOutputDirectory = resourcesOutputDirectory;
        return this;
    }

    public TemplateGeneratorBuilder addClassesTemplateSuiteGenerator(TemplateSuiteGenerator generator) {
        if (classesOutputDirectory == null) {
            throw new NullPointerException("Classes output directory not specified, call 'setClassesOutputDirectory' first");
        }
        templateSuiteGenerators.add(new TSGeneratorDirectoryPair(
                classesOutputDirectory, generator));
        generatingClasses = true;
        return this;
    }

    public TemplateGeneratorBuilder addClassesTemplateClassGenerator(TemplateClassGenerator generator) {
        if (classesOutputDirectory == null) {
            throw new NullPointerException("Classes output directory not specified, call 'setClassesOutputDirectory' first");
        }
        templateClassGenerators.add(new TCGeneratorDirectoryPair(
                classesOutputDirectory, generator));
        generatingClasses = true;
        return this;
    }

    public TemplateGeneratorBuilder addResourcesTemplateSuiteGenerator(TemplateSuiteGenerator generator) {
        if (resourcesOutputDirectory == null) {
            throw new NullPointerException("Resources output directory not specified, call 'setClassesOutputDirectory' first");
        }
        templateSuiteGenerators.add(new TSGeneratorDirectoryPair(
                resourcesOutputDirectory, generator));
        generatingResources = true;
        return this;
    }

    public TemplateGeneratorBuilder addResourcesTemplateClassGenerator(TemplateClassGenerator generator) {
        if (resourcesOutputDirectory == null) {
            throw new NullPointerException("Resources output directory not specified, call 'setClassesOutputDirectory' first");
        }
        templateClassGenerators.add(new TCGeneratorDirectoryPair(
                resourcesOutputDirectory, generator));
        generatingResources = true;
        return this;
    }

    public TemplateGenerator build() {
        return new BasicTemplateGenerator(templateSuiteGenerators,
                templateClassGenerators, generatingClasses, generatingResources);
    }

}
