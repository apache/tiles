package org.apache.tiles.autotag.generate;

import java.io.File;

import org.apache.tiles.autotag.model.TemplateSuite;

public interface TemplateSuiteGenerator {

    void generate(File directory, String packageName, TemplateSuite suite);
}
