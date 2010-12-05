package org.apache.tiles.autotag.generate;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.tiles.autotag.model.TemplateClass;
import org.apache.tiles.autotag.model.TemplateSuite;

class BasicTemplateGenerator implements TemplateGenerator {

    private List<TSGeneratorDirectoryPair> templateSuiteGenerators;

    private List<TCGeneratorDirectoryPair> templateClassGenerators;

    private boolean generatingResources = false;

    private boolean generatingClasses = false;

    BasicTemplateGenerator(
            List<TSGeneratorDirectoryPair> templateSuiteGenerators,
            List<TCGeneratorDirectoryPair> templateClassGenerators,
            boolean generatingClasses, boolean generatingResources) {
        this.templateSuiteGenerators = templateSuiteGenerators;
        this.templateClassGenerators = templateClassGenerators;
        this.generatingClasses = generatingClasses;
        this.generatingResources = generatingResources;
    }



    @Override
    public void generate(String packageName, TemplateSuite suite, Map<String, String> parameters) {
        for (TSGeneratorDirectoryPair pair: templateSuiteGenerators) {
            pair.getGenerator().generate(pair.getDirectory(), packageName, suite, parameters);
        }
        for (TemplateClass templateClass: suite.getTemplateClasses()) {
            for (TCGeneratorDirectoryPair pair: templateClassGenerators) {
                pair.getGenerator().generate(pair.getDirectory(), packageName,
                        suite, templateClass, parameters);
            }
        }
    }

    static class TSGeneratorDirectoryPair {
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

    static class TCGeneratorDirectoryPair {
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

    @Override
    public boolean isGeneratingResources() {
        return generatingResources;
    }

    @Override
    public boolean isGeneratingClasses() {
        return generatingClasses;
    }
}
