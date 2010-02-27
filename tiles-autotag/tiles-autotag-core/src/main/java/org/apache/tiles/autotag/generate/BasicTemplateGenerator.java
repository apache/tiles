package org.apache.tiles.autotag.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;

public class BasicTemplateGenerator implements TemplateGenerator {

    private List<TemplateSuiteGenerator> templateSuiteGenerators;

    private List<TemplateClassGenerator> templateClassGenerators;

    public BasicTemplateGenerator() {
        templateClassGenerators = new ArrayList<TemplateClassGenerator>();
        templateSuiteGenerators = new ArrayList<TemplateSuiteGenerator>();
    }

    @Override
    public void generate(File directory, String packageName, TemplateSuite suite) {
        for (TemplateSuiteGenerator generator: templateSuiteGenerators) {
            generator.generate(directory, packageName, suite);
        }
        for (TemplateClass templateClass: suite.getTemplateClasses()) {
            for (TemplateClassGenerator generator: templateClassGenerators) {
                generator.generate(directory, packageName, suite, templateClass);
            }
        }
    }

    protected void addTemplateSuiteGenerator(TemplateSuiteGenerator generator) {
        templateSuiteGenerators.add(generator);
    }

    protected void addTemplateClassGenerator(TemplateClassGenerator generator) {
        templateClassGenerators.add(generator);
    }
}
