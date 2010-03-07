package org.apache.tiles.autotag.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;

public class BasicTemplateGenerator implements TemplateGenerator {

    private List<TSGeneratorDirectoryPair> templateSuiteGenerators;

    private List<TCGeneratorDirectoryPair> templateClassGenerators;

    public BasicTemplateGenerator() {
        templateClassGenerators = new ArrayList<TCGeneratorDirectoryPair>();
        templateSuiteGenerators = new ArrayList<TSGeneratorDirectoryPair>();
    }

    @Override
    public void generate(String packageName, TemplateSuite suite) {
        for (TSGeneratorDirectoryPair pair: templateSuiteGenerators) {
            pair.getGenerator().generate(pair.getDirectory(), packageName, suite);
        }
        for (TemplateClass templateClass: suite.getTemplateClasses()) {
            for (TCGeneratorDirectoryPair pair: templateClassGenerators) {
                pair.getGenerator().generate(pair.getDirectory(), packageName,
                        suite, templateClass);
            }
        }
    }

    public void addTemplateSuiteGenerator(File outputDirectory,
            TemplateSuiteGenerator generator) {
        templateSuiteGenerators.add(new TSGeneratorDirectoryPair(
                outputDirectory, generator));
    }

    public void addTemplateClassGenerator(File outputDirectory, TemplateClassGenerator generator) {
        templateClassGenerators.add(new TCGeneratorDirectoryPair(outputDirectory, generator));
    }

    private static class TSGeneratorDirectoryPair {
        private File directory;

        private TemplateSuiteGenerator generator;

        public TSGeneratorDirectoryPair(File directory,
                TemplateSuiteGenerator generator) {
            this.directory = directory;
            this.generator = generator;
        }

        public File getDirectory() {
            return directory;
        }

        public TemplateSuiteGenerator getGenerator() {
            return generator;
        }
    }

    private static class TCGeneratorDirectoryPair {
        private File directory;

        private TemplateClassGenerator generator;

        public TCGeneratorDirectoryPair(File directory,
                TemplateClassGenerator generator) {
            this.directory = directory;
            this.generator = generator;
        }

        public File getDirectory() {
            return directory;
        }

        public TemplateClassGenerator getGenerator() {
            return generator;
        }
    }
}
