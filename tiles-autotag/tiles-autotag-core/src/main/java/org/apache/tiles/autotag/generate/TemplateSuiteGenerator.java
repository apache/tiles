package org.apache.tiles.autotag.generate;

import java.io.File;
import java.util.Map;

import org.apache.tiles.autotag.model.TemplateSuite;

public interface TemplateSuiteGenerator {

    void generate(File directory, String packageName, TemplateSuite suite, Map<String, String> parameters);
}
