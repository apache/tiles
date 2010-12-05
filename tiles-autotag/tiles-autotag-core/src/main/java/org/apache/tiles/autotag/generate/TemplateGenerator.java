package org.apache.tiles.autotag.generate;


import java.util.Map;

import org.apache.tiles.autotag.model.TemplateSuite;

public interface TemplateGenerator {

    void generate(String packageName, TemplateSuite suite, Map<String, String> parameters);

    boolean isGeneratingResources();

    boolean isGeneratingClasses();
}
